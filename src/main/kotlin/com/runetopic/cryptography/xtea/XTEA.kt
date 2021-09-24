package com.runetopic.cryptography.xtea

/**
 * @author Jordan Abraham
 */
class XTEA(
    private val keys: IntArray,
    private val cycles: Int
): IXTEA {
    override fun decrypt(src: ByteArray) {
        (src.indices step Int.SIZE_BYTES * 2).forEach { _ ->
            var first = getInt(src, 0)
            var second = getInt(src, 4)
            var sum = (DELTA * cycles)
            (0 until cycles).forEach { _ ->
                second -= (first shl 4 xor (first ushr 5)) + first xor sum + keys[sum ushr 11 and 3]
                sum -= DELTA
                first -= (second shl 4 xor (second ushr 5)) + second xor sum + keys[sum and 3]
            }
            putInt(src, 0, first)
            putInt(src, 4, second)
        }
    }

    override fun encrypt(src: ByteArray) {
        TODO("Not yet implemented")
    }

    internal companion object {
        const val DELTA = 0x61C88647

        fun getInt(src: ByteArray, offset: Int): Int {
            return (src[offset].toInt() and 0xFF shl 24
                    or (src[offset + 1].toInt() and 0xFF shl 16)
                    or (src[offset + 2].toInt() and 0xFF shl 8)
                    or (src[offset + 3].toInt() and 0xFF))
        }

        fun putInt(src: ByteArray, offset: Int, int: Int) {
            src[offset] = (int ushr 24).toByte()
            src[offset + 1] = (int ushr 16).toByte()
            src[offset + 2] = (int ushr 8).toByte()
            src[offset + 3] = int.toByte()
        }
    }

}