package com.longxing.common;

/**
 * Created by Zhang Long on 2017/7/22.
 *
 * thread status
 */

public class ThreadStatus_ListFiles extends ThreadStatus{
    public static final int DEPTH_INFEINE = -1;
    /**
     * 深度列表的文件夹数量, 避免大文件夹影响体验
     */
    public volatile int mFileTryCount = -1;

}
