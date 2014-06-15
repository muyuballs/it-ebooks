package info.breezes.itebooks.app.model;

import info.breezes.orm.annotation.Column;
import info.breezes.orm.annotation.Table;

/**
 * Created by jianxingqiao on 14-6-15.
 */

@Table(name = "library")
public class Library {
    @Column(primaryKey = true, name = "_id")
    public int Id;
    @Column
    public String name;
    @Column
    public String description;
    @Column
    public boolean shared;
}
