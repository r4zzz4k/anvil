@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.TextSwitcher
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

fun textSwitcher(configure: TextSwitcherScope.() -> Unit = {}) =
    v<TextSwitcher>(configure.bind(TextSwitcherScope))
abstract class TextSwitcherScope : ViewSwitcherScope() {
  override fun init(arg: (TextSwitcher) -> Unit): Unit = initWith<TextSwitcher>(arg)
  fun currentText(arg: CharSequence): Unit = attr("currentText", arg)
  fun text(arg: CharSequence): Unit = attr("text", arg)
  companion object : TextSwitcherScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
