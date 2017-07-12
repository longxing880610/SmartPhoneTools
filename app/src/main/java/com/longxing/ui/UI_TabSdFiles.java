package com.longxing.ui;

import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.longxing.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_expandable_list_item_1;

/**
 * Created by Zhang Long on .
 * <p>
 * show sd files
 */

class UI_TabSdFiles implements IUI_TabMain{
    /**
     * tag for log
     */
    //private static final String TAG = "MyLog/UI_TabSdFiles";

    private static UI_TabSdFiles sUiTabLog;


    private AdapterView.OnItemClickListener fileListViewItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    // TODO Auto-generated method stub

                    //test.........................
                    // TextView textview = (TextView)findViewById(R.id.textViewfile);
                    // textview.append(mFileName.get(position).toString());
                }
            };

    /**
     * main ui interface
     */
    private MainActivity mMainActivity;
    private UI_TabLog mUI_tabSdFiles;


    private UI_TabSdFiles() {

    }

    /**
     * @return owner object
     */
    static UI_TabSdFiles getInstance() {
        if (sUiTabLog == null) {
            sUiTabLog = new UI_TabSdFiles();
            sUiTabLog.mMainActivity = MainActivity.GetInstance();
            sUiTabLog.mUI_tabSdFiles = UI_TabLog.getInstance();
        }
        return sUiTabLog;
    }

    public @Override void initUI(View rootView) {

        ListView fileListView = (ListView) rootView.findViewById(R.id.sdFiles);

        //fileListView.

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file=new File(filePath);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return true;//name.startsWith("a");
            }
        });

        List<String> mFileName = new ArrayList<>();

        for (File mCurrentFile:files){
            mFileName.add(mCurrentFile.getName());
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(mMainActivity, simple_expandable_list_item_1, mFileName);
        fileListView.setAdapter(mAdapter);

        fileListView.setOnItemClickListener(fileListViewItemClickListener);
        //final ScrollView scrollViewLog = (ScrollView) rootView.findViewById(R.id.ScrollLog);


        displayLog("SD文件管理加载完成");
    }

    /**
     * @param msg show message
     */
    private void displayLog(String msg) {
        mUI_tabSdFiles.displayLog(msg);
    }

}
