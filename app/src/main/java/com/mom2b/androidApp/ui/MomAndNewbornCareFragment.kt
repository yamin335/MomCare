package com.mom2b.androidApp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mom2b.androidApp.BR
import com.mom2b.androidApp.R
import com.mom2b.androidApp.databinding.MomAndNewbornCareFragmentBinding
import com.mom2b.androidApp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MomAndNewbornCareFragment : BaseFragment<MomAndNewbornCareFragmentBinding, MomAndNewbornCareFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_mom_and_newborn_care
    override val br: Int
        get() = BR.viewModel
    override val viewModel: MomAndNewbornCareFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}