package com.gsrocks.compose_easy_route.generator.model

data class FunctionParameter(
    val name: String,
    val type: ParamType,
    val hasDefault: Boolean,
    val defaultValue: DefaultValue? = null
)
