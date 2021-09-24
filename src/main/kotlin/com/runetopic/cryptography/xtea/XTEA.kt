package com.runetopic.cryptography.xtea

/**
 * @author Jordan Abraham
 */
class XTEA(
    private val keys: IntArray = IntArray(4),
    private val cycles: Int
): IXTEA {
    override fun getKeys(): IntArray = keys
    override fun getCycles(): Int = cycles

    override fun decrypt(src: ByteArray) {
        (src.indices step Int.SIZE_BYTES * 2).forEach { offset ->
            var v0 = (src[offset].toInt() and 0xFF shl 24
                    or (src[offset + 1].toInt() and 0xFF shl 16)
                    or (src[offset + 2].toInt() and 0xFF shl 8)
                    or (src[offset + 3].toInt() and 0xFF))
            var v1 = (src[offset + 4].toInt() and 0xFF shl 24
                    or (src[offset + 5].toInt() and 0xFF shl 16)
                    or (src[offset + 6].toInt() and 0xFF shl 8)
                    or (src[offset + 7].toInt() and 0xFF))
            var sum = (DELTA * cycles)
            (0 until cycles).forEach { _ ->
                v1 -= (v0 shl 4 xor (v0 ushr 5)) + v0 xor sum + keys[sum ushr 11 and 3]
                sum -= DELTA
                v0 -= (v1 shl 4 xor (v1 ushr 5)) + v1 xor sum + keys[sum and 3]
            }
            src[offset] = (v0 ushr 24).toByte()
            src[offset + 1] = (v0 ushr 16).toByte()
            src[offset + 2] = (v0 ushr 8).toByte()
            src[offset + 3] = v0.toByte()
            src[offset + 4] = (v1 ushr 24).toByte()
            src[offset + 5] = (v1 ushr 16).toByte()
            src[offset + 6] = (v1 ushr 8).toByte()
            src[offset + 7] = v1.toByte()
        }
    }

    override fun encrypt(src: ByteArray) {
        TODO("Not yet implemented")
    }

    internal companion object {
        const val DELTA = 0x61C88647
    }
}