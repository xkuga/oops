package com.onepiece.kuga.models;

import java.util.List;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.*;

@Table(name = "category")
public class Category extends Model {
    @Column(name = "name", unique = true)
    public String name;

    @Column(name = "timestamp")
    public long timestamp;

    /**
     * 构造方法
     */
    public Category() {
        super();
    }

    /**
     * 构造方法
     *
     * @param name 分类名
     */
    public Category(String name) {
        this(name, System.currentTimeMillis());
    }

    /**
     * 构造方法
     *
     * @param name 分类名
     * @param timestamp 时间戳
     */
    public Category(String name, long timestamp) {
        super();
        this.name = name;
        this.timestamp = timestamp;
    }

    /**
     * 获取所有分类
     *
     * @param id id
     * @return Category
     */
    public static Category getOne(String id) {
        return getOne(Long.valueOf(id));
    }

    /**
     * 获取所有分类
     *
     * @param id id
     * @return Category
     */
    public static Category getOne(Long id) {
        return load(Category.class, id);
    }

    /**
     * 获取所有分类
     *
     * @return list
     */
    public static List<Category> getAll() {
        return new Select().from(Category.class).orderBy("timestamp ASC").execute();
    }

    /**
     * get files
     *
     * @return list
     */
    public List<File> files() {
        return getMany(File.class, "category");
    }
}
