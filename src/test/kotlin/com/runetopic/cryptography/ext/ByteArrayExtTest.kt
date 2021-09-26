package com.runetopic.cryptography.ext

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class ByteArrayExtTest {
    
    @Test
    fun `test g4`() {
        val input = byteArrayOf(55, 32, 45, 78)
        assertEquals(924855630, input.g4(0))
    }

    @Test
    fun `test p4`() {
        val input = ByteArray(4)
        input.p4(0, 100000001)
        assertEquals(byteArrayOf(5, -11, -31, 1).contentToString(), input.contentToString())
    }
}