package com.mom2b.androidApp.ui

import android.content.Context
import com.mom2b.androidApp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AntenatalCareFragmentViewModel @Inject constructor(
    @ApplicationContext private val application: Context
) : BaseViewModel(application) {
}