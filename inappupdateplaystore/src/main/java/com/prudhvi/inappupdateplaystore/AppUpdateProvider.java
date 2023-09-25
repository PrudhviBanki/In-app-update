package com.prudhvi.inappupdateplaystore;

import static com.prudhvi.inappupdateplaystore.Constants.REQUEST_CODE_UPDATE;

import android.app.Activity;
import android.content.Context;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

public class AppUpdateProvider {

    private final AppUpdateManager appUpdateManager;
    private final UpdateListener updateListener;

    private AppUpdateInfo appUpdateInfo;

    public Context context;
    private final InstallStateUpdatedListener installStateUpdatedListener;

    public AppUpdateProvider(Context context, UpdateListener listener) {
        this.context = context;
        appUpdateManager = AppUpdateManagerFactory.create(context);
        updateListener = listener;

        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                // An update has been downloaded, prompt the user to install it
                updateListener.onUpdateDownloaded();
            }
        };

        // Register the listener
        appUpdateManager.registerListener(installStateUpdatedListener);
    }

    public void checkForUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            this.appUpdateInfo=appUpdateInfo;
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                updateListener.onUpdateAvailable();
            }
        });
    }

    public void startUpdate(int type) {
        if (appUpdateInfo != null) {
            try {
                if (type == 1) {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            (Activity) context,
                            REQUEST_CODE_UPDATE);
                } else {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            (Activity) context,
                            REQUEST_CODE_UPDATE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                updateListener.onUpdateFailed();
            }
        }
    }

    public void unregisterCall() {
        appUpdateManager.unregisterListener(installStateUpdatedListener);
    }
}
