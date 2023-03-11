package com.runetopic.cryptography.isaac

import com.runetopic.cryptography.Algorithm

/**
 * @author Jordan Abraham
 */
internal interface ISAACAlgorithm : Algorithm<IntArray, ISAACAlgorithm, ISAACAlgorithm> {
    fun getNext(): Int
}
