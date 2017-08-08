package com.longxing.database;

import android.content.Context;

import com.longxing.database.datamodel.DaoMaster;
import com.longxing.database.datamodel.DaoSession;
import com.longxing.database.datamodel.FileInforModel;
import com.longxing.database.datamodel.FileInforModelDao;
import com.longxing.database.datamodel.ProfileModel;
import com.longxing.database.datamodel.ProfileModelDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Zhang Long on 2017/7/23.
 * <p>
 * table of file information
 */

public class TableProfileService extends BaseTable implements ITableDb {

    public static final String cNameTable = "Profile";


    public TableProfileService(Context context) {
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
     * @param name
     * @param value
     */
    public void insertProfile(String name, String value) {
        ProfileModel profile = new ProfileModel(name, value);

        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ProfileModelDao userDao = daoSession.getProfileModelDao();
        userDao.insert(profile);
    }

    /**
     * 查询某条记录
     *
     * @param name
     */
    public ProfileModel getProfile(String name) {

        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ProfileModelDao userDao = daoSession.getProfileModelDao();
        QueryBuilder<ProfileModel> qb = userDao.queryBuilder();
        qb.where(ProfileModelDao.Properties.ProfileName.gt(name));

        return qb.unique();
    }

}
