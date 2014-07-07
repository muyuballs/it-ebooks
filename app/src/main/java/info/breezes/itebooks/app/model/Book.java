package info.breezes.itebooks.app.model;

import info.breezes.orm.annotation.Column;
import info.breezes.orm.annotation.Table;

import java.io.Serializable;

/**
 * Created by jianxingqiao on 14-6-14.
 */

@Table(name = "books")
public class Book implements Serializable {
    @Column(name = "_id")
    public long ID;
    @Column
    public String Title;
    @Column
    public String SubTitle;
    @Column
    public String Description;
    @Column
    public String Author;
    @Column
    public String Year;
    @Column
    public String Page;
    @Column
    public String Publisher;
    @Column
    public String Image;
    @Column(primaryKey = true, autoincrement = false)
    public String ISBN;
    @Column
    public String Download;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Book book = (Book) o;

        if (!ISBN.equals(book.ISBN)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ISBN.hashCode();
    }
}
