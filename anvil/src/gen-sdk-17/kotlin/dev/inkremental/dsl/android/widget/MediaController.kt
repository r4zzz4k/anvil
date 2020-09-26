@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.widget

import android.view.View
import android.widget.MediaController
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.bind
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.initWith
import dev.inkremental.v
import kotlin.Suppress
import kotlin.Unit

fun mediaController(configure: MediaControllerScope.() -> Unit = {}) =
    v<MediaController>(configure.bind(MediaControllerScope))
abstract class MediaControllerScope : FrameLayoutScope() {
  override fun init(arg: (MediaController) -> Unit): Unit = initWith<MediaController>(arg)
  fun anchorView(arg: View): Unit = attr("anchorView", arg)
  fun mediaPlayer(arg: MediaController.MediaPlayerControl): Unit = attr("mediaPlayer", arg)
  companion object : MediaControllerScope() {
    init {
      Inkremental.registerAttributeSetter(SdkSetter)
      Inkremental.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
