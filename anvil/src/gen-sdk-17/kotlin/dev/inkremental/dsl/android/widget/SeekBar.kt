@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.SeekBar
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit

fun seekBar(configure: SeekBarScope.() -> Unit = {}) = v<SeekBar>(configure.bind(SeekBarScope))
abstract class SeekBarScope : AbsSeekBarScope() {
  override fun init(arg: (SeekBar) -> Unit): Unit = initWith<SeekBar>(arg)
  fun onSeekBarChange(arg: SeekBar.OnSeekBarChangeListener?): Unit = attr("onSeekBarChange", arg)
  companion object : SeekBarScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
