package com.lanars.compose_easy_route.generator.model

data class DeepLink(
    val uriPattern: String,
    val action: String,
    val mimeType: String,
)
