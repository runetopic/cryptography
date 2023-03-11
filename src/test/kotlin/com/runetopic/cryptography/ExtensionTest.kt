package com.runetopic.cryptography

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class ExtensionTest {

    @Test
    fun `test g8`() {
        val input = byteArrayOf(55, 32, 45, 78, -46, -32, 56, 99)
        assertEquals(3972224687909386339, input.g8(0))
    }

    @Test
    fun `test p8`() {
        val input = ByteArray(8)
        input.p8(0, 3972224687909386339)
        assertEquals(byteArrayOf(55, 32, 45, 78, -46, -32, 56, 99).contentToString(), input.contentToString())
    }
}
