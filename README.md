# Cryptography
Useful cryptography algorithms used throughout the RuneTopic organization.

# Gradle
```shell
cryptography = { module = "com.runetopic.cryptography:cryptography", version.ref "1.0.11-SNAPSHOT" }
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

### Input
```kotlin
val input = "A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED"
// The sizes below can be loaded from: index(indexId = 10).group(groupName = "huffman").file(0).data
val sizes = byteArrayOf(22, 22, 22, 22, 22, 22, 21, 22, 22, 20, 22, 22, 22, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13, 16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11, 14, 19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12, 11, 10, 10, 9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22, 13, 21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4, 5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21, 22, 22)
```
### Compression
```kotlin
val huffman = Huffman(sizes = sizes)
val compressed = ByteArray(256)
val offset = input.compressHuffman(huffman, compressed)
assertEquals(60, offset)
```

### Decompression
```kotlin
val decompressedString = compressed.decompressHuffman(huffman, input.length)
assertEquals(input, decompressedString)
```
