package com.mom2b.androidApp.ui.routing.appTour

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.mom2b.androidApp.BR
import com.mom2b.androidApp.R
import com.mom2b.androidApp.databinding.AppTourFragmentBinding
import com.mom2b.androidApp.models.AppTourModel
import com.mom2b.androidApp.ui.base.BaseFragment
import com.mom2b.androidApp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppTourFragment : BaseFragment<AppTourFragmentBinding, AppTourViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_app_tour
    override val br: Int
        get() = BR.viewModel
    override val viewModel: AppTourViewModel by viewModels()

    private lateinit var appTourSlides: ArrayList<AppTourModel>
    private lateinit var appTourSliderAdapter: AppTourSliderAdapter
    private lateinit var appTourSliderIndicatorAdapter: AppTourSliderIndicatorAdapter
    private lateinit var sliderPageChangeCallback: SliderPageChangeCallback
    private var noOfSlides = 0
    private var sliderCurrentPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appTourSlides = arrayListOf(
            AppTourModel(getString(R.string.sliderTitle1), getString(R.string.sliderDescription1), R.drawable.img_baby),
            AppTourModel(getString(R.string.sliderTitle2), getString(R.string.sliderDescription2), R.drawable.img_pregnant_mother),
            AppTourModel(getString(R.string.sliderTitle3), getString(R.string.sliderDescription3), R.drawable.img_mother_and_baby)
        )

        noOfSlides = appTourSlides.size

        appTourSliderIndicatorAdapter = AppTourSliderIndicatorAdapter(noOfSlides)
        binding.indicatorView.adapter = appTourSliderIndicatorAdapter

        sliderPageChangeCallback = SliderPageChangeCallback {
            appTourSliderIndicatorAdapter.setIndicatorAt(it)
            sliderCurrentPosition = it
            if (sliderCurrentPosition + 1 == noOfSlides) {
                binding.skipButton.visibility = View.INVISIBLE
                binding.btnNext.text = "Done"
            } else {
                binding.skipButton.visibility = View.VISIBLE
                binding.btnNext.text = "Next"
            }
        }

        appTourSliderAdapter = AppTourSliderAdapter()
        binding.sliderView.apply {
            adapter = appTourSliderAdapter
            registerOnPageChangeCallback(sliderPageChangeCallback)
            isUserInputEnabled = true
        }
        appTourSliderAdapter.submitList(appTourSlides)

        binding.skipButton.setOnClickListener {
            finishTour()
        }

        binding.btnNext.setOnClickListener {
            if (++sliderCurrentPosition < noOfSlides) {
                binding.sliderView.setCurrentItem(sliderCurrentPosition, true)
                if (sliderCurrentPosition + 1 == noOfSlides) {
                    binding.skipButton.visibility = View.INVISIBLE
                    binding.btnNext.text = "Done"
                }
            } else {
                finishTour()
            }
        }
    }

    private fun finishTour() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        requireActivity().finish()
    }

    inner class SliderPageChangeCallback(private val listener: (Int) -> Unit) : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            listener.invoke(position)
        }
    }
}