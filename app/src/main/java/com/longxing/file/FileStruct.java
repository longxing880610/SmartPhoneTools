package com.longxing.file;

import java.io.File;
import java.util.Comparator;

/**
 * File struct
 */
public class FileStruct{

    public static final int cCounting = -2;
    public static final int cCountFile = -1;

    public String mFileName;
    public String mFilePath;
    public String mFileDir;
    public boolean mIsHide;
    public long mSize = -1;
    /**
     * file when true, directory when false
     */
    public boolean mIsFileOrFalseDir = false;
    public boolean mIsRoot = false;
    public boolean mIsParent = false;
    public boolean mIsSizeCaled = false;
    public boolean mSearchInSecond = false;
    public int mFileCount = cCounting;

    public FileStruct(File file){
        mFileName = file.getName();
        mFilePath = file.getPath();
        mFileDir = file.getParent();
        mIsFileOrFalseDir = file.isFile();
        mIsHide = file.isHidden();
        if (mIsFileOrFalseDir){
            mSize = file.length();
            mFileCount = cCountFile;
        }
        else
        {
            mSize = -1;
        }
        //mIsParent = false;
    }

    private FileStruct(){
    }

    public @Override String toString(){
        return mFileName;
    }

    /**
     * clone the filestruct
     * @return
     */
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
        retValue.mFileCount = mFileCount;
        return  retValue;
    }

    /**
     * set by filestruct
     * @param fileStruct
     */
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
        mFileCount = fileStruct.mFileCount;
    }
}
