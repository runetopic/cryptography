package com.runetopic.cryptography.isaac

import com.runetopic.cryptography.toISAAC
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class ISAACTest {

    @BeforeTest
    fun `before test`() {
        random = this::class.java.getResourceAsStream("/isaac/isaac-random.txt")!!
            .bufferedReader().use { it.readLines().map { s -> s.toInt() } }
    }

    @Test
    fun `test isaac`() {
        val input = IntArray(4)
        val isaac = input.toISAAC()
        (0 until 256).forEach { assertEquals(random[it], isaac.getNext()) }
    }

    private companion object {
        lateinit var random: List<Int>
    }
}
