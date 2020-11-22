package com.nikolam.audiobookmate

import android.Manifest
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nikolam.book_overview.misc.PermissionHelper
import com.nikolam.book_overview.misc.Permissions
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val layoutResId = R.layout.activity_main

    private lateinit var permissions: Permissions
    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResId)

        supportActionBar?.hide()

        // The window will not be resized when virtual keyboard is shown (bottom navigation bar will be
        // hidden under virtual keyboard)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        permissions = Permissions(this)
        permissionHelper = PermissionHelper(this, permissions)

        Timber.v("onCreate ${javaClass.simpleName}")
    }


    override fun onStart() {
        super.onStart()
        // permissions
        permissionHelper.storagePermission { Timber.d("Got permission for storage")}
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.permissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
