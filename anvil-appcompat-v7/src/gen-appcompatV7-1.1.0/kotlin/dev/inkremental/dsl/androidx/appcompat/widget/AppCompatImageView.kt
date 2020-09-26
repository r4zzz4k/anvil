@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.widget.AppCompatImageView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.ImageViewScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatImageView(configure: AppCompatImageViewScope.() -> Unit = {}) =
    v<AppCompatImageView>(configure.bind(AppCompatImageViewScope))
abstract class AppCompatImageViewScope : ImageViewScope() {
  @JvmName("initAppCompatImageView")
  fun init(arg: (AppCompatImageView) -> Unit): Unit = initWith<AppCompatImageView>(arg)
  fun supportBackgroundTintList(arg: ColorStateList?): Unit = attr("supportBackgroundTintList", arg)
  fun supportBackgroundTintMode(arg: PorterDuff.Mode?): Unit = attr("supportBackgroundTintMode",
      arg)
  fun supportImageTintList(arg: ColorStateList?): Unit = attr("supportImageTintList", arg)
  fun supportImageTintMode(arg: PorterDuff.Mode?): Unit = attr("supportImageTintMode", arg)
  companion object : AppCompatImageViewScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
