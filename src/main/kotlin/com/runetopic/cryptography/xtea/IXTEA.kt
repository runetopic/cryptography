package com.runetopic.cryptography.xtea

import com.runetopic.cryptography.ICryptography

/**
 * @author Jordan Abraham
 */
internal interface IXTEA: ICryptography {
    fun getRounds(): Int
    fun getKeys(): IntArray
}