package com.onepiece.kuga.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "file")
public class File extends Model {
    @Column(name = "category", onDelete = Column.ForeignKeyAction.CASCADE)
    public Category category;

    @Column(name = "name")
    public String name;

    @Column(name = "content")
    public String content;

    @Column(name = "timestamp")
    public long timestamp;

    /**
     * 构造函数
     */
    public File() {
        super();
    }

    /**
     * 构造函数
     *
     * @param category 分类
     * @param name 名字
     * @param content 内容
     */
    public File(Category category, String name, String content) {
        this(category, name, content, System.currentTimeMillis());
    }

    /**
     * 构造函数
     * 
     * @param category 分类
     * @param name 名字
     * @param content 内容
     * @param timestamp 时间戳
     */
    public File(Category category, String name, String content, long timestamp) {
        super();
        this.category = category;
        this.name = name;
        this.content = content;
        this.timestamp = timestamp;
    }

    /**
     * 获取所有文件
     *
     * @param id id
     * @return File
     */
    public static File getOne(String id) {
        return getOne(Long.valueOf(id));
    }

    /**
     * 获取所有文件
     *
     * @param id id
     * @return File
     */
    public static File getOne(Long id) {
        return load(File.class, id);
    }
}
