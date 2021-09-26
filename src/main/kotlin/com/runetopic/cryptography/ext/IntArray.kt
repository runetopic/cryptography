package com.runetopic.cryptography.ext

import com.runetopic.cryptography.isaac.ISAAC

/**
 * @author Jordan Abraham
 */
fun IntArray.toISAAC() = ISAAC().to(this)