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
                values.shift(mask, size - 1)
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

    tailrec fun compress(
        input: ByteArray,
        output: ByteArray,
        key: Int = 0,
        position: Int = 0,
        index: Int = 0
    ): Int {
        if (index == input.size) return 7 + position shr 3
        val byte = input[index].toInt() and 0xFF
        val size = sizes[byte]
        if (size.toInt() == 0) throw RuntimeException("Size is equal to zero for Data = $byte")
        val remainder = position and 7
        val offset = position shr 3
        val nextKey = output.calculateNextKey(
            inverse = (-1 + (remainder - -size) shr 3) + offset,
            remainder = remainder + 24,
            mask = masks[byte],
            startKey = key and (-remainder shr 31),
            offset = offset
        )
        return compress(input, output, nextKey, position + size.toInt(), index + 1)
    }

    tailrec fun decompress(
        input: ByteArray,
        output: ByteArray,
        limit: Int,
        currentWritePosition: Int = 0,
        currentReadPosition: Int = 0,
        index: Int = 0
    ): Int {
        if (limit == 0) return 0
        val byte = input[index].toInt()

        var writePosition = currentWritePosition
        var readPosition = currentReadPosition

        repeat(8) { x ->
            readPosition = output.putKey(byte, if (x == 0) -1 else 64 shr (x - 1), readPosition, writePosition).also {
                if (it == 0 && ++writePosition >= limit) return index + 1
            }
        }
        return decompress(input, output, limit, writePosition, readPosition, index + 1)
    }

    private fun ByteArray.putKey(
        byte: Int,
        mask: Int,
        readPosition: Int,
        writePosition: Int
    ): Int {
        val nextReadPosition = when {
            mask == -1 && byte < 0 -> keys[readPosition]
            mask != -1 && byte and mask != 0 -> keys[readPosition]
            else -> readPosition + 1
        }
        val key = keys[nextReadPosition]
        if (key < 0) {
            this[writePosition] = key.inv().toByte()
            return 0
        }
        return nextReadPosition
    }

    private tailrec fun ByteArray.calculateNextKey(
        inverse: Int,
        remainder: Int,
        mask: Int,
        startKey: Int,
        offset: Int,
        block: Int = 0
    ): Int = when (block) {
        0 -> {
            val next = startKey or (mask ushr remainder)
            this[offset] = next.toByte()
            if (inverse.inv() >= offset.inv()) next
            else calculateNextKey(inverse, remainder - 8, mask, startKey, offset + 1, 1)
        }
        4 -> {
            val next = mask shl -remainder
            this[offset] = next.toByte()
            next
        }
        else -> {
            val next = mask ushr remainder
            this[offset] = next.toByte()
            if (block == 3 && inverse <= offset) next
            else if (offset.inv() <= inverse.inv()) next
            else calculateNextKey(inverse, remainder - 8, mask, startKey, offset + 1, block + 1)
        }
    }

    private tailrec fun IntArray.shift(mask: Int, index: Int) {
        if (index == 0) return
        val value = this[index]
        if (value != mask) return

        val x = 1 shl 32 - index
        if (value and x != 0) {
            this[index] = this[index - 1]
            return
        }

        this[index] = value or x
        return shift(mask, index - 1)
    }

    private tailrec fun IntArray.comb(mask: Int, index: Int, x: Int) {
        if (index > 32) return
        if (this[index] == mask) {
            this[index] = x
        }
        return comb(mask, index + 1, x)
    }
}
