package com.prudhvi.inappupdateplaystore;

import com.google.android.play.core.appupdate.AppUpdateInfo;

public interface UpdateListener {

    void onUpdateAvailable();

    void onUpdateDownloaded();

    void onUpdateFailed();
}

