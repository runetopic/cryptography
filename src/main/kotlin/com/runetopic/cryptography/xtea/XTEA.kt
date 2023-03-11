package com.runetopic.cryptography.xtea

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
internal class XTEA(
    private val rounds: Int,
    private val keys: IntArray
) : XTEAAlgorithm {
    /**
     * Decrypt from a ByteArray to a ByteBuffer.
     * The src ByteArray size does not have to be in multiple of 8 for XTEA.
     * 8 bytes at a time are decrypted and the rest are left alone at the tail end, if applicable.
     * The position of the created ByteBuffer returned is 0 and its capacity is equal to the input src ByteArray.
     */
    override fun from(src: ByteArray): ByteBuffer = ByteBuffer.wrap(src)
        .apply { repeat(src.size / 8) { decrypt() } }
        .position(0)

    private tailrec fun ByteBuffer.decrypt(
        v0: Int = int,
        v1: Int = int,
        sum: Int = -0x61C88647 * rounds,
        curr: Int = 0
    ) {
        if (applyBlock(v0, v1, curr)) return
        val one = v1 - ((v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3])
        val offset = sum - -0x61C88647
        val zero = v0 - ((one shl 4 xor (one ushr 5)) + one xor offset + keys[offset and 3])
        return decrypt(
            v0 = zero,
            v1 = one,
            sum = offset,
            curr = curr + 1
        )
    }

    /**
     * Encrypt from a ByteArray to a ByteBuffer.
     * The src ByteArray size does not have to be in multiple of 8 for XTEA.
     * 8 bytes at a time are encrypted and the rest are left alone at the tail end, if applicable.
     * The position of the created ByteBuffer returned is 0 and its capacity is equal to the input src ByteArray.
     */
    override fun to(src: ByteArray): ByteBuffer = ByteBuffer.wrap(src)
        .apply { repeat(src.size / 8) { encrypt() } }
        .position(0)

    private tailrec fun ByteBuffer.encrypt(
        v0: Int = int,
        v1: Int = int,
        sum: Int = 0,
        curr: Int = 0
    ) {
        if (applyBlock(v0, v1, curr)) return
        val zero = v0 + ((v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3])
        val offset = sum + -0x61C88647
        val one = v1 + ((zero shl 4 xor (zero ushr 5)) + zero xor offset + keys[offset ushr 11 and 3])
        return encrypt(
            v0 = zero,
            v1 = one,
            sum = offset,
            curr = curr + 1
        )
    }

    private fun ByteBuffer.applyBlock(v0: Int, v1: Int, value: Int): Boolean {
        if (value != rounds) return false
        position(position() - 8)
        putInt(v0)
        putInt(v1)
        return true
    }

    override fun getRounds(): Int = rounds
    override fun getKeys(): IntArray = keys
}
