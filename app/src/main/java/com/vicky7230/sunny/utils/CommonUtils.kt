package com.vicky7230.sunny.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.vicky7230.sunny.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by vicky on 11/2/18.
 */
object CommonUtils {

    fun showLoadingDialog(context: Context): Dialog {
        val progressDialog = Dialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    fun getCurrentTime(): String {
        val df = SimpleDateFormat("KK:mm a", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        return df.format(calendar.time)
    }


    fun getTimeFromUnixTimeStamp(unixTimeStamp: Long): String {
        val date = Date(unixTimeStamp * 1000L) // *1000 is to convert seconds to milliseconds
        val simpleDateFormat = SimpleDateFormat("KK:mm a", Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }

    fun getTimeForWidget(unixTimeStamp: Long): String {
        val date = Date(unixTimeStamp * 1000L) // *1000 is to convert seconds to milliseconds
        val simpleDateFormat = SimpleDateFormat("EEE dd LLL, YYYY", Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }
}