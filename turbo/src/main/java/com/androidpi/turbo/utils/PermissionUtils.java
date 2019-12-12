package com.androidpi.turbo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * Created on 2019-08-09.
 */
public class PermissionUtils {

    public static final String[] EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final String[] CAMERA_PERMS = {Manifest.permission.CAMERA};

    public static final String[] SHORTCUT_PERMS = {
            Manifest.permission.INSTALL_SHORTCUT,
            Manifest.permission.UNINSTALL_SHORTCUT
    };

    public static void requestPermission(Fragment fragment, int reqCode, String[] perms, String rationale) {
        EasyPermissions.requestPermissions(new PermissionRequest.Builder(fragment, reqCode, perms)
                .setRationale(rationale)
                .build());
    }
    
    public static boolean hasStoragePermission(Context context) {
        return EasyPermissions.hasPermissions(context, EXTERNAL_STORAGE_PERMS);
    }

    public static boolean hasLocationPermission(Context context) {
        return EasyPermissions.hasPermissions(context, LOCATION_PERMS);
    }

    public static boolean hasCameraPermission(Context context) {
        return EasyPermissions.hasPermissions(context, CAMERA_PERMS);
    }

    public static void requestPermission(
            Activity activity, int reqCode, String[] perms, String rationale) {
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(activity, reqCode, perms)
                        .setRationale(rationale)
                        .build());
    }
}
