package com.longxing.ui;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.longxing.R;
import com.longxing.common.CastWarn;
import com.longxing.file.FileManage;
import com.longxing.file.FileStruct;
import com.longxing.log.LogToSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    private static final String cKEY_TXT = "KEY_TXT";
    //private static final String cCount_Down_Latch = "Count_Down_Latch";

    private static final int WHAT_SHOW_PATH = 1;
    private static final int WHAT_GET_SEARCH_TEXT = 2;
    private static final int WHAT_SET_SEARCH_TEXT = 3;
    public static final int WHAT_UPDATE_FILELIST_FORCE = 4;

    private static UI_TabSdFiles sUiTabSdFiles;

    private CountDownLatch countDownLatch;
    private String mOutputString;
    private Handler mEditViewHandle;

    //private TextView mTextViewPath;
    //private EditText mSearchText;
    /**
     * main ui interface
     */
    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabLog;

    private static ArrayList<FileStruct> mFileNames = new ArrayList<>();
    private static ArrayList<FileStruct> mFileNameBackup = null;
    private List<String> mFileDir = new ArrayList<>();
    private int mCurFileDirIndex = 0;
    private ListItemAdapter mAdapter = null;

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
        // need to be initialized at first
        // file path
        TextView mTextViewPath = (TextView) rootView.findViewById(R.id.textview_path);
        // search text
        EditText mSearchText = (EditText) rootView.findViewById(R.id.editText_search);
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
                        mAdapter.addAll(mFileNameBackup);
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
                    mAdapter.addAll(mFileNames);
                }
            }
        });

        mEditViewHandle = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == WHAT_SHOW_PATH) {
                    Bundle bundle = msg.getData();
                    mTextViewPath.setText(bundle.getString(cKEY_TXT));
                } else if (msg.what == WHAT_GET_SEARCH_TEXT) {
                    //Bundle bundle = msg.getData();
                    mOutputString = mSearchText.getText().toString();
                    //CountDownLatch countDownLatch = (CountDownLatch)bundle.get(cCount_Down_Latch);
                    countDownLatch.countDown();
                } else if (msg.what == WHAT_SET_SEARCH_TEXT) {
                    Bundle bundle = msg.getData();
                    mSearchText.setText(bundle.getString(cKEY_TXT));
                } else if (msg.what == WHAT_UPDATE_FILELIST_FORCE) {
                    mAdapter.notifyDataSetChanged(false);
                }
                super.handleMessage(msg);
            }
        };
        //fileListView.

        final String rootDir;
        rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        //mAdapter = new ArrayAdapter<>(mMainActivity, simple_expandable_list_item_1, mFileNames);
        //fileListView.setAdapter(mAdapter);
        File file = new File(rootDir);
        FileStruct rootStruct = new FileStruct(file);
        rootStruct.mSize = file.getFreeSpace();
        //rootStruct.mFileName =
        mAdapter = new ListItemAdapter(mMainActivity, mFileNames, rootStruct);

        fileListView.setAdapter(mAdapter);

        fileListView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    FileStruct filedirStruct = mFileNames.get(position);
                    //Toast.makeText(mMainActivity, filedirStruct.toString(), Toast.LENGTH_SHORT).show();

                    if (filedirStruct.mIsFileOrFalseDir) {
                        FileManage.openFile(mMainActivity, filedirStruct.mFilePath);
                    } else {
                        //mCurFileDirIndex = position;
                        switchDir(filedirStruct.mFilePath);
                    }
                });

        //LogToSystem.d(TAG+"initUI","initUI");
        switchDir(rootDir);

        //region button event process
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

        Spinner searchCondition = (Spinner) rootView.findViewById(R.id.spinner_search);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(mMainActivity, R.array.searchCondition,
                        android.R.layout.simple_spinner_dropdown_item);
        searchCondition.setAdapter(adapter);
        //searchCondition.setOnItemClickListener(new SearchConditionOnClickList());

        // show or hide the hide file
        CheckBox showHideFile = (CheckBox) rootView.findViewById(R.id.checkBox_showHideFile);
        showHideFile.setOnClickListener(v -> {
            CheckBox tmpCheckbox = (CheckBox) v;
            mIsShow_hidefile = tmpCheckbox.isChecked();

            //mFileNames = switchDir(mFileDir.get(mCurFileDirIndex), false);
            List<FileStruct> allFiles = mFileNames;

            if (mIsShow_hidefile) {
                switchDir(mFileDir.get(mCurFileDirIndex), false);
                //mAdapter.addAll(allFiles);
            } else {
                //List<FileStruct> tmpFiles = allFiles.subList(0, allFiles.size());
                allFiles.removeIf(fileStruct -> fileStruct.mIsHide);
                mAdapter.addAll(allFiles);
            }
        });
        //endregion


        displayLog("SD文件管理加载完成");
    }

    //region 窗体事件响应函数

    /**
     * 排序筛选条件点击事件处理类
     */
    private class SearchConditionOnClickList implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner) view;
            LogToSystem.d(TAG + "SearchConditionOnClickList", spinner.getSelectedItem().toString());
        }
    }

    //endregion


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

    @Override
    public void processDestroy() {

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

        sendMessage(WHAT_SHOW_PATH, dir);
        //mTextViewPath.setText(dir);
        //mCurFileDirIndex = 0;
        while (isAdd) {
            int index = mCurFileDirIndex + 1;
            try {
                if (mFileDir.get(mCurFileDirIndex).equals(dir)) {
                    return true;
                }
            } catch (Exception ex) {

            }
            if (index >= mFileDir.size()) {
                mFileDir.add(dir);
                mCurFileDirIndex = mFileDir.size() - 1;
            } else {
                mFileDir.set(index, dir);
                for (int i = mFileDir.size() - 1; i > index; --i) {
                    mFileDir.remove(i);
                }
                mCurFileDirIndex = index;
            }
            //mCurFileDirIndex = mFileDir.size() - 1;
            break;
        }

        FileManage.GetFiles(dir, isShow_Hidefile, mFileNames);

        mAdapter.addAll(mFileNames);
        mFileNameBackup = null;
        sendMessage(WHAT_SET_SEARCH_TEXT, "");
        return true;
    }

    /**
     * @param what   what control
     * @param txtMsg txt message
     * @return result
     */
    public String sendMessage(int what, String txtMsg) {
        return sendMessage(what, txtMsg, false);
    }

    /**
     * @param what     what control
     * @param txtMsg   txt message
     * @param needWait need wait util result return
     * @return result
     */
    private String sendMessage(int what, String txtMsg, boolean needWait) {
        Bundle bundle = new Bundle();
        if (what == WHAT_SHOW_PATH) {
            bundle.putString(cKEY_TXT, txtMsg);
        } else if (what == WHAT_SET_SEARCH_TEXT) {
            bundle.putString(cKEY_TXT, txtMsg);
        }
        Message msg = new Message();
        msg.what = what;
        msg.setData(bundle);
        mEditViewHandle.sendMessage(msg);
        if (needWait) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await(5, TimeUnit.SECONDS);  //阻塞，直到countDown被执行1次，计数为0
            } catch (InterruptedException e) {
                LogToSystem.e(TAG + "sendMessage", e.getMessage());
            }
            return mOutputString;
        }
        return null;
    }

}

