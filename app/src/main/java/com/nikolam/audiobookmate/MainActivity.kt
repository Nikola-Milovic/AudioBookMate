package com.nikolam.audiobookmate

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.nikolam.common.navigation.NavManager
import com.nikolam.common.permissions.PermissionHelper
import com.nikolam.common.permissions.Permissions
import com.nikolam.feature_folders.FolderManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val layoutResId = R.layout.activity_main

    private val navController get() = navHostFragment.findNavController()

    private lateinit var permissions: Permissions
    private lateinit var permissionHelper: PermissionHelper

    private val navManager: NavManager by inject()

    private fun initNavManager() {
        navManager.setOnNavEvent {
            navController.navigate(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavManager()

        setContentView(layoutResId)

        val folderManager: FolderManager = get()

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
        permissionHelper.storagePermission { Timber.d("Got permission for storage") }
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
