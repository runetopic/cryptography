# Cryptography
Useful cryptography algorithms used throughout the RuneTopic organization.

# Gradle
```shell
cryptography = { module = "com.runetopic.cryptography:cryptography", version.ref "1.0.7-SNAPSHOT" }
```

# XTEA
### Decryption
```kotlin
val bytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
val decrypted = bytes.fromXTEA(rounds = 32, keys = IntArray(4))

val expected = "[-22, 56, -103, 36, 1, 86, -95, 47]"
assertEquals(expected, decrypted.contentToString())
```

### Encryption
```kotlin
val bytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
val encrypted = bytes.toXTEA(rounds = 32, keys = IntArray(4))

val expected = "[66, 17, 12, 6, 43, 119, -47, 120]"
assertEquals(expected, encrypted.contentToString())
```

# Whirlpool
### Hashing
```kotlin
val input = "The quick brown fox jumps over the lazy dog"
val hash = input.toByteArray().toWhirlpool(rounds = 10, size = 64)

val expected = "B97DE512E91E3828B40D2B0FDCE9CEB3C4A71F9BEA8D88E75C4FA854DF36725FD2B52EB6544EDCACD6F8BEDDFEA403CB55AE31F03AD62A5EF54E42EE82C3FB35"
assertEquals(expected, hash.joinToString("") { "%02X".format(it) })
```

# ISAAC
```kotlin
val seed = IntArray(4)
val isaac = seed.toISAAC()
val random = isaac.getNext()

val expected = 405143795
assertEquals(expected, random)
```

# Huffman
```kotlin
val input = "Testing huffman compression"
val huffman = Huffman(sizes = sizes)
val compressed = ByteArray(256)
val offset = huffman.compress(input, compressed)
assertEquals(16, offset)
val decompressed = ByteArray(256)
huffman.decompress(compressed, decompressed, input.length)
val decompressedString = String(decompressed, 0, input.length)
assertEquals(input, decompressedString)
```
