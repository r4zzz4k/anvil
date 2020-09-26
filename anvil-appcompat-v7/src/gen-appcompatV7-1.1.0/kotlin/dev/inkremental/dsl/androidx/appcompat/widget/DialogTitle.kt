@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.DialogTitle
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun dialogTitle(configure: DialogTitleScope.() -> Unit = {}) =
    v<DialogTitle>(configure.bind(DialogTitleScope))
abstract class DialogTitleScope : AppCompatTextViewScope() {
  @JvmName("initDialogTitle")
  fun init(arg: (DialogTitle) -> Unit): Unit = initWith<DialogTitle>(arg)
  companion object : DialogTitleScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
