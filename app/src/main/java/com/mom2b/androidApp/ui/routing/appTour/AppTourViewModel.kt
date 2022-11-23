package com.mom2b.androidApp.ui.routing.appTour

import android.content.Context
import com.mom2b.androidApp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AppTourViewModel @Inject constructor(
    @ApplicationContext private val application: Context
) : BaseViewModel(application) {
}