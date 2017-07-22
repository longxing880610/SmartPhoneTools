package com.longxing.ui;
//Download by http://ww.codefans.net

import java.io.File;
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
import com.longxing.file.FileStruct;

/* 自定义的Adapter，继承android.widget.BaseAdapter */
public class ListItemAdapter extends BaseAdapter {

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
    private FileStruct mRootDir;

    /* MyAdapter的构造器，传入三个参数  */
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
    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
      /* 使用自定义的file_row作为Layout */
            convertView = mInflater.inflate(R.layout.file_row, null);
      /* 初始化holder的text与icon */
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FileStruct fileStruct = items.get(position);
        if(items.get(position).equals(mRootDir))
        {
            holder.text.setText("Back to root directory");
            holder.icon.setImageBitmap(mIcon1);
        }
        else {
            holder.text.setText(fileStruct.mFileName);
            if (!fileStruct.mIsFileOrFalseDir) {
                holder.icon.setImageBitmap(mIcon3);
            } else {
                holder.icon.setImageBitmap(mIcon4);
            }
        }
        return convertView;
    }

    public void addAll(List<FileStruct> alllfiles) {
        if (alllfiles != items) {
            items.clear();
            items.addAll(alllfiles);
        }
        if (!alllfiles.get(0).equals(mRootDir)) {
            items.add(0, mRootDir);
        }
        notifyDataSetChanged();
    }

    /* class ViewHolder */
    private class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
