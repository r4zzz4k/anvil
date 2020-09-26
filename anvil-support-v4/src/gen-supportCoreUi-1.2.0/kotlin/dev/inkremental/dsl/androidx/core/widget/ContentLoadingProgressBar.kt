@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.core.widget

import androidx.core.widget.ContentLoadingProgressBar
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.ProgressBarScope
import dev.inkremental.dsl.androidx.core.CustomSupportV4Setter
import dev.inkremental.dsl.androidx.core.SupportCoreUiSetter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun contentLoadingProgressBar(configure: ContentLoadingProgressBarScope.() -> Unit = {}) =
    v<ContentLoadingProgressBar>(configure.bind(ContentLoadingProgressBarScope))
abstract class ContentLoadingProgressBarScope : ProgressBarScope() {
  @JvmName("initContentLoadingProgressBar")
  fun init(arg: (ContentLoadingProgressBar) -> Unit): Unit =
      initWith<ContentLoadingProgressBar>(arg)
  companion object : ContentLoadingProgressBarScope() {
    init {
      Inkremental.registerAttributeSetter(SupportCoreUiSetter)
      Inkremental.registerAttributeSetter(CustomSupportV4Setter)
    }
  }
}
