package info.breezes.itebooks.app.model;

import info.breezes.orm.annotation.Column;
import info.breezes.orm.annotation.Table;

import java.io.Serializable;

/**
 * Created by jianxingqiao on 14-6-14.
 */

@Table(name = "books")
public class Book implements Serializable {
    @Column
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
}
