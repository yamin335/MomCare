package com.mom2b.androidApp.api

import com.mom2b.androidApp.models.SiteBannerResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */
interface ApiService {

    @GET(ApiEndPoint.SITE_BANNERS)
    suspend fun getSiteBanner(@Query("param") param: JsonObject): Response<SiteBannerResponse>

}
