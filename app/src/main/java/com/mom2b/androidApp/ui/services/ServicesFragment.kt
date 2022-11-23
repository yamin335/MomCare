package com.mom2b.androidApp.ui.services

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mom2b.androidApp.BR
import com.mom2b.androidApp.R
import com.mom2b.androidApp.databinding.ServicesFragmentBinding
import com.mom2b.androidApp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : BaseFragment<ServicesFragmentBinding, ServicesViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_services
    override val br: Int
        get() = BR.viewModel
    override val viewModel: ServicesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}