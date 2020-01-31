package dev.inkremental

import android.view.LayoutInflater
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

internal class DefaultViewFactory : Inkremental.ViewFactory {
    override fun fromClass(c: Context?, viewClass: KClass<out View>): View? {
        return try {
            viewClass.java.getConstructor(Context::class.java).newInstance(c)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        }
    }

    override fun fromXml(parent: ViewGroup, xmlId: Int): View? {
        return LayoutInflater.from(parent.context).inflate(xmlId, parent, false)
    }
}
