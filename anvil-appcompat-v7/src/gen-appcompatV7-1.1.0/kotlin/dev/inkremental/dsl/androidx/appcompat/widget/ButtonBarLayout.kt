@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.ButtonBarLayout
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.LinearLayoutScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun buttonBarLayout(configure: ButtonBarLayoutScope.() -> Unit = {}) =
    v<ButtonBarLayout>(configure.bind(ButtonBarLayoutScope))
abstract class ButtonBarLayoutScope : LinearLayoutScope() {
  @JvmName("initButtonBarLayout")
  fun init(arg: (ButtonBarLayout) -> Unit): Unit = initWith<ButtonBarLayout>(arg)
  fun allowStacking(arg: Boolean): Unit = attr("allowStacking", arg)
  companion object : ButtonBarLayoutScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
