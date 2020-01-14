@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.TextClock
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import kotlin.CharSequence
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import trikita.anvil.Anvil
import trikita.anvil.attr
import trikita.anvil.bind
import trikita.anvil.v

fun textClock(configure: TextClockScope.() -> Unit = {}) =
    v<TextClock>(configure.bind(TextClockScope))
abstract class TextClockScope : TextViewScope() {
  fun format12Hour(arg: CharSequence): Unit = attr("format12Hour", arg)
  fun format24Hour(arg: CharSequence): Unit = attr("format24Hour", arg)
  fun timeZone(arg: String): Unit = attr("timeZone", arg)
  companion object : TextClockScope() {
    init {
      Anvil.registerAttributeSetter(SdkSetter)
      Anvil.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
