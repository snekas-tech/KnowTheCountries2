package com.sneka.knowthecountries.ui
import com.sneka.knowthecountries.model.country

/*
The view interface for the country details page
 */
interface CountryDetailsView {
    fun bindDataForCountry(country: country)
}
