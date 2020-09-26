@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package androidx.viewpager.widget

import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.androidx.core.CustomSupportV4Setter
import dev.inkremental.dsl.androidx.core.SupportCoreUiSetter
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun pagerTabStrip(configure: PagerTabStripScope.() -> Unit = {}) =
    v<PagerTabStrip>(configure.bind(PagerTabStripScope))
abstract class PagerTabStripScope : PagerTitleStripScope() {
  @JvmName("initPagerTabStrip")
  fun init(arg: (PagerTabStrip) -> Unit): Unit = initWith<PagerTabStrip>(arg)
  fun drawFullUnderline(arg: Boolean): Unit = attr("drawFullUnderline", arg)
  fun tabIndicatorColor(arg: Int): Unit = attr("tabIndicatorColor", arg)
  fun tabIndicatorColorResource(arg: Int): Unit = attr("tabIndicatorColorResource", arg)
  companion object : PagerTabStripScope() {
    init {
      Inkremental.registerAttributeSetter(SupportCoreUiSetter)
      Inkremental.registerAttributeSetter(CustomSupportV4Setter)
    }
  }
}
