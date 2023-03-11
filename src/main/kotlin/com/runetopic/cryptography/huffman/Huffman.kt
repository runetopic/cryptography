package com.runetopic.cryptography.huffman

class Huffman(
    private val sizes: ByteArray
) {
    private var masks: IntArray = IntArray(sizes.size)
    private var keys: IntArray = IntArray(8)

    init {
        val values = IntArray(33)
        var currIndex = 0

        repeat(sizes.size) {
            val size = sizes[it]
            if (size.toInt() == 0) return@repeat
            val x = 1 shl 32 - size
            val mask = values[size.toInt()]
            masks[it] = mask
            val value = if (mask and x != 0) {
                values[size - 1]
            } else {
                values.shiftUp(mask, size - 1)
                mask or x
            }
            values[size.toInt()] = value
            values.comb(mask, size + 1, value)
            var keyIndex = 0
            repeat(size.toInt()) { i ->
                if (mask and Integer.MIN_VALUE.ushr(i) != 0) {
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
            keys[keyIndex] = it.inv()
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

    tailrec fun compress(
        input: ByteArray,
        output: ByteArray,
        key: Int = 0,
        bitPosition: Int = 0,
        curr: Int = 0
    ): Int {
        if (curr == input.size) return 7 + bitPosition shr 3
        val unsignedByte = input[curr].toInt() and 0xFF
        val size = sizes[unsignedByte]
        if (size.toInt() == 0) throw RuntimeException("Size is equal to zero for Data = $unsignedByte")
        val remainder = bitPosition and 7
        val offset = bitPosition shr 3
        val nextKey = output.calculateNextKey((-1 + (remainder - -size) shr 3) + offset, remainder + 24, masks[unsignedByte], key and (-remainder shr 31), offset)
        return compress(input, output, nextKey, bitPosition + size.toInt(), curr + 1)
    }

    private tailrec fun ByteArray.calculateNextKey(
        inverse: Int,
        remainder: Int,
        mask: Int,
        startKey: Int,
        offset: Int,
        curr: Int = 0
    ): Int = when (curr) {
        0 -> {
            val next = startKey or (mask ushr remainder)
            this[offset] = next.toByte()
            if (inverse.inv() >= offset.inv()) next
            else calculateNextKey(inverse, remainder - 8, mask, startKey, offset, 1)
        }
        4 -> {
            val next = mask shl -remainder
            this[offset + 1] = next.toByte()
            next
        }
        else -> {
            val next = mask ushr remainder
            this[offset + 1] = next.toByte()
            if (curr == 3 && inverse <= offset + 1) next
            else if ((offset + 1).inv() <= inverse.inv()) next
            else calculateNextKey(inverse, remainder - 8, mask, startKey, offset + 1, curr + 1)
        }
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
        val compressedByte = compressed[curr].toInt()

        var decompressedIndex = currDecompressedIndex
        var keyIndex = currKeyIndex

        intArrayOf(-1, 0x40, 0x20, 0x10, 0x8, 0x4, 0x2, 0x1).forEach { mask ->
            keyIndex = decompressed.checkInverseAndApplyNextKey(compressedByte, mask, keyIndex, decompressedIndex).also {
                if (it == 0) {
                    decompressedIndex++
                    if (decompressedIndex >= decompressedLength) return curr + 1
                }
            }
        }
        return decompress(compressed, decompressed, decompressedLength, decompressedIndex, keyIndex, curr + 1)
    }

    private fun ByteArray.checkInverseAndApplyNextKey(
        compressedByte: Int,
        mask: Int,
        keyIndex: Int,
        decompressedIndex: Int
    ): Int {
        val nextIndex = when {
            mask == -1 && compressedByte < 0 -> keys[keyIndex]
            mask != -1 && compressedByte and mask != 0 -> keys[keyIndex]
            else -> keyIndex + 1
        }
        val key = keys[nextIndex]
        if (key < 0) {
            this[decompressedIndex] = key.inv().toByte()
            return 0
        }
        return nextIndex
    }
}
