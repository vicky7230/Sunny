package com.vicky7230.sunny.ui.base

import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vicky7230.sunny.utils.CommonUtils
import com.vicky7230.sunny.utils.ThemeUtils
import retrofit2.HttpException

/**
 * Created by vicky on 11/2/18.
 */
open class BaseActivity : AppCompatActivity() {

    private var progressDialog: Dialog? = null

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        return ThemeUtils.getTheme(theme)
    }

    fun hasPermissions(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        return permissions.none {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    private fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showLoading() {
        hideLoading()
        progressDialog = CommonUtils.showLoadingDialog(this)
    }

    fun hideLoading() {
        if (progressDialog != null) {
            if (progressDialog?.isShowing == true)
                progressDialog?.cancel()
        }
    }

    fun showMessage(message: String?) {
        if (message != null)
            displayMessage(message)
    }

    fun showError(message: String?) {
        if (message != null)
            displayError(message)
    }

    fun handleApiError(throwable: Throwable) {
        if (throwable is HttpException) {
            when (throwable.code()) {
                401 -> {
                    showError("sessionId expired or invalid.")
                    // TODO
                    // log out the user and clear his data
                }
            }
        } else {
            showError(throwable.message ?: "")
        }
    }
}