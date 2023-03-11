package com.runetopic.cryptography

/**
 * @author Jordan Abraham
 */
internal interface Algorithm<S, I, O> {
    fun from(src: S): I
    fun to(src: S): O
}
