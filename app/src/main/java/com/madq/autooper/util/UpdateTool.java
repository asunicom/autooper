package com.madq.autooper.util;

import android.util.Log;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSONObject;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.madq.autooper.App;
import com.madq.autooper.BuildConfig;
import com.madq.autooper.domain.Version;

/**
 * @author madeqiang
 * @version 1.0
 * @date 2019/11/15 15:51
 */
public class UpdateTool {
    private DownloadBuilder builder;
    private static UpdateTool updateTool;

    private UpdateTool() {
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("http://my-json-server.typicode.com/asunicom/api/update_auto_video")
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(final DownloadBuilder downloadBuilder, String result) {
                        Log.e("mdq", result);
                        Version version = JSONObject.parseObject(result, Version.class);
                        if (version != null && version.getApkVersion() > BuildConfig.VERSION_CODE) {
                            if (version.getNeedForceUpdate() > 0) {
                                downloadBuilder.setForceUpdateListener(new ForceUpdateListener() {
                                    @Override
                                    public void onShouldForceUpdate() {

                                    }
                                });
                            }
                            return UIData.create().setTitle(version.getUpdateTitle())
                                    .setContent(version.getApkDesc())
                                    .setDownloadUrl(version.getApkUrl());

                        }
                        return null;
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {

                    }
                }).setForceRedownload(true)
                .setDownloadAPKPath(App.context.getExternalFilesDir("download").getAbsolutePath() + "/");
    }

    public static UpdateTool getInstance() {
        if (updateTool == null) {
            updateTool = new UpdateTool();
        }
        return updateTool;
    }

    public DownloadBuilder getBuilder() {
        return builder;
    }


}
