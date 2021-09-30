package com.runetopic.cryptography.xtea

import com.runetopic.cryptography.fromXTEA
import com.runetopic.cryptography.toXTEA
import io.mockk.every
import io.mockk.mockk
import java.nio.ByteBuffer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class XTEATest {

    @BeforeTest
    fun `before test`() {
        decrypted = this::class.java.getResourceAsStream("/xtea/decrypted.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }

        keys = this::class.java.getResourceAsStream("/xtea/keys.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toInt() }.toIntArray() }

        rounds8 = this::class.java.getResourceAsStream("/xtea/8-rounds.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }

        rounds16 = this::class.java.getResourceAsStream("/xtea/16-rounds.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }

        rounds32 = this::class.java.getResourceAsStream("/xtea/32-rounds.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }

        rounds64 = this::class.java.getResourceAsStream("/xtea/64-rounds.txt")!!
            .bufferedReader().use { it.readLine().split(", ").map { int -> int.toByte() }.toByteArray() }
    }

    @Test
    fun `test decrypt with xtea 8 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 8
        every { mock.getKeys() } returns keys
        assertEquals(rounds8.contentToString(), ByteBuffer.wrap(decrypted).fromXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    @Test
    fun `test encrypt with xtea 8 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 8
        every { mock.getKeys() } returns keys
        assertEquals(decrypted.contentToString(), ByteBuffer.wrap(rounds8).toXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    @Test
    fun `test decrypt with xtea 16 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 16
        every { mock.getKeys() } returns keys
        assertEquals(rounds16.contentToString(), ByteBuffer.wrap(decrypted).fromXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    @Test
    fun `test encrypt with xtea 16 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 16
        every { mock.getKeys() } returns keys
        assertEquals(decrypted.contentToString(), ByteBuffer.wrap(rounds16).toXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    @Test
    fun `test decrypt with xtea 32 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 32
        every { mock.getKeys() } returns keys
        assertEquals(rounds32.contentToString(), ByteBuffer.wrap(decrypted).fromXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    @Test
    fun `test encrypt with xtea 32 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 32
        every { mock.getKeys() } returns keys
        assertEquals(decrypted.contentToString(), ByteBuffer.wrap(rounds32).toXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    @Test
    fun `test decrypt with xtea 64 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 64
        every { mock.getKeys() } returns keys
        assertEquals(rounds64.contentToString(), ByteBuffer.wrap(decrypted).fromXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    @Test
    fun `test encrypt with xtea 64 rounds`() {
        val mock = mockk<IXTEA>(relaxed = true)
        every { mock.getRounds() } returns 64
        every { mock.getKeys() } returns keys
        assertEquals(decrypted.contentToString(), ByteBuffer.wrap(rounds64).toXTEA(mock.getRounds(), mock.getKeys()).contentToString())
    }

    private companion object {
        lateinit var decrypted: ByteArray
        lateinit var keys: IntArray
        lateinit var rounds8: ByteArray
        lateinit var rounds16: ByteArray
        lateinit var rounds32: ByteArray
        lateinit var rounds64: ByteArray
    }
}