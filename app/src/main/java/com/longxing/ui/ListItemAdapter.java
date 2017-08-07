package com.longxing.ui;
//Download by http://ww.codefans.net

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.longxing.R;
import com.longxing.common.MyException;
import com.longxing.common.ThreadStatus_ListFiles;
import com.longxing.file.FileManage;
import com.longxing.file.FileStruct;
import com.longxing.log.LogToSystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* 自定义的Adapter，继承android.widget.BaseAdapter */
public class ListItemAdapter extends BaseAdapter implements Runnable {

    private static final String TAG = "MyLog/ListItemAdapter/";
    public static final String cSEARCH_CONDITION[] = {"名称", "大小"};
    private String mCurSearchCondition = "名称";
    /**
     * 回到根目录与回到上级目录是前两个元素
     */
    private static final int LENGTH_SPECIAL_DIRECOTRY = 1;
    /* 变量声明
     mIcon1：回到根目录的图文件
     mIcon2：回到上一层的图档
     mIcon3：文件夹的图文件
     mIcon4：文件的图档
    */
    private LayoutInflater mInflater;
    private Bitmap mIcon1;
    private Bitmap mIcon2;
    private Bitmap mIcon3;
    private Bitmap mIcon4;
    private List<FileStruct> items;
    private ArrayList<FileStruct> rootItems = new ArrayList<>();
    private FileStruct mRootDir;

    private volatile ThreadStatus_ListFiles mThreadStatus = new ThreadStatus_ListFiles();
    private Thread thread = null;
    /**
     * 是否扫描大文件夹
     */
    private volatile boolean mIsScanAllSize = false;

    /* MyAdapter的构造器，传入三个参数 */
    public ListItemAdapter(Context context, List<FileStruct> it, FileStruct rootDir) {
        /* 参数初始化 */
        mInflater = LayoutInflater.from(context);
        items = it;
        mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.back01);
        mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.back02);
        mIcon3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.folder);
        mIcon4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.doc);
        rootDir.mIsRoot = true;
        mRootDir = rootDir;
    }

    /* 因继承BaseAdapter，需覆盖以下方法 */
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            /* 使用自定义的file_row作为Layout */
            convertView = mInflater.inflate(R.layout.file_row, null);
            /* 初始化holder的text与icon */
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            holder.count = (TextView) convertView.findViewById(R.id.count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FileStruct fileStruct = items.get(position);

        String showSize = "";
        String name = null;
        Bitmap bitmap = null;
        StringBuilder sizeStr = new StringBuilder();
        // size
        String[] unit = {"B", "K", "M", "G"};
        double size = fileStruct.mSize;
        if (size >= 0) {
            int index = 1;
            while (size >= 1024 && index < unit.length) {
                size /= 1024;
                ++index;
            }
            --index;
            showSize += String.format("%.2f", size);
            if (showSize.endsWith("00")) {
                showSize = showSize.substring(0, showSize.length() - 3);
            } else if (showSize.endsWith("0")) {
                showSize = showSize.substring(0, showSize.length() - 1);
            }
            sizeStr.append(showSize);
            sizeStr.append(unit[index]);
        } else {
            sizeStr.append("等待扫描");
        }
        if (fileStruct.mIsRoot) {
            name = "Back to Root";
            bitmap = mIcon1;
            sizeStr.insert(0, "空闲:");
        } else if (fileStruct.mIsParent) {
            name = "Back to Parent";
            bitmap = mIcon2;
            sizeStr.setLength(0);
            sizeStr.append(fileStruct.mFileName);
        } else {
            name = fileStruct.mFileName;
            if (!fileStruct.mIsFileOrFalseDir) {
                bitmap = mIcon3;
            } else {
                bitmap = mIcon4;
            }
        }
        int count = fileStruct.mFileCount;
        String countStr = null;
        if (count >= 0) {
            countStr = count + "个";
        } else if (count == FileStruct.cCountFile) {
            countStr = "";
        } else {
            countStr = "等待扫描";
        }

        holder.text.setText(name);
        holder.icon.setImageBitmap(bitmap);
        holder.size.setText(sizeStr.toString());
        holder.count.setText(countStr);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        notifyDataSetChanged(true);
    }

    public void notifyDataSetChanged(boolean needSize) {
        super.notifyDataSetChanged();

        if (needSize) {
            // LogToSystem.d(TAG + "notifyDataSetChanged", "notifyDataSetChanged Cal Size");
            if (thread == null) {
                thread = new Thread(this);
            }
            if (!thread.isAlive()) {
                mThreadStatus.isAlive = true;
                thread.start();
            } else {
                mThreadStatus.isRestart = true;
            }
        }
    }

    public void EnableScanAllSize(boolean isEnable) {
        mIsScanAllSize = isEnable;
        notifyDataSetChanged();
    }

    public void addAll(List<FileStruct> allFiles) {
        if (allFiles != null) {
            if (allFiles != items) {
                items.clear();
                items.addAll(allFiles);
            }
        }

        if (allFiles == null || allFiles.size() <= 0 || !allFiles.get(0).equals(mRootDir)) {
            try {
                if (!allFiles.get(0).mFileDir.equals(mRootDir.mFilePath)) {
                    FileStruct tmpFile = new FileStruct(new File(allFiles.get(0).mFileDir).getParentFile());
                    tmpFile.mIsParent = true;
                    tmpFile.mSize = 0;
                    tmpFile.mFileCount = FileStruct.cCountFile;
                    items.add(0, tmpFile);
                }
            } catch (Exception ex) {

            }
            items.add(0, mRootDir);
        }

        notifyDataSetChanged();
    }

    public void sortByUser(String type) {
        final String name = "名称";//cSEARCH_CONDITION[0];
        final String size = "大小";//cSEARCH_CONDITION[1];
        if (mCurSearchCondition.equals(type)) {
            return;
        }
        mCurSearchCondition = type;
        switch (type) {
            case name:
                Collections.sort(items, new Comparator<FileStruct>() {
                    @Override
                    public int compare(FileStruct o1, FileStruct o2) {
                        return o1.mFileName.compareToIgnoreCase(o2.mFileName);
                    }
                });
                break;
            case size:
                Collections.sort(items, new Comparator<FileStruct>() {
                    @Override
                    public int compare(FileStruct o1, FileStruct o2) {

                        long tmpL = o2.mSize - o1.mSize;
                        if (tmpL > 0 || o2.mIsRoot || o2.mIsParent) {
                            return 1;
                        }
                        if (tmpL < 0) {
                            return -1;
                        }
                        return 0;
                    }
                });
                break;
            default:
                return;
        }
        notifyDataSetChanged(false);
    }

    /**
     *
     */
    @Override
    public void run() {
        boolean isOver = false;
        int firstIndex = LENGTH_SPECIAL_DIRECOTRY;

        List<FileStruct> tmpFiles = rootItems;
        UI_TabLog uiTabLog = UI_TabLog.getInstance();

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        uiTabLog.displayLog(date + ":start run:" + items.size());

        LogToSystem.d(TAG + "run", "start run:" + isOver);
        while (!isOver) {
            try {
                FileStruct tmpFileStruct = null;

                for (int i = firstIndex; i < items.size(); ++i) {
                    FileStruct item = items.get(i);
                    if (item.mIsParent) {
                        continue;
                    }
                    if (!item.mIsFileOrFalseDir) {
                        // only directory
                        //List<FileStruct> tmpFiles = rootItems;
                        //if (isRootDir) {
                        //List<FileStruct> tmpFiles = rootItems;
                        boolean isRestored = false;
                        tmpFileStruct = null;
                        for (FileStruct item1 : tmpFiles) {
                            if (item1.mFilePath.equals(item.mFilePath)) {
                                tmpFileStruct = item1;
                                if (item1.mSearchInSecond) {
                                    isRestored = true;
                                }
                                if (item1.mIsSizeCaled) {
                                    item.mFileCount = item1.mFileCount;
                                    item.mSize = item1.mSize;
                                    item.mIsSizeCaled = true;
                                    isRestored = true;
                                }
                                //LogToSystem.d(TAG + "run", "恢复大小的属性"+item.mFileName);
                                break;
                            }
                        }
                        if (isRestored) {
                            continue;
                        }
                        //}
                        try {
                            final int fileTryCount = 200;
                            mThreadStatus.mFileTryCount = fileTryCount;
                            long fileSize = FileManage.getFolderSize(new File(item.mFilePath), mThreadStatus);

                            if (mThreadStatus.isRestart) {
                                mThreadStatus.isRestart = false;
                                throw new MyException("计算目录大小的线程重启");
                            }
                            item.mIsSizeCaled = true;
                            item.mSize = fileSize;
                            item.mFileCount = fileTryCount - mThreadStatus.mFileTryCount;
                            item.mSearchInSecond = false;
                            //if (isRootDir) {
                        } catch (MyException ex) {
                            item.mSearchInSecond = true;
                            //LogToSystem.i(TAG + "run", item.mFileName + "-" + ex.getMessage());
                        } finally {
                            if (tmpFileStruct == null) {
                                tmpFileStruct = item.clone();
                                tmpFiles.add(tmpFileStruct);
                            } else {
                                tmpFileStruct.setByFileStruct(item);
                            }
                        }
                    }
                }

                if (mIsScanAllSize) {

                    UI_TabSdFiles.getInstance().sendMessage(UI_TabSdFiles.WHAT_UPDATE_FILELIST_FORCE, null);
                    //////////////////////////////////////////////////////////////////////////////////

                    LogToSystem.i(TAG + "run", "search goon");
                    for (int i = firstIndex; i < items.size(); ++i) {
                        FileStruct item = items.get(i);
                        if (item.mIsSizeCaled) {
                            // 已经有长度的不需要两次计算长度
                            continue;
                        }
                        if (!item.mIsFileOrFalseDir) {
                            tmpFileStruct = null;
                            for (FileStruct item1 : tmpFiles) {
                                if (item1.mFilePath.equals(item.mFilePath)) {
                                    tmpFileStruct = item1;
                                    break;
                                }
                            }

                            LogToSystem.d(TAG + "run", "large directory:" + item);
                            final int fileTryCount = ThreadStatus_ListFiles.DEPTH_INFEINE;
                            mThreadStatus.mFileTryCount = fileTryCount;
                            item.mSize = FileManage.getFolderSize(new File(item.mFilePath), mThreadStatus);
                            item.mFileCount = fileTryCount - mThreadStatus.mFileTryCount;
                            item.mIsSizeCaled = true;
                            item.mSearchInSecond = false;

                            if (tmpFileStruct == null) {
                                tmpFileStruct = item.clone();
                                tmpFiles.add(tmpFileStruct);
                            } else {
                                tmpFileStruct.setByFileStruct(item);
                            }
                        }
                        if (mThreadStatus.isRestart) {
                            mThreadStatus.isRestart = false;
                            throw new MyException("计算目录大小的线程重启");
                        }
                    }
                }
                isOver = true;
            } catch (Exception ex) {
                LogToSystem.e(TAG + "run", ex.getMessage());
            }
        }

        LogToSystem.d(TAG + "run", "end run:" + tmpFiles.size());
        date = sDateFormat.format(new java.util.Date());
        uiTabLog.displayLog(date + ":end run:" + tmpFiles.size());

        UI_TabSdFiles.getInstance().sendMessage(UI_TabSdFiles.WHAT_UPDATE_FILELIST_FORCE, null);
        //notifyDataSetChanged(false);
    }

    /* class ViewHolder */
    private class ViewHolder {
        TextView text;
        ImageView icon;
        TextView size;
        TextView count;
    }
}
