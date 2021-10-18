package com.runetopic.cryptography.whirlpool

import com.runetopic.cryptography.toWhirlpool
import io.mockk.every
import io.mockk.mockk
import java.nio.ByteBuffer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class WhirlpoolTest {

    @Test
    fun idk() {
        val bytes = byteArrayOf(1, 0, 0, 1, 54, 0, 0, 58, -82, 49, 65, 89, 38, 83, 89, 82, 79, 80, -29, 0, 29, 14, -1, -1, -18, -117, 115, -54, -89, -13, -83, 59, 83, 80, -61, -122, -24, -117, -98, -106, -128, -23, 20, -128, -81, 9, -92, -64, 81, -112, -94, 2, 40, 65, -87, 45, 48, 0, -40, -125, -103, 0, 3, 64, 77, 51, 73, -127, 48, 2, 109, 38, 0, -104, 0, 38, 6, -128, -48, 52, 6, -104, 52, -121, 26, 26, 52, 104, 52, 104, 26, 0, 0, 0, 0, 25, 0, 0, 0, 26, 3, 32, 0, -63, 36, -92, -103, 25, 26, 49, 0, 3, 13, 67, 32, 13, 0, -47, -96, 0, 1, -75, 0, 3, 77, 6, 70, -128, -13, -128, -108, 104, 19, 36, 87, -63, -105, 14, 53, -108, 89, 113, 113, -17, -88, 62, -103, 70, 67, -72, 52, 105, 104, -50, -122, -10, 29, -24, -110, -100, -47, -39, 4, -106, 0, -65, 97, -110, 1, 36, -112, -103, 9, 48, 8, 6, 100, 33, 51, 48, -124, 8, 78, -78, 16, -127, 32, 79, 49, 53, -85, 24, -25, -17, -32, 125, -10, -41, 19, 92, -9, -37, 113, -33, 55, 30, 104, 45, 62, 78, -43, 42, 26, -80, 1, -103, -121, -108, 40, 33, 122, -79, -8, 68, -12, -39, -56, 13, 31, 123, 100, -92, -36, 80, -10, -110, -71, -16, 65, -86, 19, 30, 118, -60, -120, -123, 55, -4, -110, -119, -70, 58, -58, -127, -7, -52, 12, 59, 70, -118, 119, 30, 127, -45, 121, -120, 103, -125, -105, 122, -24, -40, 93, -27, 6, 96, 66, -64, 4, -31, 15, -83, 124, -14, -24, 103, -121, 56, 29, -60, 68, 109, 46, -59, 121, -124, -127, 103, 95, -3, 46, 30, -109, 0, -124, -112, 1, 15, 98, -18, 72, -89, 10, 18, 10, 73, -22, 28, 96)
        val idk = ByteBuffer.wrap(bytes).array().toWhirlpool()
        println(idk.contentToString())
    }

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