@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.ImageButton
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit

fun imageButton(configure: ImageButtonScope.() -> Unit = {}) =
    v<ImageButton>(configure.bind(ImageButtonScope))
abstract class ImageButtonScope : ImageViewScope() {
  override fun init(arg: (ImageButton) -> Unit): Unit = initWith<ImageButton>(arg)
  companion object : ImageButtonScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
