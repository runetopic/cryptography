package com.runetopic.cryptography.isaac

import com.runetopic.cryptography.ext.toISAAC
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class ISAACTest {

    @Test
    fun `test isaac`() {
        val input = IntArray(4)
        val isaac = input.toISAAC()
        assertEquals(405143795, isaac.getNext())
    }
}