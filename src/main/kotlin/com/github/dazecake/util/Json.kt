package com.github.dazecake.util

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

@UnstableDefault
object BDXJson {

    val json = Json {

        isLenient = true
        ignoreUnknownKeys = true
        classDiscriminator = "operate"
    }
}