package com.runetopic.cryptography

/**
 * @author Jordan Abraham
 */
internal interface ICryptography {
    fun decrypt(src: ByteArray)
    fun encrypt(src: ByteArray)
}