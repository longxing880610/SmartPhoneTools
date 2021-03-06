package com.longxing.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.longxing.log.LogToSystem;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Zhang Long on 2017/7/13.
 * <p>
 * File manage
 */
public class FileManage {
    private static String TAG = "MyLog/FileManage/";

    /**
     * 获取文件及目录列表
     *
     * @param filePath        current directory
     * @param isShow_Hidefile is show the file(hide)
     * @param fileDirList list to store the file and directory
     * @return
     */
    public static List<FileStruct> GetFiles(String filePath, boolean isShow_Hidefile, List<FileStruct> fileDirList) {

        fileDirList.clear();
        File file = new File(filePath);
        File[] files = file.listFiles((dir, name) -> {
            return true;//name.startsWith("a");
        });

        for (File item : files) {
            if (!isShow_Hidefile) {
                if (item.isHidden()) {
                    //LogToSystem.d(TAG+GetFiles, item.getName());
                    continue;
                }
            }
            FileStruct fileStruct = new FileStruct(item);
            fileDirList.add(fileStruct);
        }
        Collections.sort(fileDirList, (o1, o2) -> o1.mFileName.toLowerCase().compareTo(o2.mFileName.toLowerCase()));

        return fileDirList;
    }

    /**
     * @param context  context of activity
     * @param filePath filepath of open file
     */
    public static void openFile(Context context, String filePath) {
        File file = new File(filePath);
        //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
        Uri uri = FileProvider.getUriForFile(context, "com.longxing.fileprovider", file);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(uri, type);   // /*uri*/Uri.fromFile(file)
        //跳转
        try {
            context.startActivity(intent);
        } catch (Exception ex) {
            LogToSystem.e(TAG, ex.getMessage());
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file file input
     */
    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
//获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
/* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end.equals("")) return type;
//在MIME和文件类型的匹配表中找到对应的MIME类型。
        //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
        for (String[] aMIME_MapTable : MIME_MapTable) {
            if (end.equals(aMIME_MapTable[0]))
                type = aMIME_MapTable[1];
        }
        return type;
    }

    //建立一个MIME类型与文件后缀名的匹配表
    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
}
