package dev.inkremental

import kotlin.reflect.KClass

/**
 * Anvil class is a namespace for top-level static methods and interfaces. Most
 * users would only use it to call `Anvil.render()`.
 *
 * Internally, Anvil class defines how [Renderable] are mounted into Views
 * and how they are lazily rendered, and this is the key functionality of the
 * Anvil library.
 */
object Inkremental {

    /**
     * Represents currently rendered Mount point. Must be accessed from the
     * Renderable's view() method, otherwise it returns null
     */
    var currentMount: Mount? = null
        private set

    private val mounts: MutableMap<View, Mount> = WeakHashMap()
    private var uiHandler = UiHandler()
    private val viewFactories: MutableList<ViewFactory> = mutableListOf()
    private val attributeSetters: MutableList<AttributeSetter<Any>> = mutableListOf()

    init {
        initPlatform()
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

    /** Tags: arbitrary data bound to specific views, such as last cached attribute values  */
    private val tags: MutableMap<View, MutableMap<String, Any?>> = WeakHashMap()

    fun set(v: View, key: String, value: Any?) {
        val attrs = tags.getOrPut(v) { hashMapOf() }
        attrs[key] = value
    }

    fun get(v: View?, key: String): Any? {
        val attrs = tags[v] ?: return null
        return attrs[key]
    }

    /** Starts the new rendering cycle updating all mounted
     * renderables. Update happens in a lazy manner, only the values that has
     * been changed since last rendering cycle will be actually updated in the
     * views. This method can be called from any thread, so it's safe to use
     * `Anvil.render()` in background services.  */
    fun render() = uiHandler.runOnUiThread {
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
        mounts[v] = Mount(v, r)
        render(v)
        return v
    }

    /**
     * Unmounts a  mounted renderable. This would also clean up all the child
     * views inside the parent ViewGroup, which acted as a mount point.
     * @param v A mount point to unmount from its View
     */
    fun unmount(v: View?, removeChildren: Boolean = true) {
        val m = mounts[v]
        if (m != null) {
            mounts.remove(v)
            if (v is ViewGroup) {
                v.children.forEach { unmount(it) }
                if (removeChildren) {
                    v.removeAllChildren()
                }
            }
        }
    }

    /**
     * Returns currently rendered View. It allows to access the real view from
     * inside the [Renderable].
     */
    fun <T : View?> currentView(): T? =
        currentMount?.iterator?.currentView() as T?

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
        fun fromClass(c: Context?, v: KClass<out View>): View?
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
        internal val iterator = Iterator()

        internal inner class Iterator {
            var views: Deque<View?> = Deque()
            var indices: Deque<Int> = Deque()
            fun start() {
                require(views.size == 0)
                require(indices.size == 0)
                indices.push(0)
                val v = rootView.value
                if (v != null) {
                    views.push(v)
                }
            }

            fun start(c: KClass<out View>?, layoutId: Int) {
                val i = indices.peek()!!
                val parentView = (views.peek() ?: return) as? ViewGroup
                        ?: throw RuntimeException("child views are allowed only inside view groups")
                val vg = parentView
                var v: View? = null
                if (i < vg.childrenCount) {
                    v = vg.getChildAt(i)
                }
                val context = rootView.value!!.context
                if (c != null && (v == null || v::class != c)) {
                    vg.removeView(v)
                    for (vf in viewFactories) {
                        v = vf.fromClass(context, c)
                        if (v != null) {
                            set(v, "_anvil", 1)
                            vg.addView(v, i)
                            break
                        }
                    }
                } else if (c == null && (v == null || layoutId != get(v, "_layoutId"))) {
                    vg.removeView(v)
                    for (vf in viewFactories) {
                        v = vf.fromXml(vg, layoutId)
                        if (v != null) {
                            set(v, "_anvil", 1)
                            set(v, "_layoutId", layoutId)
                            vg.addView(v, i)
                            break
                        }
                    }
                }
                checkNotNull(v)
                views.push(v)
                indices.push(indices.pop() + 1)
                indices.push(0)
            }

            fun end() {
                val index = indices.peek()!!
                val v = views.peek()
                if (v != null && v is ViewGroup && get(v, "_layoutId") == null &&
                        (mounts[v] == null || mounts[v] === this@Mount)) {
                    val vg = v
                    if (index < vg.childrenCount) {
                        removeNonAnvilViews(vg, index, vg.childrenCount - index)
                    }
                }
                indices.pop()
                if (v != null) {
                    views.pop()
                }
            }

            fun <T : Any> attr(name: String, value: T?) {
                val currentView = views.peek() ?: return
                val currentValue = get(currentView, name) as? T?
                if (currentValue == null || currentValue != value) {
                    for (setter in attributeSetters) {
                        if (setter.set(currentView, name, value, currentValue)) {
                            set(currentView, name, value)
                            return
                        }
                    }
                }
            }

            private fun removeNonAnvilViews(vg: ViewGroup, start: Int, count: Int) {
                val end = start + count - 1
                for (i in end downTo start) {
                    val v = vg.getChildAt(i)
                    if (get(v, "_anvil") != null) {
                        vg.removeView(v)
                    }
                }
            }

            fun skip() {
                var i = indices.pop()
                val vg = views.peek() as ViewGroup
                while (i < vg.childrenCount) {
                    val v = vg.getChildAt(i)
                    if (get(v, "_anvil") != null) {
                        indices.push(i)
                        return
                    }
                    i++
                }
                indices.push(i)
            }

            fun currentView(): View? {
                return views.peek()
            }
        }
    }
}
