package com.longxing.ui;

import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Zhang Long on 2017/7/10.
 * <p>
 * interface for tab main
 */
public interface IUI_TabMain {
    /**
     * init user interface for tab
     *
     * @param rootView parent view
     */
    void initUI(View rootView);

    /**
     * process key down event
     * @param keyCode
     * @param event
     */
    void processKeyDown(int keyCode, KeyEvent event);
}
