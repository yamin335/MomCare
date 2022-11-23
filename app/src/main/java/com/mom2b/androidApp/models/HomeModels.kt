package com.mom2b.androidApp.models

data class SiteBannerResponse(val resultData: List<SiteBanner>?, val recordsTotal: Int?)

data class SiteBanner(val id: Int?, val image: String?, val title: String?,
                      val subtitle: String?)