package com.sneka.knowthecountries.ui
import com.sneka.knowthecountries.model.country

/*
The view interface for the country listing page
 */
interface CountryView {
    fun notifyAdapter(countryListFromNetwork:ArrayList<country>)
    fun showProgressBar()
    fun hideProgressBar()
}
