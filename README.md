# Cryptography
Useful cryptography algorithms used throughout the RuneTopic organization.

# Gradle
```
Coming Soon
```

# XTEA
### Decryption
```
val bytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
bytes.fromXTEA(rounds = 32, keys = IntArray(4))
```

### Encryption
```
val bytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
bytes.toXTEA(rounds = 32, keys = IntArray(4))
```
