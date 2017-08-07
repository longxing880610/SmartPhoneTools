package com.longxing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zhang Long on 2017/7/23.
 * <p>
 * service for database
 */
public class DatabaseService{
    private static final String DATABASE_NAME = "smartPhone.db";//数据库名称
    private static final int SCHEMA_VERSION = 1;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

    private List<ITableDb> mListTabs = new ArrayList<>();
    private static DatabaseService s_ownObj;

    /**
     * get instance of database
     *
     * @param context context of database
     * @return instance of database
     */
    public static DatabaseService getInstance(Context context) {
        if (s_ownObj == null) {
            synchronized (DatabaseService.class) {
                if (s_ownObj == null) {
                    s_ownObj = new DatabaseService(context);
                }
            }
        }
        return s_ownObj;
    }

    /**
     * @param context
     */
    public DatabaseService(Context context) {//构造函数,接收上下文作为参数,直接调用的父类的构造函数
        //super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        mListTabs.add(new TableFileInfor(context));
    }

    //@Override
    public void onCreate(SQLiteDatabase db) {//创建的是一个午餐订餐的列表,id,菜名,地址等等
        //db.execSQL("CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT, phone TEXT);");
        for (ITableDb item : mListTabs) {
            String sql = item.newTable();
            if (sql != null) {
                db.execSQL(sql);
            }
        }
    }

    //@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
            //db.execSQL("ALTER TABLE restaurants ADD phone TEXT;");
        }

        for (ITableDb item : mListTabs) {
            String sql = item.upGradeTable(oldVersion, newVersion);
            if (sql != null) {
                db.execSQL(sql);
            }
        }
    }
}