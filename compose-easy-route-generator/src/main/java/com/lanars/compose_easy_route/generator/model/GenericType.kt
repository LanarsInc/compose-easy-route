package com.lanars.compose_easy_route.generator.model

data class GenericType(
    val simpleName: String,
    val qualifiedName: String,
    val isSerializable: Boolean,
    val isParcelable: Boolean,
    val isNullable: Boolean
)
