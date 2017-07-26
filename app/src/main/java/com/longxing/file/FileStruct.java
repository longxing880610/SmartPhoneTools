package com.longxing.file;

import java.io.File;
import java.util.Comparator;

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
    public boolean mIsParent = false;
    public boolean mIsSizeCaled = false;
    public boolean mSearchInSecond = false;

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
        mIsParent = false;
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
        retValue.mIsParent = mIsParent;
        retValue.mIsSizeCaled = mIsSizeCaled;
        retValue.mSearchInSecond = mSearchInSecond;
        return  retValue;
    }

    public void setByFileStruct(FileStruct fileStruct) {
        mFilePath = fileStruct.mFilePath;
        mSize = fileStruct.mSize;
        mIsFileOrFalseDir = fileStruct.mIsFileOrFalseDir;
        mFileName = fileStruct.mFileName;
        mFileDir = fileStruct.mFileDir;
        mIsHide = fileStruct.mIsHide;
        mIsParent = fileStruct.mIsParent;
        mIsSizeCaled = fileStruct.mIsSizeCaled;
        mSearchInSecond = fileStruct.mSearchInSecond;
    }
}
