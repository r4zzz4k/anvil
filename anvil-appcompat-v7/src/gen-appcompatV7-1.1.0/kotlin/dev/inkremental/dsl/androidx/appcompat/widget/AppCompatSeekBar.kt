@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.AppCompatSeekBar
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.SeekBarScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatSeekBar(configure: AppCompatSeekBarScope.() -> Unit = {}) =
    v<AppCompatSeekBar>(configure.bind(AppCompatSeekBarScope))
abstract class AppCompatSeekBarScope : SeekBarScope() {
  @JvmName("initAppCompatSeekBar")
  fun init(arg: (AppCompatSeekBar) -> Unit): Unit = initWith<AppCompatSeekBar>(arg)
  companion object : AppCompatSeekBarScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
