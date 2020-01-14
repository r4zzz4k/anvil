@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.GridLayout
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.view.ViewGroupScope
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import trikita.anvil.Anvil
import trikita.anvil.attr
import trikita.anvil.bind
import trikita.anvil.v

fun gridLayout(configure: GridLayoutScope.() -> Unit = {}) =
    v<GridLayout>(configure.bind(GridLayoutScope))
abstract class GridLayoutScope : ViewGroupScope() {
  fun alignmentMode(arg: Int): Unit = attr("alignmentMode", arg)
  fun columnCount(arg: Int): Unit = attr("columnCount", arg)
  fun columnOrderPreserved(arg: Boolean): Unit = attr("columnOrderPreserved", arg)
  fun orientation(arg: Int): Unit = attr("orientation", arg)
  fun rowCount(arg: Int): Unit = attr("rowCount", arg)
  fun rowOrderPreserved(arg: Boolean): Unit = attr("rowOrderPreserved", arg)
  fun useDefaultMargins(arg: Boolean): Unit = attr("useDefaultMargins", arg)
  companion object : GridLayoutScope() {
    init {
      Anvil.registerAttributeSetter(SdkSetter)
      Anvil.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
