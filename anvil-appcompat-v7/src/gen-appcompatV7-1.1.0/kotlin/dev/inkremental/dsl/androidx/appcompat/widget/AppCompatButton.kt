@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.widget.AppCompatButton
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.ButtonScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatButton(configure: AppCompatButtonScope.() -> Unit = {}) =
    v<AppCompatButton>(configure.bind(AppCompatButtonScope))
abstract class AppCompatButtonScope : ButtonScope() {
  @JvmName("initAppCompatButton")
  fun init(arg: (AppCompatButton) -> Unit): Unit = initWith<AppCompatButton>(arg)
  fun supportAllCaps(arg: Boolean): Unit = attr("supportAllCaps", arg)
  fun supportBackgroundTintList(arg: ColorStateList?): Unit = attr("supportBackgroundTintList", arg)
  fun supportBackgroundTintMode(arg: PorterDuff.Mode?): Unit = attr("supportBackgroundTintMode",
      arg)
  companion object : AppCompatButtonScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
