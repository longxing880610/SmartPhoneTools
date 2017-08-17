package com.longxing.ui;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.longxing.R;
import com.longxing.common.CastWarn;
import com.longxing.file.FileManage;
import com.longxing.file.FileStruct;
import com.longxing.log.LogToSystem;
import com.longxing.ui.Control.ListItemAdapter;
import com.longxing.ui.Control.PopupWindowHelper;

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

public class UI_TabSdFiles implements IUI_TabMain {
    public static final int WHAT_UPDATE_FILELIST_FORCE = 4;
    public static final int WHAT_SET_SORT = 5;
    //private static final String cCount_Down_Latch = "Count_Down_Latch";
    /**
     * tag for log
     */
    private static final String TAG = "MyLog/UI_TabSdFiles/";
    private static final String cKEY_TXT = "KEY_TXT";
    private static final int WHAT_SHOW_PATH = 1;
    private static final int WHAT_GET_SEARCH_TEXT = 2;
    private static final int WHAT_SET_SEARCH_TEXT = 3;
    private static UI_TabSdFiles sUiTabSdFiles;
    private static ArrayList<FileStruct> mFileNames = new ArrayList<>();
    private static ArrayList<FileStruct> mFileNameBackup = null;
    private CountDownLatch countDownLatch;

    //private TextView mTextViewPath;
    //private EditText mSearchText;
    private String mOutputString;
    private Handler mEditViewHandle;
    /**
     * main ui interface
     */
    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabLog;
    private List<String> mFileDir = new ArrayList<>();
    private int mCurFileDirIndex = 0;
    private ListItemAdapter mAdapter = null;

    private boolean mIsShow_hidefile = false;

    //region SD文件管理器初始化

    private UI_TabSdFiles() {

    }

    /**
     * @return owner object
     */
    public static UI_TabSdFiles getInstance() {
        if (sUiTabSdFiles == null) {
            sUiTabSdFiles = new UI_TabSdFiles();
            sUiTabSdFiles.mMainActivity = MainActivity.getInstance();
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
        final TextView mTextViewPath = (TextView) rootView.findViewById(R.id.textview_path);
        // search text
        final EditText mSearchText = (EditText) rootView.findViewById(R.id.editText_search);
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
                    for (int i = mFileNames.size() - 1; i >= 0; --i
                            ) {
                        FileStruct item = mFileNames.get(i);
                        if (!item.mFileNameKey.contains(keyword)) {
                            mFileNames.remove(i);
                        }
                    }
                    mAdapter.addAll(mFileNames);
                }
            }
        });

        //fileListView.

        final String rootDir;
        rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        //mAdapter = new ArrayAdapter<>(mMainActivity, simple_expandable_list_item_1, mFileNames);
        //fileListView.setAdapter(mAdapter);
        File file = new File(rootDir);
        FileStruct rootStruct = new FileStruct(file);
        rootStruct.mSize = file.getFreeSpace();
        rootStruct.mFileCount = FileStruct.cCountFile;
        //rootStruct.mFileName =
        mAdapter = new ListItemAdapter(mMainActivity, mFileNames, rootStruct);

        fileListView.setAdapter(mAdapter);

        fileListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FileStruct filedirStruct = mFileNames.get(position);
                        //Toast.makeText(mMainActivity, filedirStruct.toString(), Toast.LENGTH_SHORT).show();

                        if (filedirStruct.mIsFileOrFalseDir) {
                            FileManage.openFile(mMainActivity, filedirStruct.mFilePath);
                        } else {
                            //mCurFileDirIndex = position;
                            UI_TabSdFiles.this.switchDir(filedirStruct.mFilePath);
                        }
                    }
                });

        //LogToSystem.d(TAG+"initUI","initUI");

        //region button event process
        // back button
        Button btnBack = (Button) rootView.findViewById(R.id.button_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LogToSystem.d(TAG, "跳转前一目录");
                // 返回键
                if (mCurFileDirIndex > 0) {
                    --mCurFileDirIndex;

                    UI_TabSdFiles.this.switchDir(mFileDir.get(mCurFileDirIndex), false);

                }
            }
        });

        // forward button
        Button btnForward = (Button) rootView.findViewById(R.id.button_forward);
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.EnableScanAllSize(true);
            }
        });

        final Spinner searchCondition = (Spinner) rootView.findViewById(R.id.spinner_search);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mMainActivity, android.R.layout.simple_spinner_dropdown_item, ListItemAdapter.cSEARCH_CONDITION);

        searchCondition.setAdapter(adapter);
        searchCondition.setOnItemSelectedListener(new SearchConditionOnClickList());

        // show or hide the hide file
        CheckBox showHideFile = (CheckBox) rootView.findViewById(R.id.checkBox_showHideFile);
        showHideFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox tmpCheckbox = (CheckBox) v;
                mIsShow_hidefile = tmpCheckbox.isChecked();

                //mFileNames = switchDir(mFileDir.get(mCurFileDirIndex), false);
                List<FileStruct> allFiles = mFileNames;

                if (mIsShow_hidefile) {
                    UI_TabSdFiles.this.switchDir(mFileDir.get(mCurFileDirIndex), false);
                    //mAdapter.addAll(allFiles);
                } else {
                    //List<FileStruct> tmpFiles = allFiles.subList(0, allFiles.size());
                    for (int i = allFiles.size() - 1; i >= 0; --i
                            ) {
                        FileStruct item = allFiles.get(i);
                        if (item.mIsHide) {
                            allFiles.remove(i);
                        }
                    }

                    mAdapter.addAll(allFiles);
                }
            }
        });
        //endregion

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
                } else if (msg.what == WHAT_SET_SORT) {
                    Bundle bundle = msg.getData();
                    try {
                        int pos = Integer.decode(bundle.getString(cKEY_TXT));
                        searchCondition.setSelection(pos);
                    } catch (Exception ex) {
                        LogToSystem.e(TAG + "mEditViewHandle", ex.getMessage());
                    }
                }
                super.handleMessage(msg);
            }
        };

        switchDir(rootDir);

        // 初始化弹出菜单
        PopupMenu popupMenu = new PopupMenu(mMainActivity, fileListView);
        Menu menu = popupMenu.getMenu();

        // 通过代码添加菜单项
        menu.add(Menu.NONE, Menu.FIRST + 0, 0, "复制");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "粘贴");

        // 通过XML文件添加菜单项
        MenuInflater menuInflater = mMainActivity.getMenuInflater();
        menuInflater.inflate(R.menu.menu_file, menu);

        PopupWindowHelper popupWindowHelper = new PopupWindowHelper(fileListView);
        fileListView.setOnItemLongClickListener(new ProcListItemLongClick(popupWindowHelper, fileListView));

        displayLog("SD文件管理加载完成");
    }

    //endregion

    //region 窗体事件响应函数

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

    //endregion

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
        sendMessage(WHAT_SET_SORT, "0");
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

        bundle.putString(cKEY_TXT, txtMsg);

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

    /**
     * 排序筛选条件点击事件处理类
     */
    private class SearchConditionOnClickList implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner) parent;
            mAdapter.sortByUser(spinner.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ProcListItemLongClick implements AdapterView.OnItemLongClickListener {

        private PopupWindowHelper mPopMenu;
        private View mPopView;


        ProcListItemLongClick(PopupWindowHelper popMenu, View popView) {
            mPopMenu = popMenu;
            mPopView = popView;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            int x = (int) view.getX();
            int y = 0;//(int)view.getY();

            try {
                //((LinearLayout)view).removeView(mPopView);
                /*ViewGroup parent1 = (ViewGroup) mPopView.getParent();
                if (parent1 != null) {
                    parent1.removeView(mPopView);
                }*/
                mPopMenu.showAsPopUp(mPopView, x, y);
                //parent1.addView(mPopView);
            } catch (Exception ex) {
                LogToSystem.e(TAG + "onItemLongClick", ex.getMessage());
            }
            return true;
        }
    }

}

