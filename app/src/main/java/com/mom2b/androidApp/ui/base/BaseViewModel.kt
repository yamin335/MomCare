package com.mom2b.androidApp.ui.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mom2b.androidApp.R
import com.mom2b.androidApp.utils.NetworkUtils
import com.mom2b.androidApp.utils.showErrorToast

abstract class BaseViewModel constructor(private val context: Context) : ViewModel() {

    val apiCallStatus: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val toastError = MutableLiveData<String>()
    val toastWarning = MutableLiveData<String>()
    val toastSuccess = MutableLiveData<String>()
    val popBackStack = MutableLiveData<Boolean>()

    fun checkNetworkStatus(shouldShowMessage: Boolean) = when {
        NetworkUtils.isNetworkConnected(context) -> {
            true
        }
        shouldShowMessage -> {
            showErrorToast(context, context.getString(R.string.internet_error_msg))
            false
        }
        else -> {
            false
        }
    }
}