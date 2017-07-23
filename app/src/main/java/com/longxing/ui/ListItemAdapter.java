package com.longxing.ui;
//Download by http://ww.codefans.net

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.longxing.common.ThreadStatus;
import com.longxing.file.FileManage;
import com.longxing.file.FileStruct;
import com.longxing.log.LogToSystem;

/* 自定义的Adapter，继承android.widget.BaseAdapter */
public class ListItemAdapter extends BaseAdapter implements Runnable {

    private static final String TAG = "MyLog/ListItemAdapter/";
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

    private volatile ThreadStatus mThreadStatus = new ThreadStatus();
    private Thread thread = null;

    /* MyAdapter的构造器，传入三个参数 */
    public ListItemAdapter(Context context, List<FileStruct> it, FileStruct rootDir) {
        /* 参数初始化 */
        mInflater = LayoutInflater.from(context);
        items = it;
        mIcon1 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.back01);
        mIcon2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.back02);
        mIcon3 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder);
        mIcon4 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.doc);
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
        if (items.get(position).equals(mRootDir)) {
            holder.text.setText("Back to root directory");
            holder.icon.setImageBitmap(mIcon1);

            showSize += "空闲:";
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
                items.add(0, new FileStruct(new File(allFiles.get(0).mFilePath)));
            }catch (Exception ex){

            }
            items.add(0, mRootDir);
        }
        notifyDataSetChanged();
    }

    @Override
    public void run() {
        boolean isOver = false;
        int firstIndex = 1;

        LogToSystem.d(TAG + "run", "start run:" + isOver);
        do {
            try {

                boolean isRootDir = false;
                if (items.size() > 1) {
                    isRootDir = items.get(firstIndex).mFileDir.equals(mRootDir.mFilePath);
                }
                for (int i = firstIndex; i < items.size(); ++i) {
                    FileStruct item = items.get(i);
                    if (!item.mIsFileOrFalseDir) {
                        // only directory
                        List<FileStruct> tmpFiles = rootItems;
                        if (isRootDir) {
                            //List<FileStruct> tmpFiles = rootItems;
                            boolean isRestored = false;
                            for (FileStruct item1 : tmpFiles) {
                                if (item1.mFilePath.equals(item.mFilePath)) {
                                    item.mSize = item1.mSize;
                                    isRestored = true;
                                    //LogToSystem.d(TAG + "run", "恢复大小的属性"+item.mFileName);
                                    break;
                                }
                            }
                            if (isRestored) {
                                continue;
                            }
                        }

                        item.mSize = FileManage.getFolderSize(new File(item.mFilePath), mThreadStatus);

                        if (isRootDir) {
                            tmpFiles.add(item.clone());
                            //LogToSystem.d(TAG + "run", "存储大小的属性:"+item.mFileName);
                        }

                        //LogToSystem.d(TAG + "run", item.mFileName + "&" + item.mSize);
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
        } while (!isOver);

        LogToSystem.d(TAG + "run", "end run");

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
