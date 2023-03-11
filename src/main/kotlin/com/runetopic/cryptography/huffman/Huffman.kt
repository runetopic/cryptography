package com.runetopic.cryptography.huffman

class Huffman(
    private val sizes: ByteArray,
    private val limit: Int
) : HuffmanAlgorithm {
    private var encoded: IntArray = IntArray(sizes.size)
    private var decoded: IntArray = IntArray(8)

    init {
        // Sets up the encoded and decoded IntArrays.
        init(sizes.size)
    }

    private tailrec fun init(
        limit: Int,
        array: IntArray = IntArray(33),
        segment: Int = 0,
        index: Int = 0
    ) {
        if (index == limit) return
        val size = sizes[index]
        if (size.toInt() == 0) return
        val shift = 1 shl 32 - size
        val mask = array[size.toInt()]
        encoded[index] = mask
        val value = if (mask and shift != 0) {
            array[size - 1]
        } else {
            array.shift(mask, size - 1)
            mask or shift
        }
        array[size.toInt()] = value
        array.comb(mask, size + 1, value)
        return init(limit, array, initKeys(index, mask, size.toInt(), segment), index + 1)
    }

    private tailrec fun initKeys(
        sizeIndex: Int,
        mask: Int,
        limit: Int,
        currIndex: Int,
        keyIndex: Int = 0,
        index: Int = 0
    ): Int {
        if (index == limit) {
            decoded[keyIndex] = sizeIndex.inv()
            return if (keyIndex >= currIndex) keyIndex + 1 else currIndex
        }
        var nextKeyIndex = keyIndex
        if (mask and Integer.MIN_VALUE.ushr(index) != 0) {
            if (decoded[keyIndex] == 0) {
                decoded[keyIndex] = currIndex
            }
            nextKeyIndex = decoded[keyIndex]
        } else {
            ++nextKeyIndex
        }
        if (nextKeyIndex >= decoded.size) {
            val nextKeys = IntArray(decoded.size * 2)
            decoded.indices.forEach { nextKeys[it] = decoded[it] }
            decoded = nextKeys
        }
        return initKeys(sizeIndex, mask, limit, currIndex, nextKeyIndex, index + 1)
    }

    /**
     * Decompress a HuffmanInput with Huffman to an output ByteArray. This output can be directly wrapped into a String.
     * The input ByteArray is from the packet data containing a Huffman compressed data.
     * The input size is from the packet data containing the length of the data when decompressed.
     * The output ByteArray is the Huffman decompressed data and is sliced from 0 until the inputted size.
     * Please note the input size is limited to the limit set by your Huffman object using this.
     */
    override fun from(src: HuffmanInput): ByteArray = src.input.decompress(src.size.let { if (it > limit) limit else it })

    /**
     * Compress a HuffmanInput with Huffman to an output ByteArray. This output can be directly wrapped into a String.
     * The input ByteArray is the formatted String that you want to compress with Huffman.
     * The output ByteArray is the Huffman compressed data that is sliced from 0 until the compressed data or the limit set by your Huffman object.
     * Please note the compressed ByteArray output is limited to the limit set by your Huffman object using this.
     */
    override fun to(src: HuffmanInput): ByteArray = src.input.compress()

    private tailrec fun ByteArray.compress(
        output: ByteArray = ByteArray(256),
        key: Int = 0,
        position: Int = 0,
        index: Int = 0
    ): ByteArray {
        if (index == size) {
            // Slice the output from 0 until the size of this compressed data limited by the limit property of this object.
            return output.sliceArray(0 until (7 + position shr 3).let { if (it > limit) limit else it })
        }
        val byte = this[index].toInt() and 0xFF
        val size = sizes[byte]
        if (size.toInt() == 0) throw RuntimeException("Size is equal to zero for Data = $byte")
        val remainder = position and 7
        val readPosition = position shr 3
        val nextKey = output.putEncodedValue(
            limit = (-1 + (remainder - -size) shr 3) + readPosition,
            remainder = remainder + 24,
            mask = encoded[byte],
            startKey = key and (-remainder shr 31),
            readPosition = readPosition
        )
        return compress(output, nextKey, position + size.toInt(), index + 1)
    }

    private tailrec fun ByteArray.decompress(
        limit: Int,
        output: ByteArray = ByteArray(256),
        currentWritePosition: Int = 0,
        currentReadPosition: Int = 0,
        index: Int = 0
    ): ByteArray {
        if (limit == 0) return ByteArray(0)
        val byte = this[index].toInt()

        var writePosition = currentWritePosition
        var readPosition = currentReadPosition

        repeat(8) { x ->
            readPosition = output.putDecodedValue(byte, if (x == 0) -1 else 64 shr (x - 1), readPosition, writePosition).also { pos ->
                if (pos == 0 && ++writePosition >= limit) {
                    // Slice the output from 0 until the size set by the input HuffmanInput limited by this Huffman object limit property.
                    return output.sliceArray(0 until limit)
                }
            }
        }
        return decompress(limit, output, writePosition, readPosition, index + 1)
    }

    private fun ByteArray.putDecodedValue(
        byte: Int,
        mask: Int,
        readPosition: Int,
        writePosition: Int
    ): Int {
        val nextReadPosition = when {
            mask == -1 && byte < 0 -> decoded[readPosition]
            mask != -1 && byte and mask != 0 -> decoded[readPosition]
            else -> readPosition + 1
        }
        val key = decoded[nextReadPosition]
        if (key < 0) {
            this[writePosition] = key.inv().toByte()
            return 0
        }
        return nextReadPosition
    }

    private tailrec fun ByteArray.putEncodedValue(
        limit: Int,
        remainder: Int,
        mask: Int,
        startKey: Int,
        readPosition: Int,
        segment: Int = 0
    ): Int = when (segment) {
        0 -> {
            val next = startKey or (mask ushr remainder)
            this[readPosition] = next.toByte()
            if (limit.inv() >= readPosition.inv()) next
            else putEncodedValue(limit, remainder - 8, mask, startKey, readPosition + 1, 1)
        }
        4 -> {
            val next = mask shl -remainder
            this[readPosition] = next.toByte()
            next
        }
        else -> {
            val next = mask ushr remainder
            this[readPosition] = next.toByte()
            if (segment == 3 && limit <= readPosition) next
            else if (readPosition.inv() <= limit.inv()) next
            else putEncodedValue(limit, remainder - 8, mask, startKey, readPosition + 1, segment + 1)
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
