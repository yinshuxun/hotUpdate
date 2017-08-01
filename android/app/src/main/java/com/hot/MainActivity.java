package com.hot;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.google.zxing.common.StringUtils;
import com.hot.contansts.FileConstant;
import com.hot.utils.HttpClient;
import com.hot.utils.IOUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Nullable;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends ReactActivity {
    private String remoteVersion;
    private String localVersion;
    private static File zipfile;
    private static Long mDownLoadId;
    private static final String TAG = "HOT_UPLOAD";
    private final OkHttpClient client = new OkHttpClient();
    CompleteReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registeReceiver();
        checkVersion();
    }

    @Nullable
    @Override
    protected String getMainComponentName() {
        return "hot";
    }

    private void registeReceiver() {
        CompleteReceiver localReceiver = new CompleteReceiver();
        registerReceiver(localReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: http request.
                try {
                    JSONObject ob = HttpClient.get(FileConstant.JS_BUNDLE_REMOTE_URL);
                    remoteVersion = ob.getString("version");
                    String zipUrl = ob.getString("zip");
                    localVersion = IOUtil.getCurrVersion();
                    if(!remoteVersion.equals(localVersion)){
                        downLoadBundle(zipUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        Request request = new Request.Builder()
//                .url(FileConstant.JS_BUNDLE_REMOTE_URL)
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//            JSONObject ob = new JSONObject(response.body().string());
//            remoteVersion = ob.getString("version");
//            String zipUrl = ob.getString("zip");
//            if (remoteVersion != IOUtil.getCurrVersion()) {
//                Toast.makeText(MainActivity.this, "最新版本" + remoteVersion, Toast.LENGTH_SHORT).show();
//            }
//
//            Toast.makeText(MainActivity.this, "版本相同，无需下载", Toast.LENGTH_SHORT).show();
//            downLoadBundle(zipUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public static String unZip(String zipPath) {
        return "";
    }

    public void downLoadBundle(String remotePath) {
        // 1.检查是否存在pat压缩包,存在则删除
        isFolderExists(FileConstant.JS_PATCH_LOCAL_FOLDER);

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

    boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;
    }

    public class CompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeId == mDownLoadId) {
                Toast.makeText(MainActivity.this, "本地版本" + localVersion + " 远程版本" + remoteVersion, Toast.LENGTH_SHORT).show();
                // 1.解压
                decompression();
                zipfile.delete();
                IOUtil.writeVersion(remoteVersion);
                Toast.makeText(MainActivity.this, "安装完成，重新启动", Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(MainActivity.this, ReactNewActivity.class));
            }
        }
    }

    public void decompression() {

        try {

            ZipInputStream inZip = new ZipInputStream(new FileInputStream(FileConstant.JS_PATCH_LOCAL_PATH));
            ZipEntry zipEntry;
            String szName;
            isFolderExists(FileConstant.FUTURE_JS_PATCH_LOCAL_FOLDER);
            try {
                while ((zipEntry = inZip.getNextEntry()) != null) {

                    szName = zipEntry.getName();
                    if (zipEntry.isDirectory()) {

                        szName = szName.substring(0, szName.length() - 1);
                        File folder = new File(FileConstant.FUTURE_JS_PATCH_LOCAL_FOLDER + File.separator + szName);
                        folder.mkdirs();

                    } else {

                        File file1 = new File(FileConstant.FUTURE_JS_PATCH_LOCAL_FOLDER + File.separator + szName);
                        boolean s = file1.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file1);
                        int len;
                        byte[] buffer = new byte[1024];

                        while ((len = inZip.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                        }

                        fos.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            inZip.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(localReceiver);
    }
}
