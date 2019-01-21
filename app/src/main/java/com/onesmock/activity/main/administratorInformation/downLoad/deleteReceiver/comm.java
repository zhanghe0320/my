package com.onesmock.activity.main.administratorInformation.downLoad.deleteReceiver;

import android.os.Environment;

import java.io.File;


public class comm {
    public static File getPathFile(String path) {
        String apkName = path.substring(path.lastIndexOf("/"));
        File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), apkName);
        return outputFile;
    }

    public static void rmoveFile(String path) {
        File file = getPathFile(path);
        file.delete();
    }
}