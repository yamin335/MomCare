package com.mom2b.androidApp.ui.home

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mom2b.androidApp.BR
import com.mom2b.androidApp.R
import com.mom2b.androidApp.application.AppExecutors
import com.mom2b.androidApp.databinding.HomeFragmentBinding
import com.mom2b.androidApp.models.SiteBanner
import com.mom2b.androidApp.ui.base.BaseFragment
import com.mom2b.androidApp.utils.AppConstants
import com.mom2b.androidApp.utils.AppConstants.MINIMUM_SLIDER_NUMBER
import com.mom2b.androidApp.utils.AppConstants.SLIDER_CHANGE_TIME_INTERVAL
import com.mom2b.androidApp.utils.AppConstants.SLIDER_HUMAN_INTERACTION_AWAIT_TIME
import com.mom2b.androidApp.utils.AppConstants.START_TIME_IN_MILLI_SECONDS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_home
    override val br: Int
        get() = BR.viewModel
    override val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var indicatorAdapter: SliderIndicatorAdapter
    private lateinit var sliderPageChangeCallback: SliderPageChangeCallback
    private lateinit var sliderAdapter: SliderAdapter

    private var countdownTimer: CountDownTimer? = null

    private val sliderAnimator = ViewPager2.PageTransformer { page, position ->
//        val scaleFactor = 0.8f.coerceAtLeast(1 - abs(position - 0.01f))
//        page.scaleY = scaleFactor

        page.apply {
            val MIN_SCALE = 0.85f
            val MIN_ALPHA = 0.6f

            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = MIN_ALPHA
                    val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                    scaleY = scaleFactor
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = resources.getDimensionPixelOffset(R.dimen.halfPageMargin)
                    translationX = horzMargin.toFloat()/2

//                    translationX = if (position < 0) {
//                        horzMargin - vertMargin / 2
//                    } else {
//                        horzMargin + vertMargin / 2
//                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    //scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = MIN_ALPHA
                    val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                    scaleY = scaleFactor
                }
            }
        }
    }

    var firstTime = true
    var isHumanInteracting = false
    var sliderAutoScrollAwaitJobScope = CoroutineScope(Job() + Dispatchers.Main.immediate)

    override fun onResume() {
        super.onResume()

        if (!firstTime) {
            startTimer()
        }
        firstTime = false
    }

    override fun onPause() {
        super.onPause()
        resetTimer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sliderAdapter = SliderAdapter({ banner ->

        }, {
            isHumanInteracting = true
        }) {
            viewModel.allBanners.value?.let {
                processSlides(it)
            }
        }

        sliderPageChangeCallback = SliderPageChangeCallback {
            if (isHumanInteracting) {
                resetTimer()
                sliderAutoScrollAwaitJobScope.cancel()
                sliderAutoScrollAwaitJobScope = CoroutineScope(Job() + Dispatchers.Main.immediate)
                val sliderAutoScrollAwaitJob = sliderAutoScrollAwaitJobScope.launch {
                    delay(SLIDER_HUMAN_INTERACTION_AWAIT_TIME)
                    startTimer()
                }

                lifecycleScope.launch {
                    sliderAutoScrollAwaitJob.join()
                }
            }
            indicatorAdapter.setIndicatorAt(it)
        }

        binding.sliderView.apply {
            // Set offscreen page limit to at least 1, so adjacent pages are always laid out
            offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                val padding = resources.getDimensionPixelOffset(R.dimen.halfPageMargin) +
                        resources.getDimensionPixelOffset(R.dimen.peekOffset)
                // setting padding on inner RecyclerView puts overscroll effect in the right place
                setPadding(padding, 0, padding, 0)
                clipToPadding = false
            }
            adapter = sliderAdapter
            registerOnPageChangeCallback(sliderPageChangeCallback)
            setPageTransformer(sliderAnimator)
        }

        indicatorAdapter = SliderIndicatorAdapter(0) { itemPosition ->
            binding.sliderView.post {
                if (sliderAdapter.getActualItemCount() >= MINIMUM_SLIDER_NUMBER)  {
                    val currentPosition = binding.sliderView.currentItem
                    val factor = currentPosition / sliderAdapter.getActualItemCount()
                    val nextPosition = (factor * sliderAdapter.getActualItemCount()) + itemPosition
                    binding.sliderView.setCurrentItem(nextPosition, true)
                } else {
                    binding.sliderView.setCurrentItem(itemPosition, true)
                }
            }
        }
        binding.indicatorView.adapter = indicatorAdapter

        viewModel.allBanners.observe(viewLifecycleOwner) {
            it?.let { banners ->
                processSlides(banners)
                lifecycleScope.launch {
                    delay(SLIDER_CHANGE_TIME_INTERVAL)
                    startTimer()
                }
            }
        }

        viewModel.allBanners.postValue(listOf(
            SiteBanner(0, "https://media.istockphoto.com/id/1285606275/vector/cesarean-section-set.jpg?s=612x612&w=is&k=20&c=AxgnvaI7NVeojF6tWyaIui_fNJYjLjNnPcldb9Ikmd4=", "Caesarean Delivery Problem", "Now-a-days 90% delivery are being Caesarean"),
            SiteBanner(0, "https://media.istockphoto.com/id/1425147156/vector/cesarean-section.jpg?s=612x612&w=is&k=20&c=_ZbT1BlA0U8Drs4V7sAmr5wB6i6lE0PaASg3X0FlQpQ=", "Newborn Mortality Problem", "Approximately 43% of pneumonia-related deaths occur in age 1-11 months"),
            SiteBanner(0, "https://media.istockphoto.com/id/1042843762/vector/childbirth.jpg?s=1024x1024&w=is&k=20&c=jgKrGW8mo85mLByl_zsc7CCOVj1SgR3oV3FHIDnsfAA=", "Unskilled Birth Attendant", "The major complications can occur due to unskilled attendants.")
        ))

        binding.cardMom.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMomAndNewbornCareFragment())
        }

        binding.cardAntenatal.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAntenatalCareFragment())
        }

        binding.cardSafe.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSafeDeliveryFragment())
        }

//        if (viewModel.allBanners.value?.isNullOrEmpty() != false) {
//            viewModel.getSiteBanners(1, 20)
//        }
    }

    private fun processSlides(banners: List<SiteBanner>) {
        binding.sliderView.post {
            sliderAdapter.submitList(banners)
            indicatorAdapter.setNumberOfSlides(banners.size)
            if (banners.size >= MINIMUM_SLIDER_NUMBER) {
                var position = banners.size * AppConstants.SLIDER_NUMBER_MULTIPLIER
                position /= 2
                binding.sliderView.setCurrentItem(position, false)
                val actualPosition = if (sliderAdapter.getActualItemCount() < MINIMUM_SLIDER_NUMBER) position else position % sliderAdapter.getActualItemCount()
                indicatorAdapter.setIndicatorAt(actualPosition)
            }
        }
    }

    private fun startTimer() {
        resetTimer()
        countdownTimer = object : CountDownTimer(START_TIME_IN_MILLI_SECONDS, SLIDER_CHANGE_TIME_INTERVAL) {
            override fun onFinish() {
                startTimer()
            }

            override fun onTick(time_in_milli_seconds: Long) {
                var currentPosition = binding.sliderView.currentItem
                binding.sliderView.post {
                    binding.sliderView.setCurrentItem(++currentPosition, true)
                }
            }
        }
        countdownTimer?.start()
    }

    private fun resetTimer() {
        isHumanInteracting = false
        countdownTimer?.cancel()
        countdownTimer = null
    }

    inner class SliderPageChangeCallback(private val listener: (Int) -> Unit) : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            val actualPosition = if (sliderAdapter.getActualItemCount() < MINIMUM_SLIDER_NUMBER) position else position % sliderAdapter.getActualItemCount()
            listener.invoke(actualPosition)
        }
    }
}