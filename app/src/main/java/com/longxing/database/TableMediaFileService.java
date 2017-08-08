package com.longxing.database;

import android.content.Context;

import com.longxing.database.datamodel.DaoMaster;
import com.longxing.database.datamodel.DaoSession;
import com.longxing.database.datamodel.FileInforModel;
import com.longxing.database.datamodel.FileInforModelDao;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Zhang Long on 2017/7/23.
 * <p>
 * table of file information
 */

public class TableMediaFileService extends BaseTable implements ITableDb {

    public static final String cNameTable = "MediaFile";


    public TableMediaFileService(Context context) {
        super(context, cNameTable);
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

    /**
     * 插入一条记录
     *
     * @param fileModel
     */
    public void insertUser(FileInforModel fileModel) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileInforModelDao userDao = daoSession.getFileInforModelDao();
        userDao.insert(fileModel);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertUserList(List<FileInforModel> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FileInforModelDao userDao = daoSession.getFileInforModelDao();
        userDao.insertInTx(users);
    }


}
