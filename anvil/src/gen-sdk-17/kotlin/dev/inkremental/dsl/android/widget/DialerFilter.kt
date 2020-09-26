@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.text.TextWatcher
import android.widget.DialerFilter
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit

fun dialerFilter(configure: DialerFilterScope.() -> Unit = {}) =
    v<DialerFilter>(configure.bind(DialerFilterScope))
abstract class DialerFilterScope : RelativeLayoutScope() {
  override fun init(arg: (DialerFilter) -> Unit): Unit = initWith<DialerFilter>(arg)
  fun digitsWatcher(arg: TextWatcher): Unit = attr("digitsWatcher", arg)
  fun filterWatcher(arg: TextWatcher): Unit = attr("filterWatcher", arg)
  fun lettersWatcher(arg: TextWatcher): Unit = attr("lettersWatcher", arg)
  fun mode(arg: Int): Unit = attr("mode", arg)
  companion object : DialerFilterScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
