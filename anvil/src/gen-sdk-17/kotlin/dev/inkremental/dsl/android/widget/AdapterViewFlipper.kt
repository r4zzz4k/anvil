@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.AdapterViewFlipper
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit

fun adapterViewFlipper(configure: AdapterViewFlipperScope.() -> Unit = {}) =
    v<AdapterViewFlipper>(configure.bind(AdapterViewFlipperScope))
abstract class AdapterViewFlipperScope : AdapterViewAnimatorScope() {
  override fun init(arg: (AdapterViewFlipper) -> Unit): Unit = initWith<AdapterViewFlipper>(arg)
  fun autoStart(arg: Boolean): Unit = attr("autoStart", arg)
  fun flipInterval(arg: Int): Unit = attr("flipInterval", arg)
  companion object : AdapterViewFlipperScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
