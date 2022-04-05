package com.check24.utils

import kotlin.math.floor

fun Double.round(precision: Int): Double =
    "%.${precision}f".format(this@round).toDoubleOrNull() ?: 0.0

fun Double.roundDownHalfStep(): Double = floor(this@roundDownHalfStep * 2) / 2
