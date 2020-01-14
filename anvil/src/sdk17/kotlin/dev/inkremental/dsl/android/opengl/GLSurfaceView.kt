@file:Suppress("DEPRECATION", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "unused")

package dev.inkremental.dsl.android.opengl

import android.opengl.GLSurfaceView
import dev.inkremental.dsl.android.CustomSdkSetter
import dev.inkremental.dsl.android.SdkSetter
import dev.inkremental.dsl.android.view.SurfaceViewScope
import kotlin.Boolean
import kotlin.Int
import kotlin.Suppress
import kotlin.Unit
import trikita.anvil.Anvil
import trikita.anvil.attr
import trikita.anvil.bind
import trikita.anvil.v

fun gLSurfaceView(configure: GLSurfaceViewScope.() -> Unit = {}) =
    v<GLSurfaceView>(configure.bind(GLSurfaceViewScope))
abstract class GLSurfaceViewScope : SurfaceViewScope() {
  fun debugFlags(arg: Int): Unit = attr("debugFlags", arg)
  fun eGLConfigChooser(arg: GLSurfaceView.EGLConfigChooser): Unit = attr("eGLConfigChooser", arg)
  fun eGLConfigChooser(arg: Boolean): Unit = attr("eGLConfigChooser", arg)
  fun eGLContextClientVersion(arg: Int): Unit = attr("eGLContextClientVersion", arg)
  fun eGLContextFactory(arg: GLSurfaceView.EGLContextFactory): Unit = attr("eGLContextFactory", arg)
  fun eGLWindowSurfaceFactory(arg: GLSurfaceView.EGLWindowSurfaceFactory): Unit =
      attr("eGLWindowSurfaceFactory", arg)
  fun gLWrapper(arg: GLSurfaceView.GLWrapper): Unit = attr("gLWrapper", arg)
  fun preserveEGLContextOnPause(arg: Boolean): Unit = attr("preserveEGLContextOnPause", arg)
  fun renderMode(arg: Int): Unit = attr("renderMode", arg)
  fun renderer(arg: GLSurfaceView.Renderer): Unit = attr("renderer", arg)
  companion object : GLSurfaceViewScope() {
    init {
      Anvil.registerAttributeSetter(SdkSetter)
      Anvil.registerAttributeSetter(CustomSdkSetter)
    }
  }
}
