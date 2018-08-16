package com.example.wumeng.testjava.db;

/**
 * Created by WuMeng on 2018/8/13.
 */
@TableName("book")
public class Book {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @TablePrimaryId(value = "id",isAutoIncrease = true)
    @Column("id")
    private long id;
    @Column("author")
    private String author;
    @Column("version")
    private int version;
    @Column("bookname")
    private String bookName;
}
