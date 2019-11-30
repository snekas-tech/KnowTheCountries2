package com.sneka.knowthecountries.ui

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.graphics.Rect;
import android.widget.Toast
import com.sneka.knowthecountries.R
import com.sneka.knowthecountries.adapter.countryListAdapter
import com.sneka.knowthecountries.model.country
import com.sneka.knowthecountries.presenter.ApiClient
import com.sneka.knowthecountries.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar;
import com.sneka.knowthecountries.presenter.CountryPresenter

/*
Author name: Sneka Shanmughasundaram
Purpose: This is the Main Activity and the view is displayed to the user.
 */
class MainActivity : AppCompatActivity(),CountryView {
    var countryList = ArrayList<country>()
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var adapter: countryListAdapter? = null
    var swipeLayout: SwipeRefreshLayout? = null
    val llm: LinearLayoutManager? = null
    var searchView: SearchView? = null
    var toolbar: Toolbar? = null
    var countryPresenter:CountryPresenter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countryPresenter= CountryPresenter(this,this);
        setContentView(R.layout.activity_main)
        prepareViews()
        getDataFromAPI()
    }



    fun prepareViews() {
        prepareToolBar()
        prepareRecyclerView()
        prepareProgressBar()
        prepareSwipeRefresh()
        prepareAdapter(countryList)
    }

    override fun showProgressBar()
    {
        progressBar!!.visibility=View.VISIBLE
        if (swipeLayout!!.isRefreshing) {
            swipeLayout!!.setRefreshing(false)
        }
    }

    override fun hideProgressBar()
    {
        progressBar!!.visibility=View.GONE
    }

    fun prepareToolBar() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    fun prepareSearchView(menu: Menu?) {
        var menuItem = menu!!.findItem(R.id.search)

        //getting search view reference from menu item
        searchView = menuItem.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView!!.queryHint = "enter country name"
        searchView!!.setSearchableInfo(
            searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Int.MAX_VALUE

        //query text listener for search view
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
               adapter!!.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                adapter!!.getFilter().filter(query)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        prepareSearchView(menu)
        return true
    }

    fun prepareRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.addItemDecoration(MarginItemDecoration(
            resources.getDimension(R.dimen.default_padding).toInt()))
    }

    fun prepareAdapter(countryList: ArrayList<country>) {
        adapter = countryListAdapter(countryList)
        recyclerView!!.adapter = adapter
    }

    fun prepareSwipeRefresh(){
        swipeLayout = findViewById(R.id.swipe_layout)
        var onRefreshListener = object :SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                countryPresenter?.makeGetAllCountryNetworkCall()
            }
        }
        swipeLayout!!.setOnRefreshListener(onRefreshListener)
    }

    fun prepareProgressBar() {
        progressBar = findViewById(R.id.progressBar)
    }

    fun getDataFromAPI() {
        if (AppConstants.isConnected(applicationContext)) {
            //network call for getting country list
            countryPresenter?.makeGetAllCountryNetworkCall()
        } else {
            showNetworkWarning()
        }
    }

    fun showNetworkWarning() {
        alert("Network connection not available", "Warning") {
            positiveButton("OK") {
                finish()
            }
        }.show()
    }

    override fun notifyAdapter(countryListForNetwork: ArrayList<country>) {
        countryList.clear()
        countryList.addAll(countryListForNetwork)
        adapter!!.notifyDataSetChanged()
    }


}

class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceHeight
            }
            left =  spaceHeight
            right = spaceHeight
            bottom = spaceHeight
        }
    }
}
