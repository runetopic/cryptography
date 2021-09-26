package com.runetopic.cryptography.ext

import com.runetopic.cryptography.whirlpool.Whirlpool
import com.runetopic.cryptography.xtea.XTEA

/**
 * @author Jordan Abraham
 */
fun ByteArray.fromXTEA(rounds: Int, keys: IntArray = IntArray(4)): ByteArray = XTEA(rounds, keys).from(this)
fun ByteArray.toXTEA(rounds: Int, keys: IntArray = IntArray(4)): ByteArray = XTEA(rounds, keys).to(this)

fun ByteArray.toWhirlpool(rounds: Int = 10, size: Int = 64): ByteArray = Whirlpool(rounds, size).to(this)

internal fun ByteArray.g4(offset: Int): Int {
    return (/*****/(this[offset].toInt() and 0xFF shl 24)
            or (this[offset + 1].toInt() and 0xFF shl 16)
            or (this[offset + 2].toInt() and 0xFF shl 8)
            or (this[offset + 3].toInt() and 0xFF))
}

internal fun ByteArray.g8(offset: Int): Long {
    return (/*******/(this[offset].toLong() and 0xFF shl 56)
            or ((this[offset + 1].toLong() and 0xFF) shl 48)
            or ((this[offset + 2].toLong() and 0xFF) shl 40)
            or ((this[offset + 3].toLong() and 0xFF) shl 32)
            or ((this[offset + 4].toLong() and 0xFF) shl 24)
            or ((this[offset + 5].toLong() and 0xFF) shl 16)
            or ((this[offset + 6].toLong() and 0xFF) shl 8)
            or (this[offset + 7].toLong() and 0xFF))
}

internal fun ByteArray.p4(offset: Int, int: Int) {
    this[offset/**/] = (int shr 24).toByte()
    this[offset + 1] = (int shr 16).toByte()
    this[offset + 2] = (int shr 8).toByte()
    this[offset + 3] = int.toByte()
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