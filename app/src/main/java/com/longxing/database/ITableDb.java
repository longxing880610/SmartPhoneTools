package com.longxing.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zhang Long on 2017/7/23.
 * <p>
 * interface table database
 */

public  interface ITableDb {
    /**
     * @return
     */
    String newTable();

    /**
     *
     * @param oldVersion
     * @param newVersion
     * @return
     */
    String upGradeTable(int oldVersion, int newVersion);


}
