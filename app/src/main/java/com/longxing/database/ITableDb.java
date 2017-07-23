package com.longxing.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zhang Long on 2017/7/23.
 *
 * interface table database
 */

public interface ITableDb {
    /**
     *
     * @param db
     */
    void newTable(SQLiteDatabase db);

    /**
     *
     * @param db
     */
    void upGradeTable(SQLiteDatabase db);


}
