package com.volvadvit.messenger.services

import java.util.*

fun String.toBase64() : String {
    return Base64.getEncoder().encode(this.encodeToByteArray()).toString()
}

fun String.fromBase64() : String {
    return Base64.getDecoder().decode(this).toString()
}