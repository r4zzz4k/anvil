@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.widget.AppCompatSpinner
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.SpinnerScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatSpinner(configure: AppCompatSpinnerScope.() -> Unit = {}) =
    v<AppCompatSpinner>(configure.bind(AppCompatSpinnerScope))
abstract class AppCompatSpinnerScope : SpinnerScope() {
  @JvmName("initAppCompatSpinner")
  fun init(arg: (AppCompatSpinner) -> Unit): Unit = initWith<AppCompatSpinner>(arg)
  fun supportBackgroundTintList(arg: ColorStateList?): Unit = attr("supportBackgroundTintList", arg)
  fun supportBackgroundTintMode(arg: PorterDuff.Mode?): Unit = attr("supportBackgroundTintMode",
      arg)
  companion object : AppCompatSpinnerScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
