package com.runetopic.cryptography.ext

/**
 * @author Jordan Abraham
 */
infix fun Int.downUntil(to: Int): IntProgression {
    if (to >= Int.MAX_VALUE) return IntRange.EMPTY
    return this downTo (to + 1)
}