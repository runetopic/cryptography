package com.runetopic.cryptography.xtea

import com.runetopic.cryptography.Algorithm
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
internal interface XTEAAlgorithm : Algorithm<ByteArray, ByteBuffer> {
    fun getRounds(): Int
    fun getKeys(): IntArray
}
