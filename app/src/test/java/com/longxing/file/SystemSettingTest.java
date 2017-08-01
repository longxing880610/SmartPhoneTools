package com.longxing.file;

import com.longxing.log.LogToSystem;
import com.longxing.ui.MainActivity;

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
        MainActivity mainActivity = MainActivity.getInstance();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        SystemSetting.SaveCfg(mainActivity, "testAllCfg", str);
        LogToSystem.t(TAG + "testAllCfg", str);
        String testAllCfg = SystemSetting.GetCfg(mainActivity, "testAllCfg");
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