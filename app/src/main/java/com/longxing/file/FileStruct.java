package com.longxing.file;

import java.io.File;

/**
 * File struct
 */
public class FileStruct {

    public static final int cCounting = -2;
    public static final int cCountFile = -1;

    public String mFileNameKey;
    public String mFileName;
    public String mFilePath;
    public boolean mIsHide;
    public long mSize = -1;
    public String mCurdirIn;
    /**
     * file when true, directory when false
     */
    public boolean mIsFileOrFalseDir = false;
    public boolean mIsRoot = false;
    public boolean mIsParent = false;
    public boolean mIsSizeCaled = false;
    public boolean mSearchInSecond = false;
    public int mFileCount = cCounting;
    private String mFileDir;

    public FileStruct(File file) {
        mFileName = file.getName();
        mFileNameKey = mFileName.toLowerCase();
        mFilePath = file.getPath();
        mFileDir = file.getParent();
        mIsFileOrFalseDir = file.isFile();
        mIsHide = file.isHidden();
        if (mIsFileOrFalseDir) {
            mSize = file.length();
            mFileCount = cCountFile;
        } else {
            mSize = -1;
        }
        mCurdirIn = mFileDir;
        //mIsParent = false;
    }

    private FileStruct() {
    }

    public
    @Override
    String toString() {
        return mFileName;
    }

    /**
     * clone the filestruct
     *
     * @return
     */
    public
    @Override
    FileStruct clone() {
        FileStruct retValue = new FileStruct();
        retValue.mFilePath = mFilePath;
        retValue.mSize = mSize;
        retValue.mIsFileOrFalseDir = mIsFileOrFalseDir;
        retValue.mFileName = mFileName;
        retValue.mFileNameKey = mFileNameKey;
        retValue.mFileDir = getmFileDir();
        retValue.mIsHide = mIsHide;
        retValue.mIsParent = mIsParent;
        retValue.mIsSizeCaled = mIsSizeCaled;
        retValue.mSearchInSecond = mSearchInSecond;
        retValue.mFileCount = mFileCount;
        retValue.mCurdirIn = mCurdirIn;
        return retValue;
    }

    /**
     * set by filestruct
     *
     * @param fileStruct
     */
    public void setByFileStruct(FileStruct fileStruct) {
        mFilePath = fileStruct.mFilePath;
        mSize = fileStruct.mSize;
        mIsFileOrFalseDir = fileStruct.mIsFileOrFalseDir;
        mFileName = fileStruct.mFileName;
        mFileNameKey = fileStruct.mFileNameKey;
        mFileDir = fileStruct.getmFileDir();
        mIsHide = fileStruct.mIsHide;
        mIsParent = fileStruct.mIsParent;
        mIsSizeCaled = fileStruct.mIsSizeCaled;
        mSearchInSecond = fileStruct.mSearchInSecond;
        mFileCount = fileStruct.mFileCount;
        mCurdirIn = fileStruct.mCurdirIn;
    }

    public String getmFileDir() {
        return mIsParent ? mFilePath : mFileDir;
    }
}
