package com.runetopic.cryptography

/**
 * @author Jordan Abraham
 */
internal interface ICryptography {
    fun decrypt(src: ByteArray): ByteArray
    fun encrypt(src: ByteArray): ByteArray
}