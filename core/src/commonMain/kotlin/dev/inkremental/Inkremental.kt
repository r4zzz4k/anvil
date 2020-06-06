package dev.inkremental

import dev.inkremental.platform.*
import kotlin.jvm.JvmOverloads
import kotlin.reflect.KClass

/**
 * Inkremental class is a namespace for top-level static methods and interfaces. Most
 * users would only use it to call `Inkremental.render()`.
 *
 * Internally, Inkremental class defines how [Renderable] are mounted into Views
 * and how they are lazily rendered, and this is the key functionality of the
 * Inkremental library.
 */
object Inkremental {
    private val mounts: MutableMap<View, Mount> = WeakHashMap()
    private var currentMount: Mount? = null
    private val viewFactories: MutableList<ViewFactory> = mutableListOf()
    private val attributeSetters: MutableList<AttributeSetter<Any>> = mutableListOf()
    private val renderUiSwitcher: UiSwitcher = UiSwitcher { render() }

    init {
        registerAttributeSetter(RootAttributeSetter())
        platformInit()
    }

    fun registerViewFactory(viewFactory: ViewFactory) {
        if (!viewFactories.contains(viewFactory)) {
            viewFactories.add(0, viewFactory)
        }
    }

    fun registerAttributeSetter(setter: AttributeSetter<Any>) {
        if (!attributeSetters.contains(setter)) {
            attributeSetters.add(0, setter)
        }
    }

    /** Starts the new rendering cycle updating all mounted
     * renderables. Update happens in a lazy manner, only the values that has
     * been changed since last rendering cycle will be actually updated in the
     * views. This method can be called from any thread, so it's safe to use
     * `Anvil.render()` in background services.  */
    fun render() { // If Anvil.render() is called on a non-UI thread, use UI Handler
        if(renderUiSwitcher.scheduleOnUi()) return
        mounts.values.toSet().forEach { render(it) }
    }

    /**
     * Mounts a renderable function defining the layout into a View. If host is a
     * viewgroup it is assumed to be empty, so the Renderable would define what
     * its child views would be.
     * @param v a View into which the renderable r will be mounted
     * @param r a Renderable to mount into a View
     */
    fun <T : View> mount(v: T, r: () -> Unit): T {
        val m = Mount(v, r)
        mounts[v] = m
        render(v)
        return v
    }

    /**
     * Unmounts a  mounted renderable. This would also clean up all the child
     * views inside the parent ViewGroup, which acted as a mount point.
     * @param v A mount point to unmount from its View
     */
    @JvmOverloads
    fun unmount(v: View?, removeChildren: Boolean = true) {
        val m = mounts[v]
        if (m != null) {
            mounts.remove(v)
            m.cleanTags()
            if (v is ViewGroup) {
                val viewGroup = v
                val childCount = viewGroup.childrenCount
                for (i in 0 until childCount) {
                    unmount(viewGroup.childAt(i))
                }
                if (removeChildren) {
                    viewGroup.clearChildren()
                }
            }
        }
    }

    /**
     * Returns currently rendered Mount point. Must be called from the
     * Renderable's view() method, otherwise it returns null
     * @return current mount point
     */
    fun currentMount(): Mount? {
        return currentMount
    }

    /**
     * Returns currently rendered View. It allows to access the real view from
     * inside the [Renderable].
     */
    fun <T : View?> currentView(): T? {
        return if (currentMount == null) {
            null
        } else currentMount?.iterator?.currentView() as T?
    }

    fun render(v: View?) {
        val m = mounts[v] ?: return
        render(m)
    }

    fun render(m: Mount) {
        if (m.lock) {
            return
        }
        m.lock = true
        val prev = currentMount
        currentMount = m
        m.iterator.start()
        m.renderable()
        m.iterator.end()
        currentMount = prev
        m.lock = false
    }

    interface ViewFactory {
        fun fromClass(parent: ViewGroup, viewClass: KClass<out View>): View?
        fun fromXml(parent: ViewGroup, xmlId: Int): View?
    }

    interface AttributeSetter<T> {
        operator fun set(v: View, name: String, value: T?, prevValue: T?): Boolean
    }

    /** Mount describes a mount point. Mount point is a Renderable function
     * attached to some ViewGroup. Mount point keeps track of the virtual layout
     * declared by Renderable  */
    class Mount(v: View, val renderable: () -> Unit) {
        var lock = false
        private val rootView: WeakReference<View> = WeakReference(v)
        val iterator = Iterator()

        /** Tags: arbitrary data bound to specific views, such as last cached attribute values  */
        private val tags: MutableMap<View, MutableMap<String, Any?>> = WeakHashMap()

        operator fun set(v: View, key: String, value: Any?) {
            val attrs = tags.getOrPut(v) { mutableMapOf() }
            attrs[key] = value
        }

        operator fun get(v: View?, key: String): Any? {
            return tags[v]?.get(key)
        }

        fun cleanTags() {
            tags.clear()
        }

        @OptIn(ExperimentalStdlibApi::class)
        inner class Iterator {
            var views: ArrayDeque<View?> = ArrayDeque()
            var indices: ArrayDeque<Int> = ArrayDeque()
            fun start() {
                require(views.size == 0)
                require(indices.size == 0)
                indices.addLast(0)
                val v = rootView.get()
                if (v != null) {
                    views.addLast(v)
                }
            }

            fun start(c: KClass<out View>?, layoutId: Int) {
                val i = indices.last()
                val parentView = (views.last() ?: return) as? ViewGroup
                    ?: throw RuntimeException("child views are allowed only inside view groups")
                val vg = parentView
                var v: View? = null
                if (i < vg.childrenCount) {
                    v = vg.childAt(i)
                }
                if (c != null && (v == null || v::class != c)) {
                    vg.removeChild(v)
                    for (vf in viewFactories) {
                        v = vf.fromClass(vg, c)
                        if (v != null) {
                            set(v, "_anvil", 1)
                            vg.addChild(v, i)
                            break
                        }
                    }
                } else if (c == null && (v == null || layoutId != get(v, "_layoutId"))) {
                    vg.removeChild(v)
                    for (vf in viewFactories) {
                        v = vf.fromXml(vg, layoutId)
                        if (v != null) {
                            set(v, "_anvil", 1)
                            set(v, "_layoutId", layoutId)
                            vg.addChild(v, i)
                            break
                        }
                    }
                }
                check(v != null)
                views.addLast(v)
                indices.addLast(indices.removeLast() + 1)
                indices.addLast(0)
            }

            fun end() {
                val index = indices.last()
                val v = views.last()
                if (v != null && v is ViewGroup && get(v, "_layoutId") == null &&
                    (mounts[v] == null || mounts[v] === this@Mount)) {
                    val vg = v
                    if (index < vg.childrenCount) {
                        removeNonAnvilViews(vg, index, vg.childrenCount - index)
                    }
                }
                indices.removeLast()
                if (v != null) {
                    views.removeLast()
                }
            }

            fun <T : Any> attr(name: String, value: T?) {
                val currentView = views.last() ?: return
                val currentValue = get(currentView, name) as T?
                if (currentValue == null || (currentValue != value && name != ATTR_INIT)) {
                    for (setter in attributeSetters) {
                        if (setter.set(currentView, name, value, currentValue)) {
                            set(currentView, name, if (name != ATTR_INIT) value else true)
                            return
                        }
                    }
                }
            }

            private fun removeNonAnvilViews(vg: ViewGroup, start: Int, count: Int) {
                val end = start + count - 1
                for (i in end downTo start) {
                    val v = vg.childAt(i)
                    if (get(v, "_anvil") != null) {
                        vg.removeChild(v)
                    }
                }
            }

            fun skip() {
                val vg = views.last() as ViewGroup
                var i: Int = indices.removeLast()
                while (i < vg.childrenCount) {
                    val v = vg.childAt(i)
                    if (get(v, "_anvil") != null) {
                        indices.addLast(i)
                        return
                    }
                    i++
                }
                indices.addLast(i)
            }

            fun currentView(): View? {
                return views.lastOrNull()
            }
        }

    }
}
