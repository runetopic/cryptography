package com.runetopic.cryptography.xtea

import com.runetopic.cryptography.ICryptography

/**
 * @author Jordan Abraham
 */
internal interface IXTEA: ICryptography<ByteArray, ByteArray> {
    fun getRounds(): Int
    fun getKeys(): IntArray
}