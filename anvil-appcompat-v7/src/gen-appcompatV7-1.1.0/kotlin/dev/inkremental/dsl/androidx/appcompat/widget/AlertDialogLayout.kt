@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.androidx.appcompat.widget

import androidx.appcompat.widget.AlertDialogLayout
import dev.inkremental.Inkremental
import dev.inkremental.bind
import dev.inkremental.dsl.android.initWith
import dev.inkremental.dsl.androidx.appcompat.AppcompatV7Setter
import dev.inkremental.dsl.androidx.appcompat.CustomAppCompatv7Setter
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

fun alertDialogLayout(configure: AlertDialogLayoutScope.() -> Unit = {}) =
    v<AlertDialogLayout>(configure.bind(AlertDialogLayoutScope))
abstract class AlertDialogLayoutScope : LinearLayoutCompatScope() {
  @JvmName("initAlertDialogLayout")
  fun init(arg: (AlertDialogLayout) -> Unit): Unit = initWith<AlertDialogLayout>(arg)
  companion object : AlertDialogLayoutScope() {
    init {
      Inkremental.registerAttributeSetter(AppcompatV7Setter)
      Inkremental.registerAttributeSetter(CustomAppCompatv7Setter)
    }
  }
}
