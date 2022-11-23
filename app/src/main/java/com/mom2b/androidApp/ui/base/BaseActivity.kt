package com.mom2b.androidApp.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity() {
    /**
     * Override for set view model
     *
     * @return view data binding instance
     */
    //abstract val binding: T

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V
}