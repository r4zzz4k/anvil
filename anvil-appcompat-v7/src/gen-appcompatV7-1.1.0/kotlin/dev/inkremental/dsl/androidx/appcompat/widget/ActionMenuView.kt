@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import android.graphics.drawable.Drawable
import android.view.MenuItem
import androidx.appcompat.widget.ActionMenuView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun actionMenuView(configure: ActionMenuViewScope.() -> Unit = {}) =
    v<ActionMenuView>(configure.bind(ActionMenuViewScope))
abstract class ActionMenuViewScope : LinearLayoutCompatScope() {
  @JvmName("initActionMenuView")
  fun init(arg: (ActionMenuView) -> Unit): Unit = initWith<ActionMenuView>(arg)
  fun expandedActionViewsExclusive(arg: Boolean): Unit = attr("expandedActionViewsExclusive", arg)
  fun onMenuItemClick(arg: ((arg0: MenuItem) -> Boolean)?): Unit = attr("onMenuItemClick", arg)
  fun overflowIcon(arg: Drawable?): Unit = attr("overflowIcon", arg)
  fun overflowReserved(arg: Boolean): Unit = attr("overflowReserved", arg)
  fun popupTheme(arg: Int): Unit = attr("popupTheme", arg)
  companion object : ActionMenuViewScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
