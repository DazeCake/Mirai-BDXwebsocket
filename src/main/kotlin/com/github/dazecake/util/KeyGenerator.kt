package com.github.dazecake.util

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

object KeyGenerator {

    private val format = SimpleDateFormat("yyyyMMddHHmm")

    operator fun invoke(pwd: String, cmd: String): String = md5(pwd + format.format(Date()) + "@$cmd")

    private fun md5(raw: String) =
        MessageDigest.getInstance("MD5").apply { update(raw.toByteArray()) }.digest().toUHexString()


    private fun ByteArray.toUHexString(): String {
        return buildString(size * 2) {
            this@toUHexString.forEachIndexed { index, it ->
                if (index in 0 until size) {
                    var ret = Integer.toHexString(it.toInt() and 0xFF)
                    if (ret.length == 1) ret = "0$ret"
                    append(ret)
                }
            }
        }.toUpperCase()
    }
}