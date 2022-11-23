package com.mom2b.androidApp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mom2b.androidApp.BR
import com.mom2b.androidApp.R
import com.mom2b.androidApp.databinding.AntenatalCareFragmentBinding
import com.mom2b.androidApp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AntenatalCareFragment : BaseFragment<AntenatalCareFragmentBinding, AntenatalCareFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_antenatal_care
    override val br: Int
        get() = BR.viewModel
    override val viewModel: AntenatalCareFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}