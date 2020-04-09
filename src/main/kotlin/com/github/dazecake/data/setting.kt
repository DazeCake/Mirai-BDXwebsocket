package com.github.dazecake.data

data class ServerInfo(
    val host: String,
    val port: Int,
    val path: String,
    val basePwd: String,
    val retryTime: Int
)