package com.hot;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.RNFetchBlob.RNFetchBlobPackage;
import com.hot.contansts.FileConstant;
import com.lwansbrough.RCTCamera.RCTCameraPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
    private static MainApplication applicationContext;

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new RNFetchBlobPackage(),
                    new RCTCameraPackage()
            );
        }

        @Nullable
        @Override
        protected String getJSBundleFile() {
            File file = new File (FileConstant.JS_BUNDLE_LOCAL_PATH);
            if(file != null && file.exists()) {
                return FileConstant.JS_BUNDLE_LOCAL_PATH;
            } else {
                return super.getJSBundleFile();
            }
        }
    };

    public String getAppPackageName() {
        return this.getPackageName();
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = (MainApplication) getApplicationContext();
        SoLoader.init(this, /* native exopackage */ false);
    }

    public static MainApplication getInstance() {
        return applicationContext;
    }
}
