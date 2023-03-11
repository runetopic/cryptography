package com.runetopic.cryptography.huffman

/**
 * @author Jordan Abraham
 */
data class HuffmanInput(
    val input: ByteArray,
    val size: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HuffmanInput

        if (!input.contentEquals(other.input)) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = input.contentHashCode()
        result = 31 * result + size
        return result
    }
}
