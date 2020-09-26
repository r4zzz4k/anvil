@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.view.menu

import android.graphics.drawable.Drawable
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuBuilder
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.dsl.androidx.appcompat.widget.AppCompatTextViewScope
import dev.inkremental.v
import kotlin.Boolean
import kotlin.CharSequence
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun actionMenuItemView(configure: ActionMenuItemViewScope.() -> Unit = {}) =
    v<ActionMenuItemView>(configure.bind(ActionMenuItemViewScope))
abstract class ActionMenuItemViewScope : AppCompatTextViewScope() {
  @JvmName("initActionMenuItemView")
  fun init(arg: (ActionMenuItemView) -> Unit): Unit = initWith<ActionMenuItemView>(arg)
  fun checkable(arg: Boolean): Unit = attr("checkable", arg)
  fun checked(arg: Boolean): Unit = attr("checked", arg)
  fun expandedFormat(arg: Boolean): Unit = attr("expandedFormat", arg)
  fun icon(arg: Drawable): Unit = attr("icon", arg)
  fun itemInvoker(arg: MenuBuilder.ItemInvoker): Unit = attr("itemInvoker", arg)
  fun popupCallback(arg: ActionMenuItemView.PopupCallback): Unit = attr("popupCallback", arg)
  fun title(arg: CharSequence): Unit = attr("title", arg)
  companion object : ActionMenuItemViewScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
