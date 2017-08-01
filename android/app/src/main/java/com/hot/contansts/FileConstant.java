package com.hot.contansts;

import android.os.Environment;

import com.hot.MainApplication;

import java.io.File;

public class FileConstant {
    public static String version = "0.0.1";

    /**
     * zip的文件名
     */
    public static final String ZIP_NAME = "hot";

    /**
     * bundle文件名
     */
    public static final String JS_BUNDLE_LOCAL_FILE = "index.android.bundle";

    public static final String PATCH_IMG_FILE = "patch_imgs.txt";

    public static final String VERSION_FILE = "version.txt";

    /**
     * 第一次解压zip后的文件目录
     */
    public static final String JS_PATCH_LOCAL_FOLDER = Environment.getExternalStorageDirectory().toString()
            + File.separator + MainApplication.getInstance().getAppPackageName();

    //version文件路径
    public static final String VERSION_FILE_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER + "/" + VERSION_FILE;


    public static final String LOCAL_FOLDER = JS_PATCH_LOCAL_FOLDER + "/" + ZIP_NAME;

    public static final String DRAWABLE_PATH = JS_PATCH_LOCAL_FOLDER + "/" + ZIP_NAME + "/drawable-mdpi/";

    /**
     * 除第一次外，未来解压zip后的文件目录
     */
    public static final String FUTURE_JS_PATCH_LOCAL_FOLDER = JS_PATCH_LOCAL_FOLDER+"/future";

    public static final String FUTURE_DRAWABLE_PATH = FUTURE_JS_PATCH_LOCAL_FOLDER + "/"+ ZIP_NAME + "/drawable-mdpi/";
    public static final String FUTURE_PAT_PATH = FUTURE_JS_PATCH_LOCAL_FOLDER+"/wan/"+"bundle.pat";

    /**
     * zip文件
     */
    public static final String JS_PATCH_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER + File.separator + ZIP_NAME + version + ".zip";


    /**
     * 合并后的bundle文件保存路径
     */
//    public static final String JS_BUNDLE_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER +"/hot/" + JS_BUNDLE_LOCAL_FILE;

    /**
     * 解压后的bundle文件保存路径
     */
    public static final String JS_BUNDLE_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER +"/future/" + JS_BUNDLE_LOCAL_FILE;

    /**
     * .pat文件
     */
    public static final String JS_PATCH_LOCAL_FILE = JS_PATCH_LOCAL_FOLDER +"/hot/bundle.pat";

    /**
     * 增量图片名称文件路径
     */
    public static final String PATCH_IMG_NAMES_PATH = JS_PATCH_LOCAL_FOLDER +"/hot/" + PATCH_IMG_FILE;

    /**
     * 下载URL
     */
    public static final String JS_BUNDLE_REMOTE_URL = "http://192.168.31.174:9000/version";
}
