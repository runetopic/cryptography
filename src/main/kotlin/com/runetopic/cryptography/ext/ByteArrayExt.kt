package com.runetopic.cryptography.ext

import com.runetopic.cryptography.xtea.XTEA

/**
 * @author Jordan Abraham
 */
fun ByteArray.fromXTEA(rounds: Int, keys: IntArray = IntArray(4)) = XTEA(rounds, keys).decrypt(this)
fun ByteArray.toXTEA(rounds: Int, keys: IntArray = IntArray(4)) = XTEA(rounds, keys).encrypt(this)

internal fun ByteArray.g4(offset: Int): Int {
    return ((this[offset].toInt() and 0xFF shl 24)
            + (this[offset + 1].toInt() and 0xFF shl 16)
            + (this[offset + 2].toInt() and 0xFF shl 8)
            + (this[offset + 3].toInt() and 0xFF))
}

internal fun ByteArray.p4(offset: Int, int: Int) {
    this[offset] = (int shr 24).toByte()
    this[offset + 1] = (int shr 16).toByte()
    this[offset + 2] = (int shr 8).toByte()
    this[offset + 3] = int.toByte()
}