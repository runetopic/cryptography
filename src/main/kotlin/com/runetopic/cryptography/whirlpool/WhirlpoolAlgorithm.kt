package com.runetopic.cryptography.whirlpool

import com.runetopic.cryptography.Algorithm
import com.runetopic.cryptography.downUntil
import com.runetopic.cryptography.g8
import com.runetopic.cryptography.p8

/**
 * @author Jordan Abraham
 */
internal interface WhirlpoolAlgorithm : Algorithm<ByteArray, ByteArray, ByteArray> {
    fun getRounds(): Int
    fun getSize(): Int
    fun getBlock(): Array<LongArray>
    fun getBlockRounds(): LongArray
    fun getHash(): LongArray
    fun getBuffer(): ByteArray
    fun getBitSize(): ByteArray
    fun getPosition(): Int
    fun setPosition(position: Int)
    fun getDigestBits(): Int
    fun setDigestBits(digestBits: Int)

    fun maskWithReductionPolynomial(value: Long): Long = if (value >= 256) (value xor 0x11D) else value

    fun hash(src: ByteArray, size: Int): ByteArray {
        addThenFinalizeNEESIE(src, (src.size * 8).toLong())
        return ByteArray(size).apply {
            repeat(8) { p8(it * 8, getHash()[it]) }
        }
    }

    private fun addThenFinalizeNEESIE(src: ByteArray, bits: Long) {
        calculateBitSize(bits)
        val space = (8 - (bits.toInt() and 7)) and 7
        val occupied = getDigestBits() and 7

        var position = 0
        var byte: Int
        (bits.toInt() downUntil 8 step 8).forEach { _ ->
            byte = (((src[position].toInt() shl space) and 0xFF) or ((src[position + 1].toInt() and 0xFF) ushr (8 - space)))
            getBuffer()[getPosition()] = (getBuffer()[getPosition()].toInt() or (byte ushr occupied)).toByte()
            shift(occupied, byte)
            addDigestBits(occupied)
            position++
        }

        val leftover = bits - position * 8
        val leftoverByte = if (leftover > 0) (src[position].toInt() shl space) and 0xFF else 0
        if (leftover > 0) {
            getBuffer()[getPosition()] = (getBuffer()[getPosition()].toInt() or (leftoverByte ushr occupied)).toByte()
        }
        if (occupied + leftover >= 8) {
            shift(occupied, leftoverByte)
        }
        addDigestBits(leftover.toInt())
        finalize()
    }

    private tailrec fun calculateBitSize(
        value: Long,
        offset: Int = 32,
        carry: Int = 0
    ) {
        if (offset == 0) return
        val shift = carry + (getBitSize()[offset - 1].toInt() and 0xFF) + (value.toInt() and 0xFF)
        getBitSize()[offset - 1] = shift.toByte()
        return calculateBitSize(
            value = value ushr 8,
            offset = offset - 1,
            carry = shift ushr 8
        )
    }

    private fun finalize() {
        getBuffer()[getPosition()] = (getBuffer()[getPosition()].toInt() or (0x80 ushr (getDigestBits() and 7))).toByte()
        incrementPosition()
        if (getPosition() > 32) {
            fill(64)
            transform()
            setPosition(0)
        }
        fill(32)
        getBitSize().copyInto(getBuffer(), 32, 0, 32)
        transform()
    }

    private tailrec fun fill(limit: Int) {
        if (getPosition() >= limit) return
        getBuffer()[incrementPositionAndReturn()] = 0
        return fill(limit)
    }

    private fun transform() {
        val block = LongArray(8)
        val state = LongArray(8)
        val roundKey = LongArray(8)
        val result = LongArray(8)

        repeat(8) {
            block[it] = getBuffer().g8(it * 8)
            roundKey[it] = getHash()[it]
            state[it] = block[it] xor roundKey[it]
        }

        for (round in 1..getRounds()) {
            repeat(8) {
                result[it] = 0
                repeat(8) { index ->
                    val offset = 56 - (8 * index)
                    result[it] = result[it] xor getBlock()[index][(roundKey[(it - index) and 7] ushr offset).toInt() and 0xFF]
                }
            }
            result.copyInto(roundKey)
            roundKey[0] = roundKey[0] xor getBlockRounds()[round]
            repeat(8) {
                result[it] = roundKey[it]
                repeat(8) { index ->
                    val offset = 56 - (8 * index)
                    result[it] = result[it] xor getBlock()[index][(state[(it - index) and 7] ushr offset).toInt() and 0xFF]
                }
            }
            result.copyInto(state)
        }

        repeat(8) {
            getHash()[it] = getHash()[it] xor state[it] xor block[it]
        }
    }

    private fun shift(offset: Int, int: Int) {
        incrementPosition()
        addDigestBits(8 - offset)
        if (getDigestBits() == 512) {
            transform()
            setDigestBits(0)
            setPosition(0)
        }
        getBuffer()[getPosition()] = ((int shl (8 - offset)) and 0xFF).toByte()
    }

    private fun addDigestBits(amount: Int) {
        setDigestBits(getDigestBits() + amount)
    }

    private fun incrementPosition() {
        setPosition(getPosition() + 1)
    }

    private fun incrementPositionAndReturn(): Int {
        val position = getPosition()
        setPosition(position + 1)
        return position
    }
}
