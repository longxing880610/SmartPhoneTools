package com.longxing.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Zhang Long on 2017/8/8.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseServiceTest {
    @Test
    public void getTable() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        DatabaseService dbService = DatabaseService.getInstance(appContext);

        ITableDb table = dbService.getTable(TableProfileService.class);

        Assert.assertNotNull(table);
        //return table;
    }

}