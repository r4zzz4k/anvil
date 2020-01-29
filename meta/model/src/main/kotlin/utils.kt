package dev.inkremental.meta.model

import java.io.File

operator fun File.div(relative: File): File = resolve(relative)
operator fun File.div(relative: String): File = resolve(relative)
