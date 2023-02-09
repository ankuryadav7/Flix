package com.app.flix.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ContentResponse(

	@field:SerializedName("page")
	val page: Page? = null
) : Serializable

data class ContentItems(

	@field:SerializedName("content")
	val content: ArrayList<ContentItem?>? = null
)

data class ContentItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("poster-image")
	val posterImage: String? = null
)

data class Page(

	@field:SerializedName("page-num")
	val pageNum: String? = null,

	@field:SerializedName("page-size")
	val pageSize: String? = null,

	@field:SerializedName("content-items")
	val contentItems: ContentItems? = null,

	@field:SerializedName("total-content-items")
	val totalContentItems: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
