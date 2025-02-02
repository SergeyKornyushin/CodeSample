package com.otaliastudios.common.extensions

import android.view.View

fun View.getLocationOnScreen(): IntArray = IntArray(2).apply { getLocationOnScreen(this) }