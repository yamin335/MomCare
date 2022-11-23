package com.mom2b.androidApp.ui.routing

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.mom2b.androidApp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RoutingActivityViewModel @Inject constructor(
    @ApplicationContext val application: Context
): BaseViewModel(application) {
    val counter = MutableLiveData<Int>()
}