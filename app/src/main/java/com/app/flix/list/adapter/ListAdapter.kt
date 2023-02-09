package com.app.flix.list.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.app.flix.R
import com.app.flix.bean.ContentItem
import com.app.flix.utils.ImageHelper


/**
 * Adapter class used for display content in list form.
 */
class ListAdapter(private var itemsList: ArrayList<ContentItem?>?, private var context:Context) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var searchedQuery: String? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var previewImage: ImageView = view.findViewById(com.app.flix.R.id.previewImage)
        var contentTitle: TextView = view.findViewById(com.app.flix.R.id.contentTitle)
    }

    /**
     * used for passing the layout of list item
     */
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    /**
     * used for handling dynamic ui parts example, image and title
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        itemsList?.let {
            it.let {
                val item = it[position]
                holder.contentTitle.isSelected = true
                /** displaying the title from model */
                holder.contentTitle.text=item!!.name
                /** passing the image and poster name to helper class */
                item.posterImage?.let {
                    ImageHelper.init(context).loadImageTo(holder.previewImage,item.posterImage)
                } ?: run {
                    ImageHelper.init(context).loadImageTo(holder.previewImage,"")
                }
                item.name?.let {
                    checkSpan(item.name,holder.contentTitle)
                }
            }
        }

        holder.itemView.setOnClickListener {
            itemsList?.let {
                Toast.makeText(context, it[position]?.name, Toast.LENGTH_SHORT).show()
            }
        }

    }

    /** getting the index of searched text*/
    private fun checkSpan(name: String?, textView: TextView) {
        searchedQuery?.let {
            val iStart = name!!.lowercase().indexOf(searchedQuery!!.lowercase())
            val end = iStart+searchedQuery!!.length
            textView.text = Color.YELLOW.queryTextColor(name, iStart, end)
        }
    }

    /**
     * will update the color of query text dynamically
     */
    private fun Int.queryTextColor(inputText: String, startIndex: Int, endIndex: Int):Spannable{
        val outPutColoredText: Spannable = SpannableString(inputText)
        outPutColoredText.setSpan(
            ForegroundColorSpan(this), startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return outPutColoredText
    }
    override fun getItemCount(): Int {
        return itemsList!!.size
    }

    /**
     * update itemsList from filtered listItems
     */
    fun filteredList(filterList: ArrayList<ContentItem?>,searchedQuery:String) {
        this.itemsList = filterList
        this.searchedQuery=searchedQuery
        notifyDataSetChanged()
    }

    /**
     * update itemsList from new data, getting from pagination
     */
    fun notifyAdapter(mainContentList: ArrayList<ContentItem?>?) {
        val size=mainContentList?.size
        this.itemsList=mainContentList
        this.searchedQuery=""
        notifyItemChanged(size!!)
    }

    /**
     * reset adapter itemsList from actual list
     */
    fun resetAdapter(mainContentList: ArrayList<ContentItem?>?) {
        this.itemsList=mainContentList
        this.searchedQuery=""
        notifyDataSetChanged()
    }
}