@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.EditText
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit

fun editText(configure: EditTextScope.() -> Unit = {}) = v<EditText>(configure.bind(EditTextScope))
abstract class EditTextScope : TextViewScope() {
  override fun init(arg: (EditText) -> Unit): Unit = initWith<EditText>(arg)
  fun selection(arg: Int): Unit = attr("selection", arg)
  companion object : EditTextScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
