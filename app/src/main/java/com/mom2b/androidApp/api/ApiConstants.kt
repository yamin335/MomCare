package com.mom2b.androidApp.api

import com.mom2b.androidApp.api.Api.API_REPO

object Api {
    const val PROTOCOL = "http"
    const val API_ROOT = "api.mom2b.com"
    const val API_ROOT_URL = "$PROTOCOL://$API_ROOT/"

    const val API_REPO = "api"
    const val AUTH_REPO = "auth"
    const val PUBLIC_REPO = "public"

    const val ContentType = "Content-Type: application/json"
}

object ApiEndPoint {
    const val SITE_BANNERS = "$API_REPO/site/site/getSiteBanner"
}

object ResponseCodes {
    const val CODE_SUCCESS = 200
    const val CODE_TOKEN_EXPIRE = 401
    const val CODE_UNAUTHORIZED = 403
}

object ApiCallStatus {
    const val LOADING = 0
    const val SUCCESS = 1
    const val ERROR = 2
    const val EMPTY = 3
}
