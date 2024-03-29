@file:JvmName("CryptoExtensions")

package com.runetopic.cryptography

import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.cryptography.huffman.HuffmanInput
import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.cryptography.whirlpool.Whirlpool
import com.runetopic.cryptography.xtea.XTEA
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */

/**
 * @see Huffman.from(HuffmanInput)
 */
fun ByteArray.decompressHuffman(huffman: Huffman, size: Int): ByteArray = huffman.from(HuffmanInput(this, size))

/**
 * @see Huffman.to(HuffmanInput)
 */
fun ByteArray.compressHuffman(huffman: Huffman): ByteArray = huffman.to(HuffmanInput(this, -1))

/**
 * @see XTEA.from(ByteArray).
 */
fun ByteArray.fromXTEA(rounds: Int, keys: IntArray = IntArray(4)): ByteBuffer = XTEA(rounds, keys).from(this)

/**
 * @see XTEA.to(ByteArray).
 */
fun ByteArray.toXTEA(rounds: Int, keys: IntArray = IntArray(4)): ByteBuffer = XTEA(rounds, keys).to(this)

fun ByteArray.toWhirlpool(rounds: Int = 10, size: Int = 64): ByteArray = Whirlpool(rounds, size).to(this)

fun IntArray.toISAAC() = ISAAC().to(this)

internal infix fun Int.downUntil(to: Int): IntProgression {
    if (to >= Int.MAX_VALUE) return IntRange.EMPTY
    return this downTo (to + 1)
}

internal fun ByteArray.g8(offset: Int): Long {
    return (
        /*******/(this[offset].toLong() and 0xFF shl 56)
            or ((this[offset + 1].toLong() and 0xFF) shl 48)
            or ((this[offset + 2].toLong() and 0xFF) shl 40)
            or ((this[offset + 3].toLong() and 0xFF) shl 32)
            or ((this[offset + 4].toLong() and 0xFF) shl 24)
            or ((this[offset + 5].toLong() and 0xFF) shl 16)
            or ((this[offset + 6].toLong() and 0xFF) shl 8)
            or (this[offset + 7].toLong() and 0xFF)
        )
}

internal fun ByteArray.p8(offset: Int, long: Long) {
    this[offset/**/] = (long shr 56).toByte()
    this[offset + 1] = (long shr 48).toByte()
    this[offset + 2] = (long shr 40).toByte()
    this[offset + 3] = (long shr 32).toByte()
    this[offset + 4] = (long shr 24).toByte()
    this[offset + 5] = (long shr 16).toByte()
    this[offset + 6] = (long shr 8).toByte()
    this[offset + 7] = long.toByte()
}
