package com.longxing.file;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.longxing.log.LogToSystem;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zhang Long on 2017/7/31.
 */
public class SystemSettingTest {

    private static final String TAG = "MyLog/SystemSettingTest/";

    @Test
    public void testAllCfg() throws Exception {
        //MainActivity mainActivity = MainActivity.getInstance();
        Context appContext = InstrumentationRegistry.getTargetContext();
        final String cfgName = "testAllCfg";

        String testAllCfg = SystemSetting.getCfg(appContext, cfgName);
        LogToSystem.t(TAG + "testAllCfg1", testAllCfg);

        boolean result = SystemSetting.delCfg(appContext, cfgName);
        Assert.assertTrue(result);

        testAllCfg = SystemSetting.getCfg(appContext, cfgName);
        Assert.assertSame("", testAllCfg);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        SystemSetting.saveCfg(appContext, cfgName, str);
        LogToSystem.t(TAG + "testAllCfg2", str);
        testAllCfg = SystemSetting.getCfg(appContext, cfgName);
        Assert.assertSame(str, testAllCfg);
    }

    @Test
    public void saveCfg() throws Exception {

    }

    @Test
    public void getCfg() throws Exception {

    }

    @Test
    public void delCfg() throws Exception {

    }

}