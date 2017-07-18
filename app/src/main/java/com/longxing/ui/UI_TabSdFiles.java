package com.longxing.ui;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.longxing.R;
import com.longxing.file.FileManage;
import com.longxing.file.FileStruct;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_expandable_list_item_1;

/**
 * Created by Zhang Long on .
 * <p>
 * show sd files
 */

class UI_TabSdFiles implements IUI_TabMain {
    /**
     * tag for log
     */
    private static final String TAG = "MyLog/UI_TabSdFiles";

    private static UI_TabSdFiles sUiTabSdFiles;

    /**
     * main ui interface
     */
    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabLog;
    private List<FileStruct> mFileName;
    private List<String> mFileDir = new ArrayList<>();
    private int mCurFileDirIndex = 0;
    private ArrayAdapter<FileStruct> mAdapter = null;

    private UI_TabSdFiles() {

    }

    /**
     * @return owner object
     */
    static UI_TabSdFiles getInstance() {
        if (sUiTabSdFiles == null) {
            sUiTabSdFiles = new UI_TabSdFiles();
            sUiTabSdFiles.mMainActivity = MainActivity.GetInstance();
            sUiTabSdFiles.mUI_tabLog = UI_TabLog.getInstance();
        }
        return sUiTabSdFiles;
    }

    /**
     * init user interface
     *
     * @param rootView parent view
     */
    @Override
    public void initUI(View rootView) {

        ListView fileListView = (ListView) rootView.findViewById(R.id.sdFiles);

        //fileListView.

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        mFileName = switchDir(filePath);

        mAdapter = new ArrayAdapter<>(mMainActivity, simple_expandable_list_item_1, mFileName);
        fileListView.setAdapter(mAdapter);

        fileListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        FileStruct filedirStruct = mFileName.get(position);
                        Toast.makeText(mMainActivity, filedirStruct.toString(), Toast.LENGTH_SHORT).show();

                        if (filedirStruct.mIsFileOrFalseDir) {
                            FileManage.openFile(mMainActivity, filedirStruct.mFilePath);
                        } else {
                            //mCurFileDirIndex = position;
                            mFileName = switchDir(filedirStruct.mFilePath);

                            mAdapter.clear();
                            mAdapter.addAll(mFileName);
                        }
                    }
                });
        //final ScrollView scrollViewLog = (ScrollView) rootView.findViewById(R.id.ScrollLog);


        displayLog("SD文件管理加载完成");
    }

    @Override
    public boolean processKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "返回上一目录");
            // 返回键
            if (mCurFileDirIndex > 0) {
                --mCurFileDirIndex;

                mFileName = switchDir(mFileDir.get(mCurFileDirIndex), false);

                mAdapter.clear();
                mAdapter.addAll(mFileName);
            }
            return true;
        }
        return false;
    }

    /**
     * @param msg show message
     */
    private void displayLog(String msg) {
        if (mUI_tabLog != null) {
            mUI_tabLog.displayLog(msg);
        }
    }

    /**
     * switch directory
     *
     * @param dir directory
     */
    private List<FileStruct> switchDir(String dir) {
        return switchDir(dir, true);
    }

    /**
     * switch directory
     *
     * @param dir directory
     */
    private List<FileStruct> switchDir(String dir, boolean isAdd) {
        //mCurFileDirIndex = 0;
        if (isAdd) {
            mFileDir.add(dir);
            mCurFileDirIndex = mFileDir.size() - 1;
        }

        return FileManage.GetFiles(dir);
    }
}
