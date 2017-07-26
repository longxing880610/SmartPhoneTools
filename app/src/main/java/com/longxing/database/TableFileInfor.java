package com.longxing.database;

import com.longxing.database.datamodel.FileInforModel;

import java.lang.reflect.Field;

/**
 * Created by Zhang Long on 2017/7/23.
 * <p>
 * table of file information
 */

public class TableFileInfor implements ITableDb {

    public static final String cNameTable = "FileInfor";


    public TableFileInfor() {

    }

    public void NewTable() {

    }

    @Override
    public String newTable() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(cNameTable);// getClass().getFields()[0].getName();
        sql.append("(_id INTEGER PRIMARY KEY AUTOINCREMENT");
        for (Field item : FileInforModel.class.getFields()) {
            sql.append(", " + item.getName() + " TEXT");
        }
        sql.append(");");
        //db.execSQL(sql.toString());
        return sql.toString();
    }

    @Override
    public String upGradeTable(int oldVersion, int newVersion) {
        return null;
    }

}