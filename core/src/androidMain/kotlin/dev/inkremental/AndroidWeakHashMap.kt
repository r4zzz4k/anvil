package dev.inkremental

import java.util.WeakHashMap as JavaWeakHashMap

actual typealias WeakHashMap<K, V> = JavaWeakHashMap<K, V>
//actual fun <K, V> WeakHashMap(): WeakHashMap<K, V> = JavaWeakHashMap()
