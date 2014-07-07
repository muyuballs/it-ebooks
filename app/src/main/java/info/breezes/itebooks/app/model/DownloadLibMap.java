package info.breezes.itebooks.app.model;

import info.breezes.orm.annotation.Column;
import info.breezes.orm.annotation.Table;

/**
 * Created by jianxingqiao on 14-6-25.
 */
@Table(name="download_lib_map")
public class DownloadLibMap {
    @Column(name = "_id",primaryKey = true,autoincrement = true)
    public int id;
    @Column
    public long downloadId;
    @Column
    public String ISBN;
}
