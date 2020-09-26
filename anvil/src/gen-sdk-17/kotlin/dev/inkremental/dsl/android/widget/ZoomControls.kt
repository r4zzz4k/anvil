@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.view.View
import android.widget.ZoomControls
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Long
import kotlin.Suppress
import kotlin.Unit

fun zoomControls(configure: ZoomControlsScope.() -> Unit = {}) =
    v<ZoomControls>(configure.bind(ZoomControlsScope))
abstract class ZoomControlsScope : LinearLayoutScope() {
  override fun init(arg: (ZoomControls) -> Unit): Unit = initWith<ZoomControls>(arg)
  fun isZoomInEnabled(arg: Boolean): Unit = attr("isZoomInEnabled", arg)
  fun isZoomOutEnabled(arg: Boolean): Unit = attr("isZoomOutEnabled", arg)
  fun onZoomInClick(arg: ((arg0: View) -> Unit)?): Unit = attr("onZoomInClick", arg)
  fun onZoomOutClick(arg: ((arg0: View) -> Unit)?): Unit = attr("onZoomOutClick", arg)
  fun zoomSpeed(arg: Long): Unit = attr("zoomSpeed", arg)
  companion object : ZoomControlsScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
