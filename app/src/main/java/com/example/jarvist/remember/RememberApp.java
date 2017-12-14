package com.example.jarvist.remember;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Jarvist on 2017/12/14.
 */

public class RememberApp extends Application {

    @Override
    public void onCreate() {
        AVOSCloud.initialize(this,"NfLUjobJ47uyIrq8mAbCxDvG-gzGzoHsz","NLRMoEeObaCICX57Xq6qG53B");
        AVOSCloud.setDebugLogEnabled(true);
        super.onCreate();
    }
}
