package dev.inkremental

expect interface Deque<T>
expect fun <T> Deque(): Deque<T>

expect val <T> Deque<T>.size: Int
expect fun <T> Deque<T>.push(item: T)
expect fun <T> Deque<T>.peek(): T?
expect fun <T> Deque<T>.pop(): T

