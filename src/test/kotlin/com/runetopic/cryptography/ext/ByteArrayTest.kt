package com.runetopic.cryptography.ext

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class ByteArrayTest {
    
    @Test
    fun `test g4`() {
        val input = byteArrayOf(55, 32, 45, 78)
        assertEquals(924855630, input.g4(0))
    }

    @Test
    fun `test g8`() {
        val input = byteArrayOf(55, 32, 45, 78, -46, -32, 56, 99)
        assertEquals(3972224687909386339, input.g8(0))
    }

    @Test
    fun `test p4`() {
        val input = ByteArray(4)
        input.p4(0, 924855630)
        assertEquals(byteArrayOf(55, 32, 45, 78).contentToString(), input.contentToString())
    }

    @Test
    fun `test p8`() {
        val input = ByteArray(8)
        input.p8(0, 3972224687909386339)
        assertEquals(byteArrayOf(55, 32, 45, 78, -46, -32, 56, 99).contentToString(), input.contentToString())
    }
}