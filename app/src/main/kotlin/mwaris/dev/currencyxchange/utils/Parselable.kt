package mwaris.dev.currencyxchange.utils

interface Parselable<T> {

    fun parse(): T?
}