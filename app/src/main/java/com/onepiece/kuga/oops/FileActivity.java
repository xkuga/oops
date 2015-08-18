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

public class FileActivity extends Activity {
    /**
     * Aes 对象
     */
    private static Aes aes;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 文件
     */
    private File file;

    /**
     * 编辑框
     */
    private EditText editText;

    /**
     * 日志标签
     */
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        // 初始化
        context = this;
        editText = (EditText) findViewById(R.id.editText);
        aes = ((Oops) getApplication()).getAes();
        file = File.getOne(getIntent().getStringExtra("fileId"));

        //noinspection ConstantConditions
        getActionBar().setTitle(aes.decrypt(file.name));
        editText.setText(aes.decrypt(file.content));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.readMode) {
            setEditable(false);
        } else if (id == R.id.editMode) {
            setEditable(true);
        } else if (id == R.id.save) {
            saveFile();
            Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveFile();
    }

    /**
     * 设置编辑框是否可编辑
     *
     * @param bool bool
     */
    public void setEditable(boolean bool) {
        editText.setClickable(bool);
        editText.setCursorVisible(bool);
        editText.setFocusable(bool);
        editText.setFocusableInTouchMode(bool);
    }

    /**
     * 保存文件内容
     */
    public void saveFile() {
        file.content = aes.encrypt(editText.getText().toString());
        file.save();
        Log.d(TAG, "saved: " + aes.decrypt(file.name));
    }
}
