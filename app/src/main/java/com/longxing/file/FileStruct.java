package com.longxing.file;

import java.io.File;

/**
 * File struct
 */
public class FileStruct{
    public String mFileName;
    public String mFilePath;
    public String mFileDir;
    public boolean mIsHide;
    /**
     * file when true, directory when false
     */
    public boolean mIsFileOrFalseDir = false;

    public FileStruct(File file){
        mFileName = file.getName();
        mFilePath = file.getPath();
        mFileDir = file.getParent();
        mIsFileOrFalseDir = file.isFile();
        mIsHide = file.isHidden();
    }

    private FileStruct(){
    }

    public @Override String toString(){
        return mFileName;
    }
}
