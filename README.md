# Cryptography
Useful cryptography algorithms used throughout the RuneTopic organization.

# Gradle
```
Coming Soon
```

# XTEA
### Decryption
```
val bytes = byteArrayOf(#)
val decrypted = bytes.fromXTEA(rounds = 32, keys = IntArray(4))
```

### Encryption
```
val bytes = byteArrayOf(#)
val encrypted bytes.toXTEA(rounds = 32, keys = IntArray(4))
```
# Whirlpool
### Hashing
```
val bytes = byteArrayOf(#)
val hash = bytes.toWhirlpool(rounds = 10, size = 64)
```