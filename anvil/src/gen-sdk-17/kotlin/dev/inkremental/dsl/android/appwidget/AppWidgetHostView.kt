@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.appwidget

import android.appwidget.AppWidgetHostView
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.FrameLayoutScope
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit

fun appWidgetHostView(configure: AppWidgetHostViewScope.() -> Unit = {}) =
    v<AppWidgetHostView>(configure.bind(AppWidgetHostViewScope))
abstract class AppWidgetHostViewScope : FrameLayoutScope() {
  override fun init(arg: (AppWidgetHostView) -> Unit): Unit = initWith<AppWidgetHostView>(arg)
  companion object : AppWidgetHostViewScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
