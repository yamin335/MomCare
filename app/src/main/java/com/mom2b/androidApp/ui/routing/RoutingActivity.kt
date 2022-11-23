package com.mom2b.androidApp.ui.routing

import android.os.Bundle
import androidx.activity.viewModels
import com.mom2b.androidApp.databinding.RoutingActivityBinding
import com.mom2b.androidApp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutingActivity : BaseActivity<RoutingActivityBinding, RoutingActivityViewModel>() {
    private lateinit var binding: RoutingActivityBinding
    override val viewModel: RoutingActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RoutingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}