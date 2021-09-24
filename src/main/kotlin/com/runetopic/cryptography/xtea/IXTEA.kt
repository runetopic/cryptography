package com.runetopic.cryptography.xtea

/**
 * @author Jordan Abraham
 */
internal interface IXTEA {
    fun decrypt(src: ByteArray)
    fun encrypt(src: ByteArray)
}