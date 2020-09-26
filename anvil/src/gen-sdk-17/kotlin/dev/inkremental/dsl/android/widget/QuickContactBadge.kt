@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.QuickContactBadge
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Array
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit

fun quickContactBadge(configure: QuickContactBadgeScope.() -> Unit = {}) =
    v<QuickContactBadge>(configure.bind(QuickContactBadgeScope))
abstract class QuickContactBadgeScope : ImageViewScope() {
  override fun init(arg: (QuickContactBadge) -> Unit): Unit = initWith<QuickContactBadge>(arg)
  fun excludeMimes(arg: Array<String>): Unit = attr("excludeMimes", arg)
  fun mode(arg: Int): Unit = attr("mode", arg)
  companion object : QuickContactBadgeScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
