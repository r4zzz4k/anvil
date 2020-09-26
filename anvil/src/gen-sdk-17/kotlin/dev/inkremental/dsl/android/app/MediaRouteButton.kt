@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.app

import android.app.MediaRouteButton
import android.view.View
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.view.ViewScope
import dev.inkremental.v
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit

fun mediaRouteButton(configure: MediaRouteButtonScope.() -> Unit = {}) =
    v<MediaRouteButton>(configure.bind(MediaRouteButtonScope))
abstract class MediaRouteButtonScope : ViewScope() {
  override fun init(arg: (MediaRouteButton) -> Unit): Unit = initWith<MediaRouteButton>(arg)
  fun extendedSettingsClickListener(arg: View.OnClickListener): Unit =
      attr("extendedSettingsClickListener", arg)
  fun routeTypes(arg: Int): Unit = attr("routeTypes", arg)
  companion object : MediaRouteButtonScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
