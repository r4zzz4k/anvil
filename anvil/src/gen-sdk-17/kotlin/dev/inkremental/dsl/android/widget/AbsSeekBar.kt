@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.graphics.drawable.Drawable
import android.widget.AbsSeekBar
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

fun absSeekBar(configure: AbsSeekBarScope.() -> Unit = {}) =
    v<AbsSeekBar>(configure.bind(AbsSeekBarScope))
abstract class AbsSeekBarScope : ProgressBarScope() {
  override fun init(arg: (AbsSeekBar) -> Unit): Unit = initWith<AbsSeekBar>(arg)
  fun keyProgressIncrement(arg: Int): Unit = attr("keyProgressIncrement", arg)
  fun thumb(arg: Drawable): Unit = attr("thumb", arg)
  fun thumbOffset(arg: Int): Unit = attr("thumbOffset", arg)
  companion object : AbsSeekBarScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
