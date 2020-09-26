@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.graphics.drawable.Drawable
import android.widget.CheckedTextView
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

fun checkedTextView(configure: CheckedTextViewScope.() -> Unit = {}) =
    v<CheckedTextView>(configure.bind(CheckedTextViewScope))
abstract class CheckedTextViewScope : TextViewScope() {
  override fun init(arg: (CheckedTextView) -> Unit): Unit = initWith<CheckedTextView>(arg)
  fun checkMarkDrawable(arg: Drawable?): Unit = attr("checkMarkDrawable", arg)
  fun checkMarkDrawable(arg: Int): Unit = attr("checkMarkDrawable", arg)
  fun checked(arg: Boolean): Unit = attr("checked", arg)
  companion object : CheckedTextViewScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
