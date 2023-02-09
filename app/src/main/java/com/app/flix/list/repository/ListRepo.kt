package com.app.flix.list.repository

/**
 * Used for define the methods that need to be invoke for calling local source data or server data
 */
interface ListRepo {
    fun getContent(count:Int)
}