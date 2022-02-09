package com.runetopic.cryptography.huffman

import com.runetopic.cryptography.compressHuffman
import com.runetopic.cryptography.decompressHuffman
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
        val input = "A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED"
        val huffman = Huffman(sizes = sizes)
        val compressed = ByteArray(256)
        val offset = input.compressHuffman(huffman, compressed)
        assertEquals(60, offset)
        val decompressedString = compressed.decompressHuffman(huffman, input.length)
        assertEquals(input, decompressedString)
    }

    private companion object {
        lateinit var sizes: ByteArray
    }
}
