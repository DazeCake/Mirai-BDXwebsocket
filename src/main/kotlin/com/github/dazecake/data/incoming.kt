package com.github.dazecake.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed class Incoming

@Serializable
@SerialName("onmsg")
data class MemberMessage(
    val target: String,
    val text: String
) : Incoming()

@Serializable
@SerialName("onjoin")
data class MemberJoin(
    val target: String,
    val text: String
) : Incoming()

@Serializable
@SerialName("onleft")
data class MemberLeave(
    val target: String,
    val text: String
) : Incoming()

@Serializable
@SerialName("onCMD")
data class MemberCmd(
    val target: String,
    val CMD: String
) : Incoming()

@Serializable
@SerialName("runcmd")
data class CmdResp(
    val Auth: String = "",
    val feedback: String = "",
    val onError: String? = null,
    val text: String = ""
) : Incoming()