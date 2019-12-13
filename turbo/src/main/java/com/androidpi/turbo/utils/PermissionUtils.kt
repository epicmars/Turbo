package com.androidpi.turbo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * Created on 2019-08-09.
 */
object PermissionUtils {
    val EXTERNAL_STORAGE_PERMS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val LOCATION_PERMS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val CAMERA_PERMS =
        arrayOf(Manifest.permission.CAMERA)

    val SHORTCUT_PERMS = arrayOf(
        Manifest.permission.INSTALL_SHORTCUT,
        Manifest.permission.UNINSTALL_SHORTCUT
    )

    fun hasStoragePermission(context: Context?): Boolean {
        return EasyPermissions.hasPermissions(context!!, *EXTERNAL_STORAGE_PERMS)
    }

    fun hasLocationPermission(context: Context?): Boolean {
        return EasyPermissions.hasPermissions(context!!, *LOCATION_PERMS)
    }

    fun hasCameraPermission(context: Context?): Boolean {
        return EasyPermissions.hasPermissions(context!!, *CAMERA_PERMS)
    }

    fun requestPermission(
        activity: Activity,
        reqCode: Int,
        perms: Array<String>,
        rationale: String?
    ) {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(activity, reqCode, *perms)
                .setRationale(rationale)
                .build()
        )
    }

    fun requestPermission(
        fragment: Fragment?,
        reqCode: Int,
        perms: Array<String>,
        rationale: String?
    ) {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(fragment!!, reqCode, *perms)
                .setRationale(rationale)
                .build()
        )
    }
}