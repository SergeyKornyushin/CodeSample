package com.codesample.domain.common.extentions

fun String.replaceParagraphs(): String = this.replace("¶¶", "\n")
