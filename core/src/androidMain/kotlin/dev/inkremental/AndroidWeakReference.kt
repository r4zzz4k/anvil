package dev.inkremental

import java.lang.ref.WeakReference as JavaWeakReference

actual typealias WeakReference<T> = JavaWeakReference<T>

actual val <T> WeakReference<T>.value: T?
    get() = get()
