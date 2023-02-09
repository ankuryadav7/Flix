package com.app.flix.list.implementation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.flix.App
import com.app.flix.bean.ContentResponse
import com.app.flix.list.repository.ListRepo
import com.app.flix.utils.NetworkResult
import com.app.flix.utils.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

/**
 * Used for handling data from local source or server
 */
class ListRepoImpl : ListRepo{
    private var jsonString: String? = null
    private var gson = Gson()
    private var `is` : InputStream? = null
    /** live data object for posting the value to UI */
    val contentResponse = MutableLiveData<NetworkResult<ContentResponse>>()
    override fun getContent(count:Int) {
        contentResponse.postValue(NetworkResult.Loading())
        if (NetworkUtils.isNetworkAvailable) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    callJson(count)
                }
            }
    }else{
            contentResponse.postValue(NetworkResult.Error("",401,null))
        }
}

    private fun callJson(count: Int) {
        /**
        if counter is less than or equal to 2 will will fetch the json from asset folder
        if counter is greater than 2 will will not doing anything
        */
        if (count>2){
            return
        }
        jsonString = try {
            `is` = when (count) {
                0 -> {
                    App.getContext().assets.open("CONTENTLISTINGPAGE-PAGE1.json")
                }
                1 -> {
                    App.getContext().assets.open("CONTENTLISTINGPAGE-PAGE2.json")
                }
                else -> {
                    App.getContext().assets.open("CONTENTLISTINGPAGE-PAGE3.json")
                }
            }

            val size = `is`!!.available()
            val buffer = ByteArray(size)
            `is`!!.read(buffer)
            `is`!!.close()
            String(buffer, charset("UTF-8"))
        } catch (e: IOException) {
        }.toString()
        getJsonObject(jsonString!!)

    }

    private fun getJsonObject(jsonString: String) {
        /** converting json to class object using gson */
        val contentModel = gson.fromJson(jsonString, ContentResponse::class.java)
        contentModel?.let {
            contentResponse.postValue(NetworkResult.Success(it))
        }?: run {
            contentResponse.postValue(NetworkResult.Error("",500,null))
        }
    }


}