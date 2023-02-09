package com.app.flix.list.viewModel

import androidx.lifecycle.ViewModel
import com.app.flix.list.implementation.ListRepoImpl

/**
 * Listing ViewModel
 */
class ListViewModel(private val listRepoImpl: ListRepoImpl) : ViewModel() {

    /** calling repo method */
    fun fetchContentData(count:Int) {
        listRepoImpl.getContent(count)
    }

    /** observing the live data update */
    val contentData = listRepoImpl.contentResponse
}