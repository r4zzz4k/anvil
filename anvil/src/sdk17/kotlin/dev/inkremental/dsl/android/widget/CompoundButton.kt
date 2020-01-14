@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.graphics.drawable.Drawable
import android.widget.CompoundButton
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import trikita.anvil.Anvil
import trikita.anvil.attr
import trikita.anvil.bind
import trikita.anvil.v

fun compoundButton(configure: CompoundButtonScope.() -> Unit = {}) =
    v<CompoundButton>(configure.bind(CompoundButtonScope))
abstract class CompoundButtonScope : ButtonScope() {
  fun buttonDrawable(arg: Drawable?): Unit = attr("buttonDrawable", arg)
  fun buttonDrawable(arg: Int): Unit = attr("buttonDrawable", arg)
  fun checked(arg: Boolean): Unit = attr("checked", arg)
  fun onCheckedChange(arg: ((arg0: CompoundButton, arg1: Boolean) -> Unit)?): Unit =
      attr("onCheckedChange", arg)
  companion object : CompoundButtonScope() {
    init {
      Anvil.registerAttributeSetter(SdkSetter)
      Anvil.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
