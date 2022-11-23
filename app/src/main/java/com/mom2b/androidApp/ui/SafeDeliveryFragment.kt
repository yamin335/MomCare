package com.mom2b.androidApp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mom2b.androidApp.BR
import com.mom2b.androidApp.R
import com.mom2b.androidApp.databinding.SafeDeliveryFragmentBinding
import com.mom2b.androidApp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SafeDeliveryFragment : BaseFragment<SafeDeliveryFragmentBinding, SafeAndImprovedDeliveryFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_safe_and_improved_delivery
    override val br: Int
        get() = BR.viewModel
    override val viewModel: SafeAndImprovedDeliveryFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}