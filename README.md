# Cryptography
Useful cryptography algorithms used throughout the RuneTopic organization.

# Gradle
```
cryptography = { module = "com.runetopic.cryptography:cryptography", version.ref "1.0.4-SNAPSHOT" }
```

# XTEA
### Decryption
```
val bytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
val decrypted = bytes.fromXTEA(rounds = 32, keys = IntArray(4))

val expected = "[-22, 56, -103, 36, 1, 86, -95, 47]"
assertEquals(expected, decrypted.contentToString())
```

### Encryption
```
val bytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
val encrypted = bytes.toXTEA(rounds = 32, keys = IntArray(4))

val expected = "[66, 17, 12, 6, 43, 119, -47, 120]"
assertEquals(expected, encrypted.contentToString())
```

# Whirlpool
### Hashing
```
val input = "The quick brown fox jumps over the lazy dog"
val hash = input.toByteArray().toWhirlpool(rounds = 10, size = 64)

val expected = "B97DE512E91E3828B40D2B0FDCE9CEB3C4A71F9BEA8D88E75C4FA854DF36725FD2B52EB6544EDCACD6F8BEDDFEA403CB55AE31F03AD62A5EF54E42EE82C3FB35"
assertEquals(expected, hash.joinToString("") { "%02X".format(it) })
```

# ISAAC
```
val seed = IntArray(4)
val isaac = seed.toISAAC()
val random = isaac.getNext()

val expected = 405143795
assertEquals(expected, random)
```