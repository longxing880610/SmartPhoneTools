package com.longxing.ui;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.longxing.R;
import com.longxing.common.CastWarn;
import com.longxing.file.FileManage;
import com.longxing.file.FileStruct;
import com.longxing.log.LogToFile;

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
    private static final String TAG = "MyLog/UI_TabSdFiles/";

    private static UI_TabSdFiles sUiTabSdFiles;

    private TextView mTextViewPath;
    private EditText mSearchText;
    /**
     * main ui interface
     */
    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabLog;

    private static ArrayList<FileStruct> mFileNames = new ArrayList<>();
    private static ArrayList<FileStruct> mFileNameBackup = null;
    private List<String> mFileDir = new ArrayList<>();
    private int mCurFileDirIndex = 0;
    private ArrayAdapter<FileStruct> mAdapter = null;

    private boolean mIsShow_hidefile = false;

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

        // file path
        mTextViewPath = (TextView) rootView.findViewById(R.id.textview_path);
        // search text
        mSearchText = (EditText) rootView.findViewById(R.id.editText_search);
        mSearchText.addTextChangedListener(new TextWatcher() {
            private int mLengthBefore = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // LogToSystem.d(TAG + "beforeTextChanged", s + "&" + start + "&" + count + "&" + after);
                mLengthBefore = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogToSystem.d(TAG + "onTextChanged", s + "&" + start + "&" + count + "&" + count);

            }

            @Override
            public void afterTextChanged(Editable s) {
                // ArrayList<FileStruct> fileStructs;
                String keyword = s.toString().toLowerCase();
                //LogToSystem.d(TAG + "afterTextChanged", keyword);
                if (keyword.isEmpty()) {
                    //switchDir(mFileDir.get(mCurFileDirIndex), false);
                    if (mFileNameBackup != null) {
                        mFileNames.clear();
                        mFileNames.addAll(mFileNameBackup);
                        mAdapter.notifyDataSetChanged();
                    }
                    // may be the forward or backward in the file manager
                    // LogToSystem.d(TAG+"afterTextChanged", "keyword.isEmpty");
                } else {
                    if (mFileNameBackup == null) {
                        mFileNameBackup = CastWarn.cast(mFileNames.clone());
                    } else if (mLengthBefore > keyword.length()) {
                        // length of keyword is decrease
                        //LogToSystem.d(TAG + "afterTextChanged", "length of keyword is decrease");
                        mFileNames.clear();
                        mFileNames.addAll(mFileNameBackup);
                    }
                    //List<FileStruct> tmpFiles = fileStructs.subList(0, fileStructs.size());
                    mFileNames.removeIf(fileStruct -> !fileStruct.mFileName.toLowerCase().contains(keyword));
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        //fileListView.

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        mAdapter = new ArrayAdapter<>(mMainActivity, simple_expandable_list_item_1, mFileNames);
        fileListView.setAdapter(mAdapter);

        fileListView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    FileStruct filedirStruct = mFileNames.get(position);
                    Toast.makeText(mMainActivity, filedirStruct.toString(), Toast.LENGTH_SHORT).show();

                    if (filedirStruct.mIsFileOrFalseDir) {
                        FileManage.openFile(mMainActivity, filedirStruct.mFilePath);
                    } else {
                        //mCurFileDirIndex = position;
                        switchDir(filedirStruct.mFilePath);

                    }
                });

        switchDir(filePath);

        //final ScrollView scrollViewLog = (ScrollView) rootView.findViewById(R.id.ScrollLog);

        // back button
        Button btnBack = (Button) rootView.findViewById(R.id.button_back);
        btnBack.setOnClickListener(v -> {
            //LogToSystem.d(TAG, "跳转前一目录");
            // 返回键
            if (mCurFileDirIndex > 0) {
                --mCurFileDirIndex;

                switchDir(mFileDir.get(mCurFileDirIndex), false);

            }
        });

        // forward button
        Button btnForward = (Button) rootView.findViewById(R.id.button_forward);
        btnForward.setOnClickListener(v -> {
            // LogToSystem.d(TAG, "跳转前一目录");
            // 返回键
            if (mCurFileDirIndex < mFileDir.size() - 1) {
                ++mCurFileDirIndex;

                switchDir(mFileDir.get(mCurFileDirIndex), false);

            }
        });

        // show or hide the hide file
        CheckBox showHideFile = (CheckBox) rootView.findViewById(R.id.checkBox_showHideFile);
        showHideFile.setOnClickListener(v -> {
            CheckBox tmpCheckbox = (CheckBox) v;
            mIsShow_hidefile = tmpCheckbox.isChecked();

            //mFileNames = switchDir(mFileDir.get(mCurFileDirIndex), false);
            List<FileStruct> alllfiles = mFileNames;

            if (mIsShow_hidefile) {
                switchDir(mFileDir.get(mCurFileDirIndex), false);
                //mAdapter.addAll(alllfiles);
            } else {
                //List<FileStruct> tmpFiles = alllfiles.subList(0, alllfiles.size());
                alllfiles.removeIf(fileStruct -> fileStruct.mIsHide);
                mAdapter.addAll(alllfiles);
            }
        });


        displayLog("SD文件管理加载完成");
    }

    @Override
    public boolean processKeyDown(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogToSystem.d(TAG, "返回上一目录");
            // 返回键
            if (mCurFileDirIndex > 0) {
                --mCurFileDirIndex;

                mFileNames = switchDir(mFileDir.get(mCurFileDirIndex), false);

                mAdapter.clear();
                mAdapter.addAll(mFileNames);
            }
            return true;
        }*/
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
    private boolean switchDir(String dir) {
        return switchDir(dir, true);
    }

    /**
     * switch directory
     *
     * @param dir directory
     */
    private boolean switchDir(String dir, boolean isAdd) {
        return switchDir(dir, isAdd, mIsShow_hidefile);
    }

    /**
     * switch directory
     *
     * @param dir directory
     */
    private boolean switchDir(String dir, boolean isAdd, boolean isShow_Hidefile) {

        mTextViewPath.setText(dir);
        //mCurFileDirIndex = 0;
        if (isAdd) {
            int index = mCurFileDirIndex + 1;
            if (index >= mFileDir.size()) {
                mFileDir.add(dir);
                mCurFileDirIndex = mFileDir.size() - 1;
            } else {
                mFileDir.set(index, dir);
                for (int i=mFileDir.size()-1; i>index; --i) {
                    mFileDir.remove(i);
                }
                mCurFileDirIndex = index;
            }
            //mCurFileDirIndex = mFileDir.size() - 1;
        }

        FileManage.GetFiles(dir, isShow_Hidefile, mFileNames);

        mAdapter.notifyDataSetChanged();
        mFileNameBackup = null;
        mSearchText.setText("");
        return true;
    }
}
