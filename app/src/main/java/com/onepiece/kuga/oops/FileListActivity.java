package com.onepiece.kuga.oops;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.onepiece.kuga.Oops;
import com.onepiece.kuga.crypto.*;
import com.onepiece.kuga.models.*;
import com.onepiece.kuga.utils.*;

import java.util.*;

public class FileListActivity extends ListActivity {
    /**
     * Aes 对象
     */
    private static Aes aes;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 文件分类
     */
    private Category category;

    /**
     * 文件列表视图
     */
    private ListView listView;

    /**
     * 文件列表
     */
    private ArrayList<HashMap<String, String>> fileList;

    /**
     * 添加按钮
     */
    private Button btnAdd;

    /**
     * 文件名编辑框
     */
    private EditText editTextFileName;

    /**
     * 文件列表选择的下标
     */
    private int selectedIndex;

    /**
     * 长按时可选的编辑选项
     */
    private final CharSequence[] editItems = {"Rename", "Delete"};

    /**
     * 日志标签
     */
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        // 初始化
        context = this;
        listView = this.getListView();
        btnAdd = (Button) findViewById(R.id.btnAdd);
        fileList = new ArrayList<HashMap<String, String>>();
        aes = ((Oops) getApplication()).getAes();
        category = Category.getOne(getIntent().getStringExtra("categoryId"));

        // 添加监听事件
        listView.setOnItemClickListener(openFileListener);
        listView.setOnItemLongClickListener(editFileListener);
        btnAdd.setOnClickListener(addFileListener);

        renderFileList();
    }

    /**
     * Render file list
     */
    private void renderFileList() {
        fileList = new ArrayList<HashMap<String, String>>();

        List<File> files = category.files();

        if (files == null || files.isEmpty()) {
            listView.setBackgroundResource(R.drawable.empty_tips);
        } else {
            for (File file : files) {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("fileId", file.getId().toString());
                map.put("fileName", aes.decrypt(file.name));
                map.put("fileMeta", Datetime.format(file.timestamp));

                fileList.add(map);
            }

            listView.setBackgroundResource(0);
        }

        SimpleAdapter simple = new SimpleAdapter(
                context,
                fileList,
                R.layout.file_item,
                new String[] {"fileName", "fileMeta"},
                new int[] {R.id.fileName, R.id.fileMeta}
        );

        listView.setAdapter(simple);
    }

    private GridView.OnItemClickListener openFileListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
            String fileId = fileList.get(index).get("fileId");
            Intent intent = new Intent(context, FileActivity.class);
            intent.putExtra("fileId", fileId);
            startActivity(intent);
        }
    };

    private GridView.OnItemLongClickListener editFileListener = new GridView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long id) {
            selectedIndex = index;

            new AlertDialog.Builder(context)
                    .setTitle(R.string.edit)
                    .setItems(editItems, itemClickListener)
                    .create().show();

            return true;
        }
    };

    private DialogInterface.OnClickListener itemClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            if (editItems[id].equals("Delete")) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.warning)
                        .setPositiveButton(R.string.confirm, confirmDeleteFolderListener)
                        .setNegativeButton(R.string.cancel, CommonDialog.cancelListener)
                        .create().show();
            } else if (editItems[id].equals("Rename")) {
                editTextFileName = new EditText(context);
                editTextFileName.setSingleLine(true);

                new AlertDialog.Builder(context)
                        .setTitle(R.string.rename)
                        .setView(editTextFileName)
                        .setPositiveButton(R.string.confirm, confirmRenameFileListener)
                        .setNegativeButton(R.string.cancel, CommonDialog.cancelListener)
                        .create().show();
            }
        }
    };

    private DialogInterface.OnClickListener confirmDeleteFolderListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            String fileId = fileList.get(selectedIndex).get("fileId");
            File file = File.getOne(fileId);

            file.delete();

            renderFileList();
            Log.d(TAG, "file deleted: " + aes.decrypt(file.name));
        }
    };

    private DialogInterface.OnClickListener confirmRenameFileListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            String name = editTextFileName.getText().toString();

            // 名称不能为空，也不能重复
            if (name.equals("")) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_empty);
            } else if (exists(name)) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_exists);
            } else {
                String fileId = fileList.get(selectedIndex).get("fileId");
                File file = File.getOne(fileId);

                file.name = aes.encrypt(name);
                file.save();

                renderFileList();
                Log.d(TAG, "file renamed: " + name);
            }
        }
    };

    private View.OnClickListener addFileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editTextFileName = new EditText(context);
            editTextFileName.setSingleLine(true);

            new AlertDialog.Builder(context)
                    .setTitle(R.string.file_name)
                    .setView(editTextFileName)
                    .setPositiveButton(R.string.confirm, confirmAddFileListener)
                    .setNegativeButton(R.string.cancel, CommonDialog.cancelListener)
                    .create().show();
        }
    };

    private DialogInterface.OnClickListener confirmAddFileListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            String name = editTextFileName.getText().toString();

            // 名称不能为空，也不能重复
            if (name.equals("")) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_empty);
            } else if (exists(name)) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_exists);
            } else {
                File file = new File();

                file.category = category;
                file.name = aes.encrypt(name);
                file.timestamp = System.currentTimeMillis();
                file.save();

                renderFileList();
                Log.d(TAG, "file added: " + name);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.about:
                CommonDialog.showTips(context, R.string.about, R.string.about_msg);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 文件名是否已存在
     *
     * @param name 名字
     * @return bool
     */
    private boolean exists(String name) {
        List<File> files = category.files();

        for (File i : files) {
            if (name.equals(aes.decrypt(i.name))) {
                return true;
            }
        }

        return false;
    }
}
