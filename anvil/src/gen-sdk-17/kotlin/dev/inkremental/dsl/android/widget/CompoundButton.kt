@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.graphics.drawable.Drawable
import android.widget.CompoundButton
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

fun compoundButton(configure: CompoundButtonScope.() -> Unit = {}) =
    v<CompoundButton>(configure.bind(CompoundButtonScope))
abstract class CompoundButtonScope : ButtonScope() {
  override fun init(arg: (CompoundButton) -> Unit): Unit = initWith<CompoundButton>(arg)
  fun buttonDrawable(arg: Drawable?): Unit = attr("buttonDrawable", arg)
  fun buttonDrawable(arg: Int): Unit = attr("buttonDrawable", arg)
  fun checked(arg: Boolean): Unit = attr("checked", arg)
  fun onCheckedChange(arg: ((arg0: CompoundButton, arg1: Boolean) -> Unit)?): Unit =
      attr("onCheckedChange", arg)
  companion object : CompoundButtonScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
