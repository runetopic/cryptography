package com.runetopic.cryptography.huffman

class Huffman(
    private val sizes: ByteArray
) {
    private var masks: IntArray = IntArray(sizes.size)
    private var keys: IntArray = IntArray(8)

    init {
        val values = IntArray(33)
        var currIndex = 0

        for (index in sizes.indices) {
            val size = sizes[index]
            if (size.toInt() == 0) continue
            val x = 1 shl 32 - size
            val mask = values[size.toInt()]
            masks[index] = mask
            val value = if (mask and x != 0) {
                values[size - 1]
            } else {
                values.shiftUp(mask, size - 1)
                mask or x
            }
            values[size.toInt()] = value
            values.comb(mask, size + 1, value)
            var keyIndex = 0
            repeat(size.toInt()) {
                if (mask and Integer.MIN_VALUE.ushr(it) != 0) {
                    if (keys[keyIndex] == 0) {
                        keys[keyIndex] = currIndex
                    }
                    keyIndex = keys[keyIndex]
                } else {
                    ++keyIndex
                }

                if (keyIndex >= keys.size) {
                    val keysCopy = IntArray(keys.size * 2)
                    keys.indices.forEach { index -> keysCopy[index] = keys[index] }
                    keys = keysCopy
                }
            }
            keys[keyIndex] = index.inv()
            if (keyIndex >= currIndex) {
                currIndex = keyIndex + 1
            }
        }
    }

    private tailrec fun IntArray.shiftUp(mask: Int, index: Int) {
        if (index == 0) return
        val count = this[index]
        if (count != mask) {
            return
        }

        val x = 1 shl 32 - index
        if (count and x != 0) {
            this[index] = this[index - 1]
            return
        }

        this[index] = count or x
        return shiftUp(mask, index - 1)
    }

    private tailrec fun IntArray.comb(mask: Int, index: Int, x: Int) {
        if (index > 32) return
        if (this[index] == mask) {
            this[index] = x
        }
        return comb(mask, index + 1, x)
    }

    fun compress(text: String, output: ByteArray): Int {
        var key = 0

        val input = text.toByteArray()

        var bitpos = 0
        for (pos in text.indices) {
            val data = input[pos].toInt() and 255
            val size = this.sizes[data]
            if (size.toInt() == 0) throw RuntimeException("Size is equal to zero for Data = $data")
            val mask = masks[data]
            var remainder = bitpos and 7
            key = key and (-remainder shr 31)
            var offset = bitpos shr 3
            bitpos += size.toInt()
            val inverse = (-1 + (remainder - -size) shr 3) + offset
            remainder += 24
            key = key or mask.ushr(remainder)
            output[offset] = key.toByte()
            if (inverse.inv() < offset.inv()) {
                remainder -= 8
                key = mask.ushr(remainder)
                output[++offset] = key.toByte()
                if (offset.inv() > inverse.inv()) {
                    remainder -= 8
                    key = mask.ushr(remainder)
                    output[++offset] = key.toByte()
                    if (offset.inv() > inverse.inv()) {
                        remainder -= 8
                        key = mask.ushr(remainder)
                        output[++offset] = key.toByte()
                        if (inverse > offset) {
                            remainder -= 8
                            key = mask shl -remainder
                            output[++offset] = key.toByte()
                        }
                    }
                }
            }
        }

        return 7 + bitpos shr 3
    }

    tailrec fun decompress(
        compressed: ByteArray,
        decompressed: ByteArray,
        decompressedLength: Int,
        currDecompressedIndex: Int = 0,
        currKeyIndex: Int = 0,
        curr: Int = 0
    ): Int {
        if (decompressedLength == 0) return 0

        var decompressedIndex = currDecompressedIndex
        var keyIndex = currKeyIndex

        val compressedByte = compressed[curr]

        if (compressedByte < 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        if (compressedByte.toInt() and 0x40 != 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        if (compressedByte.toInt() and 0x20 != 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        if (compressedByte.toInt() and 0x10 != 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        if (compressedByte.toInt() and 0x8 != 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        if (compressedByte.toInt() and 0x4 != 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        if (compressedByte.toInt() and 0x2 != 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        if (compressedByte.toInt() and 0x1 != 0) {
            keyIndex = keys[keyIndex]
        } else {
            ++keyIndex
        }

        if (keys[keyIndex] < 0) {
            decompressed[decompressedIndex++] = keys[keyIndex].inv().toByte()
            if (decompressedIndex >= decompressedLength) return curr + 1
            keyIndex = 0
        }

        return decompress(
            compressed = compressed,
            decompressed = decompressed,
            decompressedLength = decompressedLength,
            currDecompressedIndex = decompressedIndex,
            currKeyIndex = keyIndex,
            curr = curr + 1
        )
    }
}
