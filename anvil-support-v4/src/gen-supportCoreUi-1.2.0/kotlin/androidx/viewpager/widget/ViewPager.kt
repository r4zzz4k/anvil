@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package androidx.viewpager.widget

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

fun viewPager(configure: ViewPagerScope.() -> Unit = {}) =
    v<ViewPager>(configure.bind(ViewPagerScope))
abstract class ViewPagerScope : ViewGroupScope() {
  @JvmName("initViewPager")
  fun init(arg: (ViewPager) -> Unit): Unit = initWith<ViewPager>(arg)
  fun adapter(arg: PagerAdapter): Unit = attr("adapter", arg)
  fun currentItem(arg: Int): Unit = attr("currentItem", arg)
  fun offscreenPageLimit(arg: Int): Unit = attr("offscreenPageLimit", arg)
  fun pageMargin(arg: Int): Unit = attr("pageMargin", arg)
  fun pageMarginDrawable(arg: Drawable?): Unit = attr("pageMarginDrawable", arg)
  fun pageMarginDrawable(arg: Int): Unit = attr("pageMarginDrawable", arg)
  companion object : ViewPagerScope() {
    init {
      Inkremental.registerAttributeSetter(SupportCoreUiSetter)
      Inkremental.registerAttributeSetter(CustomSupportV4Setter)
    }
  }
}
