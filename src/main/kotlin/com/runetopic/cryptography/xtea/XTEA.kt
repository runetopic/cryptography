package com.runetopic.cryptography.xtea

import com.runetopic.cryptography.ext.g4
import com.runetopic.cryptography.ext.p4

/**
 * @author Jordan Abraham
 */
internal class XTEA(
    private val rounds: Int,
    private val keys: IntArray = IntArray(4)
): IXTEA {
    override fun getRounds(): Int = rounds
    override fun getKeys(): IntArray = keys

    override fun decrypt(src: ByteArray) {
        val amount = Int.SIZE_BYTES * 2
        if (src.size % amount != 0) throw IllegalArgumentException("The src array size must be a multiple of $amount")
        (src.indices step amount).forEach {
            var v0 = src.g4(0 + it)
            var v1 = src.g4(4 + it)
            var sum = (DELTA * rounds)
            (0 until rounds).forEach { _ ->
                v1 -= (v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3]
                sum -= DELTA
                v0 -= (v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3]
            }
            src.p4(0 + it, v0)
            src.p4(4 + it, v1)
        }
    }

    override fun encrypt(src: ByteArray) {
        val amount = Int.SIZE_BYTES * 2
        if (src.size % amount != 0) throw IllegalArgumentException("The src array size must be a multiple of $amount")
        (src.indices step amount).forEach {
            var v0 = src.g4(0 + it)
            var v1 = src.g4(4 + it)
            var sum = 0
            (0 until rounds).forEach { _ ->
                v0 += (v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3]
                sum += DELTA
                v1 += (v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3]
            }
            src.p4(0 + it, v0)
            src.p4(4 + it, v1)
        }
    }

    internal companion object {
        const val DELTA = 0x61C88647
    }
}