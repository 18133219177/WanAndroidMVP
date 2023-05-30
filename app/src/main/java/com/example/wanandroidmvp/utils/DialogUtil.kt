package com.example.wanandroidmvp.utils

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnCancelListener
import android.text.TextUtils
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog

object DialogUtil {

    fun getDialog(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    fun getWaitDialog(context: Context, message: String): ProgressDialog {
        val waitDialog = ProgressDialog(context)
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message)
        }
        return waitDialog
    }

    fun getMessageDialog(
        context: Context, message: String,
        onClickListener: DialogInterface.OnClickListener? = null
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton("确定", onClickListener)
        return builder
    }

    fun getConfirmDialog(
        context: Context, message: String, onClickListener: DialogInterface.OnClickListener,
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton("确定", onClickListener)
        builder.setNegativeButton("取消", null)
        return builder
    }

    fun getConfirmDialog(
        context: Context, message: String, onClickListener: DialogInterface.OnClickListener,
        onCancelListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton("确定", onClickListener)
        builder.setNegativeButton("取消", onCancelListener)
        return builder
    }

    fun getSelectDialog(
        context: Context, title: String, arrays: Array<String>,
        onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setItems(arrays, onClickListener)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setNegativeButton("取消", null)
        return builder
    }


    fun getSingleChoiceDialog(
        context: Context,
        title: String,
        arrays: Array<String>,
        selectIndex: Int,
        onClickListener: DialogInterface.OnClickListener,
        onOKClickListener: DialogInterface.OnClickListener,
        onCancelClickListener: DialogInterface.OnClickListener? = null
    ): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener)
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title)
        }
        builder.setPositiveButton("确定", onOKClickListener)
        builder.setNegativeButton("取消", onCancelClickListener)
        return builder
    }
}