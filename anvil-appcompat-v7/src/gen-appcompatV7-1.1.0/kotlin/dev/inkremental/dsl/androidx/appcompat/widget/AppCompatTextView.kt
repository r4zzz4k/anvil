@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.TextViewScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatTextView(configure: AppCompatTextViewScope.() -> Unit = {}) =
    v<AppCompatTextView>(configure.bind(AppCompatTextViewScope))
abstract class AppCompatTextViewScope : TextViewScope() {
  @JvmName("initAppCompatTextView")
  fun init(arg: (AppCompatTextView) -> Unit): Unit = initWith<AppCompatTextView>(arg)
  fun precomputedText(arg: PrecomputedTextCompat): Unit = attr("precomputedText", arg)
  fun supportBackgroundTintList(arg: ColorStateList?): Unit = attr("supportBackgroundTintList", arg)
  fun supportBackgroundTintMode(arg: PorterDuff.Mode?): Unit = attr("supportBackgroundTintMode",
      arg)
  fun supportCompoundDrawablesTintList(arg: ColorStateList?): Unit =
      attr("supportCompoundDrawablesTintList", arg)
  fun supportCompoundDrawablesTintMode(arg: PorterDuff.Mode?): Unit =
      attr("supportCompoundDrawablesTintMode", arg)
  fun textMetricsParamsCompat(arg: PrecomputedTextCompat.Params): Unit =
      attr("textMetricsParamsCompat", arg)
  companion object : AppCompatTextViewScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
