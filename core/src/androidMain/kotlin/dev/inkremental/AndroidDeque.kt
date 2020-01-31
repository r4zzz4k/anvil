package dev.inkremental

import java.util.*
import java.util.Deque as JavaDeque

actual typealias Deque<T> = JavaDeque<T>
actual fun <T> Deque(): Deque<T> = ArrayDeque()

actual val <T> Deque<T>.size: Int
    get() = size
actual fun <T> Deque<T>.push(item: T) = push(item)
actual fun <T> Deque<T>.peek(): T? = peek()
actual fun <T> Deque<T>.pop(): T = pop()
