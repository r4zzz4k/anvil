@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.content.Intent
import android.graphics.drawable.Drawable
import android.widget.AbsListView
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import trikita.anvil.Anvil
import trikita.anvil.attr
import trikita.anvil.bind
import trikita.anvil.v

fun absListView(configure: AbsListViewScope.() -> Unit = {}) =
    v<AbsListView>(configure.bind(AbsListViewScope))
abstract class AbsListViewScope : AdapterViewScope() {
  fun cacheColorHint(arg: Int): Unit = attr("cacheColorHint", arg)
  fun choiceMode(arg: Int): Unit = attr("choiceMode", arg)
  fun drawSelectorOnTop(arg: Boolean): Unit = attr("drawSelectorOnTop", arg)
  fun fastScrollAlwaysVisible(arg: Boolean): Unit = attr("fastScrollAlwaysVisible", arg)
  fun fastScrollEnabled(arg: Boolean): Unit = attr("fastScrollEnabled", arg)
  fun filterText(arg: String): Unit = attr("filterText", arg)
  fun friction(arg: Float): Unit = attr("friction", arg)
  fun multiChoiceModeListener(arg: AbsListView.MultiChoiceModeListener): Unit =
      attr("multiChoiceModeListener", arg)
  fun onScroll(arg: AbsListView.OnScrollListener?): Unit = attr("onScroll", arg)
  fun recyclerListener(arg: AbsListView.RecyclerListener): Unit = attr("recyclerListener", arg)
  fun remoteViewsAdapter(arg: Intent): Unit = attr("remoteViewsAdapter", arg)
  fun scrollingCacheEnabled(arg: Boolean): Unit = attr("scrollingCacheEnabled", arg)
  fun selector(arg: Drawable): Unit = attr("selector", arg)
  fun selector(arg: Int): Unit = attr("selector", arg)
  fun smoothScrollbarEnabled(arg: Boolean): Unit = attr("smoothScrollbarEnabled", arg)
  fun stackFromBottom(arg: Boolean): Unit = attr("stackFromBottom", arg)
  fun textFilterEnabled(arg: Boolean): Unit = attr("textFilterEnabled", arg)
  fun transcriptMode(arg: Int): Unit = attr("transcriptMode", arg)
  fun velocityScale(arg: Float): Unit = attr("velocityScale", arg)
  companion object : AbsListViewScope() {
    init {
      Anvil.registerAttributeSetter(SdkSetter)
      Anvil.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
