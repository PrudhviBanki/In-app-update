package com.prudhvi.inappupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prudhvi.inappupdateplaystore.AppUpdateProvider;
import com.prudhvi.inappupdateplaystore.UpdateListener;

public class MainActivity extends AppCompatActivity implements UpdateListener {

    AppUpdateProvider appUpdateProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appUpdateProvider = new AppUpdateProvider(this, this);
        appUpdateProvider.checkForUpdate();

    }

    @Override
    public void onUpdateAvailable() {
        //1=IMMEDIATE 0=FLEXIBLE
        appUpdateProvider.startUpdate(1);
    }

    @Override
    public void onUpdateDownloaded() {

    }

    @Override
    public void onUpdateFailed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appUpdateProvider.unregisterCall();
    }
}