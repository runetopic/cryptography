package com.runetopic.cryptography.isaac

import com.runetopic.cryptography.Algorithm

/**
 * @author Jordan Abraham
 */
internal interface IISAAC : Algorithm<IntArray, IISAAC> {
    fun getNext(): Int
}
