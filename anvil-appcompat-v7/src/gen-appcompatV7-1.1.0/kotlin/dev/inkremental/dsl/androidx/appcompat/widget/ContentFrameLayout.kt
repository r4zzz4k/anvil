@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.ContentFrameLayout
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.FrameLayoutScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun contentFrameLayout(configure: ContentFrameLayoutScope.() -> Unit = {}) =
    v<ContentFrameLayout>(configure.bind(ContentFrameLayoutScope))
abstract class ContentFrameLayoutScope : FrameLayoutScope() {
  @JvmName("initContentFrameLayout")
  fun init(arg: (ContentFrameLayout) -> Unit): Unit = initWith<ContentFrameLayout>(arg)
  companion object : ContentFrameLayoutScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
