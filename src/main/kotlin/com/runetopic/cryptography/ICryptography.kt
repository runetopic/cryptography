package com.runetopic.cryptography

/**
 * @author Jordan Abraham
 */
internal interface ICryptography {
    fun from(src: ByteArray): ByteArray
    fun to(src: ByteArray): ByteArray
}