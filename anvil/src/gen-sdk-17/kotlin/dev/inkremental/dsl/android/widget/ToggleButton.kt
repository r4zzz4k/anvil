@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.ToggleButton
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.CharSequence
import kotlin.Suppress
import kotlin.Unit

fun toggleButton(configure: ToggleButtonScope.() -> Unit = {}) =
    v<ToggleButton>(configure.bind(ToggleButtonScope))
abstract class ToggleButtonScope : CompoundButtonScope() {
  override fun init(arg: (ToggleButton) -> Unit): Unit = initWith<ToggleButton>(arg)
  fun textOff(arg: CharSequence): Unit = attr("textOff", arg)
  fun textOn(arg: CharSequence): Unit = attr("textOn", arg)
  companion object : ToggleButtonScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
