@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.widget.DatePicker
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Boolean
import kotlin.Long
import kotlin.Suppress
import kotlin.Unit

fun datePicker(configure: DatePickerScope.() -> Unit = {}) =
    v<DatePicker>(configure.bind(DatePickerScope))
abstract class DatePickerScope : FrameLayoutScope() {
  override fun init(arg: (DatePicker) -> Unit): Unit = initWith<DatePicker>(arg)
  fun calendarViewShown(arg: Boolean): Unit = attr("calendarViewShown", arg)
  fun maxDate(arg: Long): Unit = attr("maxDate", arg)
  fun minDate(arg: Long): Unit = attr("minDate", arg)
  fun spinnersShown(arg: Boolean): Unit = attr("spinnersShown", arg)
  companion object : DatePickerScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
