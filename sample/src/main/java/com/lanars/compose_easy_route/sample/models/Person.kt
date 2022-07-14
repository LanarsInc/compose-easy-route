package com.lanars.compose_easy_route.sample.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Person(val name: String, val age: Int) : Parcelable
