@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package androidx.coordinatorlayout.widget

import android.graphics.drawable.Drawable
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.view.ViewGroupScope
import dev.inkremental.dsl.androidx.core.CustomSupportV4Setter
import dev.inkremental.dsl.androidx.core.SupportCoreUiSetter
import dev.inkremental.v
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun coordinatorLayout(configure: CoordinatorLayoutScope.() -> Unit = {}) =
    v<CoordinatorLayout>(configure.bind(CoordinatorLayoutScope))
abstract class CoordinatorLayoutScope : ViewGroupScope() {
  @JvmName("initCoordinatorLayout")
  fun init(arg: (CoordinatorLayout) -> Unit): Unit = initWith<CoordinatorLayout>(arg)
  fun statusBarBackground(arg: Drawable?): Unit = attr("statusBarBackground", arg)
  fun statusBarBackgroundColor(arg: Int): Unit = attr("statusBarBackgroundColor", arg)
  fun statusBarBackgroundResource(arg: Int): Unit = attr("statusBarBackgroundResource", arg)
  companion object : CoordinatorLayoutScope() {
    init {
      Inkremental.registerAttributeSetter(SupportCoreUiSetter)
      Inkremental.registerAttributeSetter(CustomSupportV4Setter)
    }
  }
}
