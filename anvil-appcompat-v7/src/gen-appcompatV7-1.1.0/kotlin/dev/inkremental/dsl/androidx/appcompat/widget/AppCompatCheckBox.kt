@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.widget.AppCompatCheckBox
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.CheckBoxScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatCheckBox(configure: AppCompatCheckBoxScope.() -> Unit = {}) =
    v<AppCompatCheckBox>(configure.bind(AppCompatCheckBoxScope))
abstract class AppCompatCheckBoxScope : CheckBoxScope() {
  @JvmName("initAppCompatCheckBox")
  fun init(arg: (AppCompatCheckBox) -> Unit): Unit = initWith<AppCompatCheckBox>(arg)
  fun supportBackgroundTintList(arg: ColorStateList?): Unit = attr("supportBackgroundTintList", arg)
  fun supportBackgroundTintMode(arg: PorterDuff.Mode?): Unit = attr("supportBackgroundTintMode",
      arg)
  fun supportButtonTintList(arg: ColorStateList?): Unit = attr("supportButtonTintList", arg)
  fun supportButtonTintMode(arg: PorterDuff.Mode?): Unit = attr("supportButtonTintMode", arg)
  companion object : AppCompatCheckBoxScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
