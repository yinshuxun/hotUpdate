package com.hot;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.hot.contansts.FileConstant;
import com.hot.hotupdate.HotUpdate;

import java.io.File;

/**
 * Created by shuxun on 2017/7/31.
 */

public class BaseActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
    private String currVersion;
    private static File zipfile;
    private static Long mDownLoadId;
    private static final String TAG = "HOT_UPLOAD";
    CompleteReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkVersion();
        registeReceiver();
    }

    private void registeReceiver() {
        CompleteReceiver localReceiver = new CompleteReceiver();
        registerReceiver(localReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void invokeDefaultOnBackPressed() {

    }

    public void checkVersion() {
        if (true) {
            // 有最新版本
            Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
            downLoadBundle(FileConstant.JS_BUNDLE_REMOTE_URL);
        }
    }

    public static String unZip(String zipPath) {
        return "";
    }

    public void downLoadBundle(String remotePath) {
        // 1.检查是否存在pat压缩包,存在则删除
        zipfile = new File(FileConstant.JS_PATCH_LOCAL_PATH);
        Log.v("hot日志", "检查是否存在,检查路径:::" + FileConstant.JS_PATCH_LOCAL_PATH);
        if (zipfile != null && zipfile.exists()) {
            Log.v("hot日志", "存在,准备删除");
            zipfile.delete();
        }
        Log.v("hot日志", "准备下载");

        // 2.下载
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager
                .Request(Uri.parse(remotePath));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationUri(Uri.parse("file://" + FileConstant.JS_PATCH_LOCAL_PATH));
        mDownLoadId = downloadManager.enqueue(request);
    }



    public class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(completeId == mDownLoadId) {
                HotUpdate.handleZIP(getApplicationContext());
            }
        }
    }

}
