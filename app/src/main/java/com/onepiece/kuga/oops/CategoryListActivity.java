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

public class CategoryListActivity extends ListActivity {
    /**
     * Aes 对象
     */
    private static Aes aes;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 分类列表视图
     */
    private ListView listView;

    /**
     * 分类列表
     */
    private ArrayList<HashMap<String, String>> categoryList;

    /**
     * 添加按钮
     */
    private Button btnAdd;

    /**
     * 分类名编辑框
     */
    private EditText editTextCategoryName;

    /**
     * 分类列表选择的下标
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
        setContentView(R.layout.activity_category_list);

        // 初始化
        context = this;
        listView = this.getListView();
        btnAdd = (Button) findViewById(R.id.btnAdd);
        categoryList = new ArrayList<HashMap<String, String>>();
        aes = ((Oops) getApplication()).getAes();

        // 添加监听事件
        listView.setOnItemClickListener(openCategoryListener);
        listView.setOnItemLongClickListener(editCategoryListener);
        btnAdd.setOnClickListener(addCategoryListener);

        renderCategoryList();
    }

    /**
     * Render category list
     */
    private void renderCategoryList() {
        categoryList = new ArrayList<HashMap<String, String>>();

        List<Category> categories = Category.getAll();

        if (categories == null || categories.isEmpty()) {
            listView.setBackgroundResource(R.drawable.empty_tips);
        } else {
            for (Category category : categories) {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("categoryId", category.getId().toString());
                map.put("categoryName", aes.decrypt(category.name));
                map.put("categoryMeta", Datetime.format(category.timestamp));

                categoryList.add(map);
            }

            listView.setBackgroundResource(0);
        }

        SimpleAdapter simple = new SimpleAdapter(
                context,
                categoryList,
                R.layout.category_item,
                new String[] {"categoryName", "categoryMeta"},
                new int[] {R.id.categoryName, R.id.categoryMeta}
        );

        listView.setAdapter(simple);
    }

    private GridView.OnItemClickListener openCategoryListener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
            String categoryId = categoryList.get(index).get("categoryId");
            Intent intent = new Intent(context, FileListActivity.class);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        }
    };

    private GridView.OnItemLongClickListener editCategoryListener = new GridView.OnItemLongClickListener() {
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
                editTextCategoryName = new EditText(context);
                editTextCategoryName.setSingleLine(true);

                new AlertDialog.Builder(context)
                        .setTitle(R.string.rename)
                        .setView(editTextCategoryName)
                        .setPositiveButton(R.string.confirm, confirmRenameCategoryListener)
                        .setNegativeButton(R.string.cancel, CommonDialog.cancelListener)
                        .create().show();
            }
        }
    };

    private DialogInterface.OnClickListener confirmDeleteFolderListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            String categoryId = categoryList.get(selectedIndex).get("categoryId");
            Category category = Category.getOne(categoryId);

            category.delete();

            renderCategoryList();
            Log.d(TAG, "category deleted: " + aes.decrypt(category.name));
        }
    };

    private DialogInterface.OnClickListener confirmRenameCategoryListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            String name = editTextCategoryName.getText().toString();

            // 名称不能为空，也不能重复
            if (name.equals("")) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_empty);
            } else if (exists(name)) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_exists);
            } else {
                String categoryId = categoryList.get(selectedIndex).get("categoryId");
                Category category = Category.getOne(categoryId);

                category.name = aes.encrypt(name);
                category.save();

                renderCategoryList();
                Log.d(TAG, "category renamed: " + name);
            }
        }
    };

    private View.OnClickListener addCategoryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editTextCategoryName = new EditText(context);
            editTextCategoryName.setSingleLine(true);

            new AlertDialog.Builder(context)
                    .setTitle(R.string.category_name)
                    .setView(editTextCategoryName)
                    .setPositiveButton(R.string.confirm, confirmAddCategoryListener)
                    .setNegativeButton(R.string.cancel, CommonDialog.cancelListener)
                    .create().show();
        }
    };

    private DialogInterface.OnClickListener confirmAddCategoryListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            String name = editTextCategoryName.getText().toString();

            // 名称不能为空，也不能重复
            if (name.equals("")) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_empty);
            } else if (exists(name)) {
                CommonDialog.showTips(context, R.string.tips, R.string.name_exists);
            } else {
                Category category = new Category();

                category.name = aes.encrypt(name);
                category.timestamp = System.currentTimeMillis();
                category.save();

                renderCategoryList();
                Log.d(TAG, "category added: " + name);
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
     * 分类名是否已存在
     *
     * @param name 名字
     * @return bool
     */
    private boolean exists(String name) {
        List<Category> categories = Category.getAll();

        for (Category i : categories) {
            if (name.equals(aes.decrypt(i.name))) {
                return true;
            }
        }

        return false;
    }
}
