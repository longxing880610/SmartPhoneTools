package com.longxing.database;

import android.database.sqlite.SQLiteDatabase;

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
    public void newTable(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(cNameTable);// getClass().getFields()[0].getName();
        sql.append("(_id INTEGER PRIMARY KEY AUTOINCREMENT");
        for (Field item : FileInforModel.class.getFields()) {
            sql.append(", " + item.getName() + " TEXT");
        }
        sql.append(");");
        db.execSQL(sql.toString());
    }

    @Override
    public void upGradeTable(SQLiteDatabase db) {

    }

}
