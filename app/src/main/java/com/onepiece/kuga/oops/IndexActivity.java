package com.onepiece.kuga.oops;

import com.onepiece.kuga.Oops;
import com.onepiece.kuga.crypto.Aes;
import com.onepiece.kuga.utils.CommonDialog;

import android.content.*;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.app.Activity;

import android.util.Log;


public class IndexActivity extends Activity {
    /**
     * Motto
     */
    private static final String MOTTO = "哎呦不错哦";

    /**
     * 上下文
     */
    private final Context context = this;

    /**
     * 密码框
     */
    private EditText editTextPassword;

    /**
     * 确认按钮
     */
    private Button btnConfirm;

    /**
     * 用于存储 Motto 的密文
     */
    private SharedPreferences share;

    /**
     * 日志标签
     */
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // 初始化
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        share = getSharedPreferences("check", Context.MODE_PRIVATE);

        // 判断是否首次运行应用
        if (isFirstLaunch()) {
            Log.d(TAG, "First launch");
            editTextPassword.setHint(R.string.init_password);
            btnConfirm.setOnClickListener(initPasswordListener);
        } else {
            Log.d(TAG, "Not first launch");
            editTextPassword.setHint(R.string.input_password);
            btnConfirm.setOnClickListener(checkPasswordListener);
        }
    }

    private View.OnClickListener initPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String password = editTextPassword.getText().toString();
            Aes aes = new Aes(password);

            // 记录全局 Aes 对象
            ((Oops) getApplication()).setAes(aes);

            // 把加密后的 motto 存入 xml 文件
            Editor editor = share.edit();
            editor.putString("motto", aes.encrypt(MOTTO));
            editor.apply();

            startCategoryActivity();
        }
    };

    private View.OnClickListener checkPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Aes aes = new Aes(editTextPassword.getText().toString());

            // 记录全局 Aes 对象
            ((Oops) getApplication()).setAes(aes);

            try {
                // 判断是否能还原 MOTTO
                if (aes.decrypt(share.getString("motto", "")).equals(MOTTO)) {
                    Log.d(TAG, "password corrent");
                    startCategoryActivity();
                } else {
                    Log.d(TAG, "password incorret");
                    Toast.makeText(context, R.string.incorrect, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d(TAG, "decryption failed");
                Toast.makeText(context, R.string.incorrect, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 加载 Category Activity
     */
    private void startCategoryActivity() {
        startActivity(new Intent(context, CategoryListActivity.class));
    }

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
     * 是否第一次启动
     *
     * @return bool
     */
    private boolean isFirstLaunch() {
        return share.getString("motto",  "").equals("");
    }
}


