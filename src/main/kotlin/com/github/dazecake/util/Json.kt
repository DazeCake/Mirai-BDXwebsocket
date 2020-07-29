package com.github.dazecake.util

import com.github.dazecake.data.Outgoing
import kotlinx.serialization.StringFormat
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.getContextualOrDefault

@UnstableDefault
object BDXJson {

    val json = Json {

        isLenient = true
        ignoreUnknownKeys = true
        classDiscriminator = "operate"
    }

    fun stringifyOuting(value: Outgoing): String = json.stringify(Outgoing.serializer(), value)
}