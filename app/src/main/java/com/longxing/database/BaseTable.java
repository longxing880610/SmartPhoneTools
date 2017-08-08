package com.longxing.database;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.longxing.database.datamodel.DaoMaster;

import java.io.File;

import javax.xml.validation.Schema;

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
            mDevOpenHelper = openHelper;
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

    /**
     * Created by Doraemon
     * Date: 16/5/12
     * Time: 09:22
     * Summary:该类主要用于基于GreenDao框架自定义数据库路径
     */
    public class GreenDaoContext extends ContextWrapper {

        private String currentUserId;
        private Context mContext;

        public GreenDaoContext() {
            super(null);/*
            this.mContext = ApplicationCache.context;
            this.currentUserId = AppUserCache.userInfo.getUserId();*/
        }

        /**
         * 获得数据库路径，如果不存在，则创建对象
         *
         * @param dbName
         */
        @Override
        public File getDatabasePath(String dbName) {
            File baseFile = null;//AppPathUtil.getDbCacheDir(mContext);
            StringBuffer buffer = new StringBuffer();
            buffer.append(baseFile.getPath());
            buffer.append(File.separator);
            buffer.append(currentUserId);
            buffer.append(File.separator);
            buffer.append(dbName);
            return new File(buffer.toString());
        }

        /**
         * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
         *
         * @param name
         * @param mode
         * @param factory
         */
        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                                   SQLiteDatabase.CursorFactory factory) {
            SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
            return result;
        }

        /**
         * Android 4.0会调用此方法获取数据库。
         *
         * @param name
         * @param mode
         * @param factory
         * @param errorHandler
         * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
         * android.database.sqlite.SQLiteDatabase.CursorFactory,
         * android.database.DatabaseErrorHandler)
         */
        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                                   DatabaseErrorHandler errorHandler) {
            SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);

            return result;
        }

    }

}
