@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.ViewStubCompat
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.view.ViewScope
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun viewStubCompat(configure: ViewStubCompatScope.() -> Unit = {}) =
    v<ViewStubCompat>(configure.bind(ViewStubCompatScope))
abstract class ViewStubCompatScope : ViewScope() {
  @JvmName("initViewStubCompat")
  fun init(arg: (ViewStubCompat) -> Unit): Unit = initWith<ViewStubCompat>(arg)
  fun inflatedId(arg: Int): Unit = attr("inflatedId", arg)
  fun layoutInflater(arg: LayoutInflater): Unit = attr("layoutInflater", arg)
  fun layoutResource(arg: Int): Unit = attr("layoutResource", arg)
  fun onInflate(arg: ((arg0: ViewStubCompat, arg1: View) -> Unit)?): Unit = attr("onInflate", arg)
  companion object : ViewStubCompatScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
