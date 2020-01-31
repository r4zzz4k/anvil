package dev.inkremental

expect class WeakReference<T> constructor(referent: T?)

expect val <T> WeakReference<T>.value: T?
