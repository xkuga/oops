package com.onepiece.kuga;

import com.activeandroid.app.Application;
import com.onepiece.kuga.crypto.Aes;

public class Oops extends Application {
    /**
     * 全局 Aes 对象
     */
    private Aes aes;

    /**
     * 获取全局 Aes 对象
     *
     * @return Aes
     */
    public Aes getAes() {
        return aes;
    }

    /**
     * 设置全局 Aes 对象
     *
     * @param aes Aes 对象
     */
    public void setAes(Aes aes) {
        this.aes = aes;
    }
}
