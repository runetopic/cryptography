package com.runetopic.cryptography.whirlpool

import com.runetopic.cryptography.toWhirlpool
import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class WhirlpoolTest {

    @BeforeTest
    fun `before test`() {
        input = this::class.java.getResourceAsStream("/whirlpool/input.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }

        whirlpool = this::class.java.getResourceAsStream("/whirlpool/whirlpool-hash.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }
    }

    @Test
    fun `test whirlpool`() {
        val mock = mockk<IWhirlpool>(relaxed = true)
        every { mock.getRounds() } returns 10
        every { mock.getSize() } returns 64
        assertEquals(whirlpool.contentToString(), input.toWhirlpool(mock.getRounds(), mock.getSize()).contentToString())
    }

    @Test
    fun `test quick brown fox jumps over the lazy dog`() {
        val mock = mockk<IWhirlpool>(relaxed = true)
        every { mock.getRounds() } returns 10
        every { mock.getSize() } returns 64

        val input = "The quick brown fox jumps over the lazy dog"
        assertEquals(
            "B97DE512E91E3828B40D2B0FDCE9CEB3C4A71F9BEA8D88E75C4FA854DF36725FD2B52EB6544EDCACD6F8BEDDFEA403CB55AE31F03AD62A5EF54E42EE82C3FB35",
            input.toByteArray().toWhirlpool(mock.getRounds(), mock.getSize()).joinToString("") { "%02X".format(it) }
        )
    }

    @Test
    fun `test quick brown fox jumps over the lazy eog`() {
        val mock = mockk<IWhirlpool>(relaxed = true)
        every { mock.getRounds() } returns 10
        every { mock.getSize() } returns 64

        val input = "The quick brown fox jumps over the lazy eog"
        assertEquals(
            "C27BA124205F72E6847F3E19834F925CC666D0974167AF915BB462420ED40CC50900D85A1F923219D832357750492D5C143011A76988344C2635E69D06F2D38C",
            input.toByteArray().toWhirlpool(mock.getRounds(), mock.getSize()).joinToString("") { "%02X".format(it) }
        )
    }

    @Test
    fun `test zero length`() {
        val mock = mockk<IWhirlpool>(relaxed = true)
        every { mock.getRounds() } returns 10
        every { mock.getSize() } returns 64

        val input = ""
        assertEquals(
            "19FA61D75522A4669B44E39C1D2E1726C530232130D407F89AFEE0964997F7A73E83BE698B288FEBCF88E3E03C4F0757EA8964E59B63D93708B138CC42A66EB3",
            input.toByteArray().toWhirlpool(mock.getRounds(), mock.getSize()).joinToString("") { "%02X".format(it) }
        )
    }

    private companion object {
        lateinit var input: ByteArray
        lateinit var whirlpool: ByteArray
    }
}