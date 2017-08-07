package com.longxing.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.longxing.database.datamodel.DaoMaster;

/**
 * Created by Zhang Long on 2017/8/7.
 * base class of database table
 */
public class BaseTable {
    protected Context mContext;
    protected String mTableName;
    protected DaoMaster.DevOpenHelper mDevOpenHelper;


    public BaseTable(Context context, String tableName) {
        mContext = context;
        mTableName = tableName;
    }

    /**
     * get open helper
     *
     * @return
     */
    private DaoMaster.DevOpenHelper getDevOpenHelper() {
        DaoMaster.DevOpenHelper openHelper = mDevOpenHelper;
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, mTableName, null);
        }
        return openHelper;
    }

    /**
     * get readable database object
     *
     * @return
     */
    protected SQLiteDatabase getReadableDatabase() {
        DaoMaster.DevOpenHelper openHelper = getDevOpenHelper();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * get writable database object
     *
     * @return
     */
    protected SQLiteDatabase getWritableDatabase() {
        DaoMaster.DevOpenHelper openHelper = getDevOpenHelper();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

}
