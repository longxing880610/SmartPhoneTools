package com.longxing.ui.Control;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.longxing.R;
import com.longxing.file.FileStruct;
import com.longxing.log.LogToSystem;
import com.longxing.ui.MainActivity;

import java.io.File;

/**
 * Created by Zhang Long on 2017/8/18.
 * <p>
 * file manage context menu
 */

public class FileManageContextMenu {

    private static final int cTagValue_invalid = 0;
    private static final int cTagValue_addDir = 1;
    private static final int cTagValue_rename = 2;
    private static final int cTagValue_delete = 3;
    private static final int cTagValue_ok = 4;
    private static final String TAG = "MyLog/FileManageContextMenu";

    private PopupWindowHelper mPopupWindowHelper;

    private EditText mEditText;
    private Button mButtonOK;
    private TextView mTextPath;
    private FileStruct mSelFileStruct;
    private Button mbtnNewDir;
    private Button mbtnRename;
    private Button mbtnDel;

    public void initUI(View rootView) {
        mPopupWindowHelper = new PopupWindowHelper(rootView);
        ProcBtnClick procBtnClick = new ProcBtnClick();

        mEditText = (EditText) rootView.findViewById(R.id.editText_name);
        //mEditText.setVisibility(View.INVISIBLE);
        mButtonOK = (Button) rootView.findViewById(R.id.button_ok);
        mButtonOK.setOnClickListener(procBtnClick);
        mButtonOK.setTag(cTagValue_ok);

        Button btnNewDir = (Button) rootView.findViewById(R.id.button_newDir);
        btnNewDir.setOnClickListener(procBtnClick);
        btnNewDir.setTag(cTagValue_addDir);
        mbtnNewDir = btnNewDir;

        Button btnRename = (Button) rootView.findViewById(R.id.button_rename);
        btnRename.setOnClickListener(procBtnClick);
        btnRename.setTag(cTagValue_rename);
        mbtnRename = btnRename;

        Button btnDel = (Button) rootView.findViewById(R.id.button_del);
        btnDel.setOnClickListener(procBtnClick);
        btnDel.setTag(cTagValue_delete);
        mbtnDel = btnDel;

        mTextPath = (TextView) rootView.findViewById(R.id.textView_pathMenu);

    }

    public void showAsPopUp(View parent, int x, int y) {

        // get the select item file path
        ListView listView = (ListView) parent;
        int position = listView.pointToPosition(x, y);
        mSelFileStruct = (FileStruct) listView.getItemAtPosition(position);
        if (mSelFileStruct.mIsRoot || mSelFileStruct.mIsParent) {
            mbtnRename.setVisibility(View.INVISIBLE);
            mbtnDel.setVisibility(View.INVISIBLE);
        } else {

            mbtnRename.setVisibility(View.VISIBLE);
            mbtnDel.setVisibility(View.VISIBLE);
        }

        mTextPath.setText(mSelFileStruct.mFilePath);


        // init menu
        mEditText.setVisibility(View.INVISIBLE);
        mButtonOK.setVisibility(View.INVISIBLE);

        mPopupWindowHelper.showAsPopUp(parent, x, y);
    }

    private void showToast(String msg) {
        MainActivity.getInstance().showToast(msg);
    }

    private void showSnackbar(String msg) {
        MainActivity.getInstance().showSnackbar(msg);
    }

    private class ProcBtnClick implements View.OnClickListener {

        //private View mRootView;
        private int mButtonTag = cTagValue_invalid;

        ProcBtnClick() {
        }

        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            int tagValue = (int) btn.getTag();

            if (tagValue != cTagValue_ok) {
                mButtonTag = tagValue;
            }
            switch (tagValue) {
                case cTagValue_ok:
                    LogToSystem.d(TAG + "onClick", "cTagValue_ok");
                    if (mSelFileStruct != null) {
                        try {
                            switch (mButtonTag) {
                                case cTagValue_addDir:
                                    String fileName = mEditText.getText().toString().trim();
                                    String newPath = mSelFileStruct.mCurdirIn + File.separator + fileName;
                                    File file = new File(newPath);
                                    if (!file.exists()) {
                                        boolean reslt = file.mkdirs();
                                        if (!reslt) {
                                            showSnackbar("文件夹(" + fileName + ")创建失败");
                                        } else {
                                            showToast("文件夹(" + fileName + ")创建成功,需要打开才可见");
                                        }
                                    } else {
                                        showToast("文件夹(" + fileName + ")已经存在");
                                    }
                                    break;
                                case cTagValue_rename:
                                    fileName = mEditText.getText().toString().trim();
                                    String path = mSelFileStruct.mFilePath;
                                    newPath = mSelFileStruct.getmFileDir() + File.separator + fileName;
                                    file = new File(path);
                                    if (file.exists()) {

                                        String premsg = "文件";
                                        if (!mSelFileStruct.mIsFileOrFalseDir) {
                                            premsg += "夹";
                                        }
                                        premsg += "(";
                                        File newFile = new File(newPath);
                                        if (newFile.exists()) {
                                            showSnackbar(premsg + fileName + ")已存在, 重命名失败");
                                        } else {
                                            boolean reslt = file.renameTo(newFile);
                                            if (!reslt) {
                                                showSnackbar(premsg + fileName + ")重命名失败");
                                            } else {
                                                showToast(premsg + fileName + ")重命名成功,需要打开看得到效果");
                                            }
                                        }
                                    }
                                    break;
                                case cTagValue_delete:
                                    fileName = mEditText.getText().toString().trim();
                                    path = mSelFileStruct.mFilePath;
                                    file = new File(path);
                                    if (file.exists()) {
                                        boolean reslt = file.delete();
                                        String premsg = "文件";
                                        if (!mSelFileStruct.mIsFileOrFalseDir) {
                                            premsg += "夹";
                                        }
                                        premsg += "(";
                                        if (!reslt) {
                                            showSnackbar(premsg + fileName + ")删除失败");
                                        } else {
                                            showToast(premsg + fileName + ")删除成功,需要打开才不可见");
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception ex) {
                            LogToSystem.e(TAG + "onClick_ok", ex.getMessage());
                        }
                    }
                    mPopupWindowHelper.dismiss();
                    break;
                case cTagValue_addDir:
                    mEditText.setHint(R.string.hint_name);
                    mEditText.setText("");
                    mEditText.setVisibility(View.VISIBLE);
                    mButtonOK.setVisibility(View.VISIBLE);
                    break;
                case cTagValue_rename:
                    if (mSelFileStruct != null) {
                        String fileName = mSelFileStruct.mFileName;
                        mEditText.setText(fileName);
                        mEditText.setVisibility(View.VISIBLE);
                        mButtonOK.setVisibility(View.VISIBLE);
                    }
                    break;
                case cTagValue_delete:
                    mEditText.setText(R.string.deleteYes);
                    mEditText.setVisibility(View.VISIBLE);
                    mButtonOK.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

        }
    }

}
