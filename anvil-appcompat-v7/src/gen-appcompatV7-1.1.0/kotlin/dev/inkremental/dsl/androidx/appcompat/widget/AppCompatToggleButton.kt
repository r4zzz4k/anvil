@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.AppCompatToggleButton
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.ToggleButtonScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatToggleButton(configure: AppCompatToggleButtonScope.() -> Unit = {}) =
    v<AppCompatToggleButton>(configure.bind(AppCompatToggleButtonScope))
abstract class AppCompatToggleButtonScope : ToggleButtonScope() {
  @JvmName("initAppCompatToggleButton")
  fun init(arg: (AppCompatToggleButton) -> Unit): Unit = initWith<AppCompatToggleButton>(arg)
  companion object : AppCompatToggleButtonScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
