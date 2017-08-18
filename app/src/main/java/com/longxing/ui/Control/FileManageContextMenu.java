package com.longxing.ui.Control;

import android.view.View;

/**
 * Created by Zhang Long on 2017/8/18.
 */

public class FileManageContextMenu {

    private PopupWindowHelper mPopupWindowHelper;

    public void initUI(View rootView) {
        mPopupWindowHelper = new PopupWindowHelper(rootView);
    }

    public void showAsPopUp(View parent, int x, int y) {
        mPopupWindowHelper.showAsPopUp(parent, x, y);
    }
}
