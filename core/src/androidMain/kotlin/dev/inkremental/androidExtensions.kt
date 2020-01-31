package dev.inkremental

import android.app.Activity
import android.app.Fragment
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import kotlin.math.roundToInt

// weight constants
sealed class Size {
    object MATCH : Size()
    object WRAP : Size()
    class EXACT(val size: Px) : Size()
}

inline class Sp(val value: Float)
inline class Dip(val value: Int)
inline class Px(val value: Int)

val r: Resources
    get() = Inkremental.currentView<View>()!!.resources

fun dip(value: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value,
    r.displayMetrics
)

fun dip(value: Int): Int = dip(value.toFloat()).roundToInt()

fun sip(value: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, value,
    r.displayMetrics
)

fun sip(value: Int): Int = sip(value.toFloat()).roundToInt()

val isPortrait: Boolean
    get() = r.configuration.orientation == Configuration.ORIENTATION_PORTRAIT


fun withId(@IdRes id: Int, r: () -> Unit): View {
    var v = Inkremental.currentView<View>()
    requireNotNull(v) { "Anvil.currentView() is null" }
    v = v.findViewById(id)
    requireNotNull(v) { "No view found for ID $id" } // TODO convert id to string
    return Inkremental.mount(v, r)
}

actual abstract class RootViewScope {
    val Int.dp : Dip
        get() = Dip(this)

    val Float.sp : Sp
        get() = Sp(this)

    val Int.px : Px
        get() = Px(this)

    val Int.sizeDp : Size.EXACT
        get() = Size.EXACT(this.dp.toPx())

    val Int.sizePx : Size.EXACT
        get() = Size.EXACT(this.px)

    fun Dip.toPx() : Px {
        return Px(dip(this.value))
    }
}

fun renderable(
    context: Context,
    r: () -> Unit
): View = object : RenderableView(context) {
    override var renderable: () -> Unit = {
        r()
    }
}

fun Activity.renderable(
    r: () -> Unit
): View = renderable(this, r)

fun Activity.renderableContentView(
    r: () -> Unit
): View = renderable(r).also { setContentView(it) }

fun Fragment.renderable(
    r: () -> Unit
): View = renderable(if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context else activity, r)

fun xml(@LayoutRes layoutId: Int, r: () -> Unit = {}) {
    Inkremental.currentMount?.iterator?.start(null, layoutId)
    r()
    end()
}
