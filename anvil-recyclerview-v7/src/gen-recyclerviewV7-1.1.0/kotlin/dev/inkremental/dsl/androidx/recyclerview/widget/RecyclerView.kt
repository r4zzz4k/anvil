@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.recyclerview.widget

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.android.view.ViewGroupScope
import dev.inkremental.dsl.androidx.recyclerview.CustomRecyclerViewv7Setter
import dev.inkremental.dsl.androidx.recyclerview.RecyclerviewV7Setter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun recyclerView(configure: RecyclerViewScope.() -> Unit = {}) =
    v<RecyclerView>(configure.bind(RecyclerViewScope))
abstract class RecyclerViewScope : ViewGroupScope() {
  @JvmName("initRecyclerView")
  fun init(arg: (RecyclerView) -> Unit): Unit = initWith<RecyclerView>(arg)
  fun accessibilityDelegateCompat(arg: RecyclerViewAccessibilityDelegate?): Unit =
      attr("accessibilityDelegateCompat", arg)
  fun childDrawingOrderCallback(arg: RecyclerView.ChildDrawingOrderCallback?): Unit =
      attr("childDrawingOrderCallback", arg)
  fun edgeEffectFactory(arg: RecyclerView.EdgeEffectFactory): Unit = attr("edgeEffectFactory", arg)
  fun hasFixedSize(arg: Boolean): Unit = attr("hasFixedSize", arg)
  fun itemAnimator(arg: RecyclerView.ItemAnimator?): Unit = attr("itemAnimator", arg)
  fun itemViewCacheSize(arg: Int): Unit = attr("itemViewCacheSize", arg)
  fun layoutManager(arg: RecyclerView.LayoutManager?): Unit = attr("layoutManager", arg)
  fun onFling(arg: ((arg0: Int, arg1: Int) -> Boolean)?): Unit = attr("onFling", arg)
  fun preserveFocusAfterLayout(arg: Boolean): Unit = attr("preserveFocusAfterLayout", arg)
  fun recycledViewPool(arg: RecyclerView.RecycledViewPool?): Unit = attr("recycledViewPool", arg)
  fun recyclerListener(arg: RecyclerView.RecyclerListener?): Unit = attr("recyclerListener", arg)
  fun scrollingTouchSlop(arg: Int): Unit = attr("scrollingTouchSlop", arg)
  fun viewCacheExtension(arg: RecyclerView.ViewCacheExtension?): Unit = attr("viewCacheExtension",
      arg)
  companion object : RecyclerViewScope() {
    init {
      Inkremental.registerAttributeSetter(RecyclerviewV7Setter)
      Inkremental.registerAttributeSetter(CustomRecyclerViewv7Setter)
    }
  }
}
