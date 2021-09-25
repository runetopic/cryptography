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

    override fun decrypt(src: ByteArray): ByteArray {
        val size = Int.SIZE_BYTES * 2
        check(src.size % size == 0)
        val decrypted = src.copyOf()
        (decrypted.indices step size).forEach {
            var v0 = decrypted.g4(0 + it)
            var v1 = decrypted.g4(4 + it)
            var sum = (DELTA * rounds)
            (0 until rounds).forEach { _ ->
                v1 -= (v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3]
                sum -= DELTA
                v0 -= (v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3]
            }
            decrypted.p4(0 + it, v0)
            decrypted.p4(4 + it, v1)
        }
        return decrypted
    }

    override fun encrypt(src: ByteArray): ByteArray {
        val size = Int.SIZE_BYTES * 2
        check(src.size % size == 0)
        val encrypted = src.copyOf()
        (encrypted.indices step size).forEach {
            var v0 = encrypted.g4(0 + it)
            var v1 = encrypted.g4(4 + it)
            var sum = 0
            (0 until rounds).forEach { _ ->
                v0 += (v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3]
                sum += DELTA
                v1 += (v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3]
            }
            encrypted.p4(0 + it, v0)
            encrypted.p4(4 + it, v1)
        }
        return encrypted
    }

    internal companion object {
        const val DELTA = 0x61C88647
    }
}