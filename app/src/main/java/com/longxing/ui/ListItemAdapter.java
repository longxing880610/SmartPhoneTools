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
import java.util.ArrayList;
import java.util.List;

/* 自定义的Adapter，继承android.widget.BaseAdapter */
public class ListItemAdapter extends BaseAdapter implements Runnable {

    private static final String TAG = "MyLog/ListItemAdapter/";
    /**
     * 回到根目录与回到上级目录是前两个元素
     */
    private static final int LENGTH_SPECIAL_DIRECOTRY = 2;
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

    /* MyAdapter的构造器，传入三个参数 */
    public ListItemAdapter(Context context, List<FileStruct> it, FileStruct rootDir) {
        /* 参数初始化 */
        mInflater = LayoutInflater.from(context);
        items = it;
        mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.back01);
        mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.back02);
        mIcon3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.folder);
        mIcon4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.doc);
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FileStruct fileStruct = items.get(position);

        String showSize = "";
        if (fileStruct.equals(mRootDir)) {
            holder.text.setText("Back to root directory");
            holder.icon.setImageBitmap(mIcon1);

            showSize += "空闲:";
        } else if (fileStruct.mIsParent) {
            holder.text.setText(fileStruct.mFileName);
            holder.icon.setImageBitmap(mIcon2);
            // showSize += "空闲:";
        } else {
            holder.text.setText(fileStruct.mFileName);
            if (!fileStruct.mIsFileOrFalseDir) {
                holder.icon.setImageBitmap(mIcon3);
            } else {
                holder.icon.setImageBitmap(mIcon4);
            }

        }
        // size
        String[] unit = {"B", "K", "M", "G"};
        double size = fileStruct.mSize;
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
        holder.size.setText(showSize + unit[index]);

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
                    items.add(0, tmpFile);
                }
            } catch (Exception ex) {

            }
            items.add(0, mRootDir);
        }
        notifyDataSetChanged();
    }

    /**
     *
     */
    @Override
    public void run() {
        boolean isOver = true;
        int firstIndex = LENGTH_SPECIAL_DIRECOTRY;

        //LogToSystem.d(TAG + "run", "start run:" + isOver);
        List<FileStruct> tmpFiles = rootItems;
        while (!isOver) {
            try {
                FileStruct tmpFileStruct = null;

                for (int i = firstIndex; i < items.size(); ++i) {
                    FileStruct item = items.get(i);
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
                            mThreadStatus.mFileTryCount = 100;
                            item.mSize = FileManage.getFolderSize(new File(item.mFilePath), mThreadStatus);

                            if (mThreadStatus.isRestart) {
                                mThreadStatus.isRestart = false;
                                throw new MyException("计算目录大小的线程重启");
                            }
                            item.mIsSizeCaled = true;
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
                UI_TabSdFiles.getInstance().sendMessage(UI_TabSdFiles.WHAT_UPDATE_FILELIST_FORCE, null);
                //////////////////////////////////////////////////////////////////////////////////
                //LogToSystem.i(TAG + "run", "search goon");
                for (int i = firstIndex; i < items.size(); ++i) {
                    FileStruct item = items.get(i);
                    if (item.mIsSizeCaled){
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

                        //LogToSystem.d(TAG + "run", "large directory:" + item);

                        mThreadStatus.mFileTryCount = ThreadStatus_ListFiles.DEPTH_INFEINE;
                        item.mSize = FileManage.getFolderSize(new File(item.mFilePath), mThreadStatus);
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
                isOver = true;
            } catch (Exception ex) {
                LogToSystem.e(TAG + "run", ex.getMessage());
            }
        }

        LogToSystem.d(TAG + "run", "end run:" + tmpFiles.size());

        UI_TabSdFiles.getInstance().sendMessage(UI_TabSdFiles.WHAT_UPDATE_FILELIST_FORCE, null);
        //notifyDataSetChanged(false);
    }

    /* class ViewHolder */
    private class ViewHolder {
        TextView text;
        ImageView icon;
        TextView size;
    }
}
