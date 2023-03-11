package com.runetopic.cryptography

/**
 * @author Jordan Abraham
 */
internal interface Algorithm<S, T> {
    fun from(src: S): T
    fun to(src: S): T
}
