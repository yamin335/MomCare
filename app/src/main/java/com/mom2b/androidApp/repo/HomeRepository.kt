package com.mom2b.androidApp.repo

import com.mom2b.androidApp.api.ApiService
import com.mom2b.androidApp.models.SiteBannerResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val apiService: ApiService
    ) {

    suspend fun getSiteBanners(pageNumber: Int, pageSize: Int): Response<SiteBannerResponse> {
        val param = JsonObject().apply {
            addProperty("pageNumber", pageNumber)
            addProperty("pageSize", pageSize)
        }

        return withContext(Dispatchers.IO) {
            apiService.getSiteBanner(param)
        }
    }
}