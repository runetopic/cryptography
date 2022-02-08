package com.runetopic.cryptography.huffman

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HuffmanTest {

    @BeforeTest
    fun before() {
        sizes = this::class.java.getResourceAsStream("/huffman/sizes.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }
    }

    @Test
    fun testHuffman() {
        val input = "Testing huffman compression"
        val huffman = Huffman(sizes = sizes)
        val compressed = ByteArray(256)
        val offset = huffman.compress(input, compressed)
        assertEquals(16, offset)
        val decompressed = ByteArray(256)
        huffman.decompress(compressed, decompressed, input.length)
        val decompressedString = String(decompressed, 0, input.length)
        assertEquals(input, decompressedString)
    }

    private companion object {
        lateinit var sizes: ByteArray
    }
}
