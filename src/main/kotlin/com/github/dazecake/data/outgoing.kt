package com.github.dazecake.data

import kotlinx.serialization.Serializable


@Serializable
data class Outgoing(
    val op: String = "runcmd",
    val passwd: String,
    val cmd: String
)