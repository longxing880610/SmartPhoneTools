package com.longxing.database.datamodel;

import org.greenrobot.greendao.annotation.*;

/**
 * Created by Zhang Long on 2017/7/23.
 */
public class BaseModel {
    @Id(autoincrement = true)
    @Property(nameInDb = "Id")
    public long id;
    @Property(nameInDb = "Code")
    public String code;
    @Property(nameInDb = "UpdateTime")
    public String updatetime;

}
