package dev.inkremental

import kotlin.reflect.KClass
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class InkrementalTest {
    var createdViews: MutableMap<KClass<*>, Int?> = mutableMapOf()
    var changedAttrs: MutableMap<String, Int?> = mutableMapOf()
    lateinit var container: ViewGroup

    @BeforeTest
    fun setUp() {
        changedAttrs.clear()
        createdViews.clear()
    }

    @AfterTest
    fun tearDown() {
        Inkremental.unmount(container)
    }

    val context: Context?
        get() = null

    init {
        Inkremental.registerAttributeSetter(object : Inkremental.AttributeSetter<Any> {
            override fun set(v: View, name: String, value: Any?, prevValue: Any?): Boolean {
                changedAttrs[name] = if (!changedAttrs.containsKey(name)) 1 else changedAttrs[name]!! + 1
                return false
            }
        })
        Inkremental.registerViewFactory(object : Inkremental.ViewFactory {
            override fun fromClass(c: Context?, v: KClass<out View>): View? {
                createdViews[v] = if (!createdViews.containsKey(v)) 1 else createdViews[v]!! + 1
                return null
            }
            override fun fromXml(parent: ViewGroup, xmlId: Int): View? = null
        })
    }
}
