@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.MultiAutoCompleteTextView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit

fun multiAutoCompleteTextView(configure: MultiAutoCompleteTextViewScope.() -> Unit = {}) =
    v<MultiAutoCompleteTextView>(configure.bind(MultiAutoCompleteTextViewScope))
abstract class MultiAutoCompleteTextViewScope : AutoCompleteTextViewScope() {
  override fun init(arg: (MultiAutoCompleteTextView) -> Unit): Unit =
      initWith<MultiAutoCompleteTextView>(arg)
  fun tokenizer(arg: MultiAutoCompleteTextView.Tokenizer): Unit = attr("tokenizer", arg)
  companion object : MultiAutoCompleteTextViewScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
