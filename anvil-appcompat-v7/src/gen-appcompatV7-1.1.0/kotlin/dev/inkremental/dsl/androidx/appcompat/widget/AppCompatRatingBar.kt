@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.AppCompatRatingBar
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.widget.RatingBarScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun appCompatRatingBar(configure: AppCompatRatingBarScope.() -> Unit = {}) =
    v<AppCompatRatingBar>(configure.bind(AppCompatRatingBarScope))
abstract class AppCompatRatingBarScope : RatingBarScope() {
  @JvmName("initAppCompatRatingBar")
  fun init(arg: (AppCompatRatingBar) -> Unit): Unit = initWith<AppCompatRatingBar>(arg)
  companion object : AppCompatRatingBarScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
