package com.app.flix.list.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.app.flix.R
import com.app.flix.base.BaseBindingActivity
import com.app.flix.bean.ContentItem
import com.app.flix.databinding.ActivityListBinding
import com.app.flix.list.adapter.ListAdapter
import com.app.flix.list.viewModel.ListViewModel
import com.app.flix.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class ListActivity : BaseBindingActivity<ActivityListBinding>() {
    /**
     * initializing the viewmodel.
     */
    private val railsViewModel by viewModel<ListViewModel>()
    private var listAdapter : ListAdapter? = null
    var isLoading : Boolean = false
    var count : Int = 0
    private var layoutManager : GridLayoutManager? =null
    private var mainContentList: ArrayList<ContentItem?>? = ArrayList()

    /**
     * setup the layout binding
     * */
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityListBinding {
        return ActivityListBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiComponents()
        /**
         * call viewmodel model for fetching content list
         * */
        railsViewModel.fetchContentData(count)
        fetchingData()
    }

    private fun uiComponents() {
        /** applying click on search icon */
        getBinding()!!.toolbar.search.setOnClickListener {
            /**
                when user click on search icon, title toolbar will be gone and
                search toolbar will be visible
            */
            getBinding()!!.toolbar.relativeLayout.visibility= View.GONE
            getBinding()!!.toolbar.toolbar.searchRelativeLayout.visibility= View.VISIBLE
            getBinding()!!.toolbar.toolbar.searchEdt.requestFocus()
        }

        getBinding()!!.toolbar.back.setOnClickListener {
            finish()
        }

        getBinding()!!.toolbar.toolbar.crossIcon.setOnClickListener {
            /**
                when user click on cross(cancel) icon, title toolbar will be visible and
                search toolbar will be gone
            */
            getBinding()!!.toolbar.relativeLayout.visibility= View.VISIBLE
            getBinding()!!.toolbar.toolbar.searchRelativeLayout.visibility= View.GONE
            getBinding()!!.toolbar.toolbar.searchEdt.apply {
                text.clear()
            }
            hideSoftKeyboard(getBinding()!!.toolbar.toolbar.searchEdt)
            if (listAdapter!=null) {
                getBinding()!!.recyclerView.post { listAdapter?.resetAdapter(mainContentList) }
            }
        }
        addTextWatcher()
    }


    private fun fetchingData() {
        /** observe the live data for latest response */
        railsViewModel.contentData.observe(this@ListActivity) { it ->
            when (it) {
                is NetworkResult.Loading -> {
                    /** we can display loader here */
                }
                is NetworkResult.Success -> {
                    /** received data from repo, and display into recyclerview */
                    isLoading=false
                    it.data?.page?.contentItems?.content?.let {
                        setupRecyclerView(it)
                    } ?: run {
                        // If data,page, contentItems or content is null.
                        Toast.makeText(this@ListActivity,resources.getString(R.string.no_data),Toast.LENGTH_SHORT).show()
                    }

                }
                is NetworkResult.Error -> {
                    /** here we can handle error condition

                    Note: 401 is just a dummy error code, we define these code based on requirement
                    or handle according to the server error codes
                    */
                    if (it.code==401){
                        Toast.makeText(this@ListActivity,resources.getString(R.string.connection_error),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun setupRecyclerView(it: ArrayList<ContentItem?>) {
        /**
        check for load more functionality
        if adapter is null we will create a new object of adapter
        if the adapter is not null we will just notifyAdapter
        */
        if (listAdapter!=null) {
            for (item in it){
                mainContentList!!.add(item)
            }
            getBinding()!!.recyclerView.post { listAdapter?.notifyAdapter(mainContentList) }
        }else{
            mainContentList!!.addAll(it)
            RecyclerAnimator(this).animate(getBinding()!!.recyclerView)
            listAdapter = ListAdapter(mainContentList,this@ListActivity)
            layoutManager = GridLayoutManager(applicationContext,resources.getInteger(R.integer.grid_column_count))
            getBinding()!!.recyclerView.layoutManager = layoutManager
            getBinding()!!.recyclerView.adapter = listAdapter
            addPagination()
        }

    }



    private fun addTextWatcher() {
        /**
       adding a TextChangedListener
       to call a method whenever there is some change on the EditText
       */
        getBinding()!!.toolbar.toolbar.searchEdt.addTextChangedListener(TextWatcherHandler(
            afterChanged = {
                /** after the change, calling the method and passing the search input */
                if (it.toString().length>= InterfaceImp.searchQueryCount){
                    filterList(it.toString())
                }
                else {
                    /** reset the adapter if the searchQuery count is less than 3 */
                    if (listAdapter != null) {
                        getBinding()!!.recyclerView.post {
                            listAdapter?.resetAdapter(
                                mainContentList
                            )
                        }
                    }
                }
            }
        ))
    }

    private fun filterList(toString: String) {
        /** new array list that will hold the filtered data */
        val filteredList: ArrayList<ContentItem?> = ArrayList()

        /** looping through existing elements */
        for (s in mainContentList!!) {
            /** if the existing elements contains the search input */
            if (s!!.name!!.lowercase(Locale.getDefault()).contains(toString.lowercase(Locale.ROOT))) {
                /** adding the element to filtered list */
                filteredList.add(s)
            }
        }

        filteredList.let {
            /** check size of filtered list, if size is 0 than show a message to user
            else show result
            */
            if (it.size==0){
                Toast.makeText(this@ListActivity,resources.getString(R.string.no_result),Toast.LENGTH_LONG).show()
            }
            /** calling a method of the adapter class and passing the filtered list */
            listAdapter?.filteredList(filteredList,toString)
        }
    }



    private fun addPagination() {
        getBinding()!!.recyclerView.addOnScrollListener(object :
            PaginationScrollListener(getBinding()!!.recyclerView.layoutManager as GridLayoutManager) {
            override fun loadMoreItems() {
                /** check=> if search box is visible than load more will be ignored */
                if (getBinding()!!.toolbar.toolbar.searchRelativeLayout.visibility==View.GONE){
                    if (!isLoading) {
                        isLoading = true
                        count++
                        /** calling fresh data from repo based on counter value(max=2) */
                        railsViewModel.fetchContentData(count)
                    }
                }
            }
        })

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        /** Checks the orientation of the screen */
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) layoutManager!!.spanCount=resources.getInteger(R.integer.grid_column_count) else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) layoutManager!!.spanCount=resources.getInteger(R.integer.grid_column_count)
    }

}