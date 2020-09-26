@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.MultiAutoCompleteTextViewScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatMultiAutoCompleteTextView(configure: AppCompatMultiAutoCompleteTextViewScope.() -> Unit
    = {}) =
    v<AppCompatMultiAutoCompleteTextView>(configure.bind(AppCompatMultiAutoCompleteTextViewScope))
abstract class AppCompatMultiAutoCompleteTextViewScope : MultiAutoCompleteTextViewScope() {
  @JvmName("initAppCompatMultiAutoCompleteTextView")
  fun init(arg: (AppCompatMultiAutoCompleteTextView) -> Unit): Unit =
      initWith<AppCompatMultiAutoCompleteTextView>(arg)
  fun supportBackgroundTintList(arg: ColorStateList?): Unit = attr("supportBackgroundTintList", arg)
  fun supportBackgroundTintMode(arg: PorterDuff.Mode?): Unit = attr("supportBackgroundTintMode",
      arg)
  companion object : AppCompatMultiAutoCompleteTextViewScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
