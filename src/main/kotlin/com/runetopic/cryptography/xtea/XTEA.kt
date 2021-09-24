package com.runetopic.cryptography.xtea

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class XTEA(
    private val rounds: Int,
    private val keys: IntArray = IntArray(4)
): IXTEA {
    override fun getRounds(): Int = rounds
    override fun getKeys(): IntArray = keys

    override fun decrypt(src: ByteArray): ByteArray {
        val amount = Int.SIZE_BYTES * 2
        if (src.size % amount != 0) throw IllegalArgumentException("The src array size must be a multiple of $amount")
        val buffer = ByteBuffer.wrap(src)
        val decrypted = ByteBuffer.allocate(src.size)
        (0 until src.size / amount).forEach { _ ->
            var v0 = buffer.int
            var v1 = buffer.int
            var sum = (DELTA * rounds)
            (0 until rounds).forEach { _ ->
                v1 -= (v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3]
                sum -= DELTA
                v0 -= (v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3]
            }
            decrypted.putInt(v0)
            decrypted.putInt(v1)
        }
        return decrypted.put(buffer).array()
    }

    override fun encrypt(src: ByteArray): ByteArray {
        val amount = Int.SIZE_BYTES * 2
        if (src.size % amount != 0) throw IllegalArgumentException("The src array size must be a multiple of $amount")
        val buffer = ByteBuffer.wrap(src)
        val encrypted = ByteBuffer.allocate(src.size)
        (src.indices step amount).forEach { _ ->
            var v0 = buffer.int
            var v1 = buffer.int
            var sum = 0
            (0 until rounds).forEach { _ ->
                v0 += (v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3]
                sum += DELTA
                v1 += (v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3]
            }
            encrypted.putInt(v0)
            encrypted.putInt(v1)
        }
        return encrypted.put(buffer).array()
    }

    internal companion object {
        const val DELTA = 0x61C88647
    }
}