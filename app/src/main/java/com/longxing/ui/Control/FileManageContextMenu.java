package com.longxing.ui.Control;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.longxing.R;
import com.longxing.log.LogToSystem;

/**
 * Created by Zhang Long on 2017/8/18.
 */

public class FileManageContextMenu {

    private static final int cTagValue_addDir = 1;
    private static final int cTagValue_rename = 2;
    private static final int cTagValue_delete = 3;
    private static final String TAG = "MyLog/FileManageContextMenu";

    private PopupWindowHelper mPopupWindowHelper;

    private EditText mEditText;

    public void initUI(View rootView) {
        mPopupWindowHelper = new PopupWindowHelper(rootView);

        mEditText = (EditText)rootView.findViewById(R.id.editText_name);
        mEditText.setVisibility(View.INVISIBLE);

        ProcBtnClick procBtnClick = new ProcBtnClick(rootView);
        Button btnNewDir = (Button)rootView.findViewById(R.id.button_newDir);
        btnNewDir.setOnClickListener(procBtnClick);
        btnNewDir.setTag(cTagValue_addDir);

        Button btnRename = (Button)rootView.findViewById(R.id.button_rename);
        btnRename.setOnClickListener(procBtnClick);
        btnRename.setTag(cTagValue_rename);

        Button btnDel = (Button)rootView.findViewById(R.id.button_del);
        btnDel.setOnClickListener(procBtnClick);
        btnDel.setTag(cTagValue_delete);

    }

    public void showAsPopUp(View parent, int x, int y) {
        // init menu
        mEditText.setVisibility(View.INVISIBLE);

        mPopupWindowHelper.showAsPopUp(parent, x, y);
    }

    private class ProcBtnClick implements View.OnClickListener{

        View mRootView;

        ProcBtnClick(View rootView){
            mRootView = rootView;
        }
        @Override
        public void onClick(View v) {
            Button btn = (Button)v;
            int tagValue = (int)btn.getTag();

            switch (tagValue){
                case cTagValue_addDir:
                    mEditText.setHint(R.string.hint_name);
                    mEditText.setText("");
                    mEditText.setVisibility(View.VISIBLE);
                    break;
                case cTagValue_rename:
                    LogToSystem.d(TAG+"onClick", "rename");
                    break;
                default:
                    break;
            }

        }
    }

}
