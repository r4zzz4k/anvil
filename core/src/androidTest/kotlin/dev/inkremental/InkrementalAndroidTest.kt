package dev.inkremental

import android.widget.FrameLayout
import kotlin.test.BeforeTest

abstract class InkrementalAndroidTest: InkrementalTest() {
    @BeforeTest
    fun setUpAndroid() {
        container = MockLayout()
    }
}

open class MockView : View(null) {
    private var id = 0
    private var tag: Any? = null
    var text: String? = null

    override fun setId(id: Int) {
        this.id = id
    }

    override fun getId(): Int {
        return id
    }

    override fun getTag(): Any {
        return tag!!
    }

    override fun setTag(tag: Any) {
        this.tag = tag
    }

    override fun toString(): String {
        return "MockView$" + this.hashCode()
    }
}

open class MockLayout : FrameLayout(null) {
    private val children: MutableList<View> =
        ArrayList()
    private var id = 0
    private var tag: Any? = null
    var text: String? = null

    override fun getChildCount(): Int {
        return children.size
    }

    override fun getChildAt(index: Int): View {
        return children[index]
    }

    override fun indexOfChild(v: View): Int {
        return children.indexOf(v)
    }

    override fun addView(v: View, index: Int) {
        children.add(index, v)
        super.addView(v, index)
    }

    override fun removeView(v: View?) {
        children.remove(v)
        super.removeView(v)
    }

    override fun removeViews(start: Int, count: Int) {
        if (count > 0) {
            children.subList(start, start + count).clear()
        }
        super.removeViews(start, count)
    }

    override fun setId(id: Int) {
        this.id = id
    }

    override fun getId(): Int {
        return id
    }

    override fun getTag(): Any {
        return tag!!
    }

    override fun setTag(tag: Any) {
        this.tag = tag
    }

    override fun toString(): String {
        return "MockLayout$" + this.hashCode()
    }
}

class MockLayoutScope : Scope
