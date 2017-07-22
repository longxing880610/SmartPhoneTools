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
    public long mSize = 0;
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
        if (mIsFileOrFalseDir){
            mSize = file.length();
        }
        else
        {
            mSize = 0;
        }
    }

    private FileStruct(){
    }

    public @Override String toString(){
        return mFileName;
    }


    public @Override FileStruct clone(){
        FileStruct retValue = new FileStruct();
        retValue.mFilePath = mFilePath;
        retValue.mSize = mSize;
        retValue.mIsFileOrFalseDir = mIsFileOrFalseDir;
        retValue.mFileName = mFileName;
        retValue.mFileDir = mFileDir;
        retValue.mIsHide = mIsHide;
        return  retValue;
    }
}
