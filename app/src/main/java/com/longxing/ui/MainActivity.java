package com.longxing.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.longxing.R;
import com.longxing.common.ConstDef;
import com.longxing.log.LogToSystem;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyLog/MainActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static MainActivity sMainActivity;
    //private UI_TabLog mUiTabLog;

    //region back key to exit application
    /**
     * count of back key, use to check if exit the application
     */
    private int mBackKeyCount = 0;

    private Timer mTimer = null;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        String userName = getIntent().getStringExtra(ConstDef.cUserName);
        //String userName = bundle.getString(ConstDef.cUserName);// 得到子窗口ChildActivity的回传数据
        TextView editWel = (TextView) findViewById(R.id.editText_welcome);
        editWel.setText("欢迎您," + userName);

        sMainActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstDef.cActiveLogin:         // 子窗口ChildActivity的回传数据
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        //处理代码在此地

                    }
                }
                break;
            default:
                //其它窗口的回传数据
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //region 按钮事件响应函数

    /**
     * 点击程序版本按钮
     *
     * @param view the view
     */
    public void onBtnVersionClick(View view) {
        Toast.makeText(MainActivity.this, this.getString(R.string.app_version), Toast.LENGTH_SHORT).show();
    }
    //endregion

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int tabId = mViewPager.getCurrentItem();
        //LogToSystem.d(TAG, "onKeyDown:" + keyCode + "&" + tabId);
        IUI_TabMain tab = mSectionsPagerAdapter.getPageInterface(tabId);
        if (tab != null) {
            if (tab.processKeyDown(keyCode, event)) {
                return true;
            }
        }
        // check the back key, delay to exit application
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ++mBackKeyCount;
            if (mTimer == null) {
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    public void run() {
                        //LogToSystem.d(TAG, "退出应用的定时器:" + mBackKeyCount);
                        mBackKeyCount = 0;
                        mTimer.cancel();
                        mTimer = null;
                    }
                }, 5 * 1000, 5 * 1000);
            }
            if (mBackKeyCount < 2) {
                //LogToSystem.d(TAG, "再按一次退出应用");
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                return true;
            }
            return moveTaskToBack(false);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        int tabId = mViewPager.getCurrentItem();
        //LogToSystem.d(TAG, "onKeyDown:" + keyCode + "&" + tabId);
        IUI_TabMain tab = mSectionsPagerAdapter.getPageInterface(tabId);
        if (tab != null) {
            tab.processDestroy();
        }

        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int tabId = getArguments().getInt(ARG_SECTION_NUMBER);
            //LogToSystem.d(TAG, "onCreateView_page:"+tabId);
            return sMainActivity.mSectionsPagerAdapter.getPageView(tabId, inflater, container);
        }
    }

    public static MainActivity GetInstance() {
        return sMainActivity;
    }

}
