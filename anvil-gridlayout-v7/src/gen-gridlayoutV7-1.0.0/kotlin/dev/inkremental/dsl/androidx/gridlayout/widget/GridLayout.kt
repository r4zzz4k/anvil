@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.gridlayout.widget

import android.util.Printer
import androidx.gridlayout.widget.GridLayout
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.view.ViewGroupScope
import dev.inkremental.dsl.androidx.gridlayout.GridlayoutV7Setter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit

fun gridLayout(configure: GridLayoutScope.() -> Unit = {}) =
    v<GridLayout>(configure.bind(GridLayoutScope))
abstract class GridLayoutScope : ViewGroupScope() {
  fun alignmentMode(arg: Int): Unit = attr("alignmentMode", arg)
  fun columnCount(arg: Int): Unit = attr("columnCount", arg)
  fun columnOrderPreserved(arg: Boolean): Unit = attr("columnOrderPreserved", arg)
  fun orientation(arg: Int): Unit = attr("orientation", arg)
  fun printer(arg: Printer): Unit = attr("printer", arg)
  fun rowCount(arg: Int): Unit = attr("rowCount", arg)
  fun rowOrderPreserved(arg: Boolean): Unit = attr("rowOrderPreserved", arg)
  fun useDefaultMargins(arg: Boolean): Unit = attr("useDefaultMargins", arg)
  companion object : GridLayoutScope() {
    init {
      Inkremental.registerAttributeSetter(GridlayoutV7Setter)
    }
  }
}
