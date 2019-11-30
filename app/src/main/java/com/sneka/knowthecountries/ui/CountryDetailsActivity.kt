package com.sneka.knowthecountries.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sneka.knowthecountries.model.country
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.sneka.knowthecountries.R
import com.sneka.knowthecountries.utils.AppConstants
import com.sneka.knowthecountries.presenter.ApiClient
import com.sneka.knowthecountries.presenter.CountryDetailsPresenter
import com.sneka.knowthecountries.presenter.CountryPresenter

/*
Author name: Sneka Shanmughasundaram
Purpose: The CountryDetails Activity is to display more details related to the country and also display the details
in Google maps.
 */
class CountryDetailsActivity : AppCompatActivity(), OnMapReadyCallback,CountryDetailsView {
    var mapFragment: SupportMapFragment? = null
    var capitalTV: TextView? = null
    var areaTV: TextView? = null
    var populationTV: TextView? = null
    var regionTV: TextView? = null
    var timeZoneTV: TextView? = null
    var languageTV: TextView? = null
    var currencyTV: TextView? = null
    var imageView: ImageView? = null
    var nameTV: TextView? = null
    var globalLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.countrylistdetail)

        // getting the 2 word country alpha code from main activity
        var alphaCode = intent.getStringExtra(AppConstants.ALPHA_2_CODE_EXTRA)

        prepareView()
        val countryPresenter: CountryDetailsPresenter
        countryPresenter= CountryDetailsPresenter(this,this);
        // make get country network call
        countryPresenter.makeGetCountryNetworkCall(alphaCode)
    }



    fun prepareMap() {
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }

    // place marker on the country geo graphic location
    fun placeMarker(googleMap: GoogleMap, latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,4.5f))
        googleMap.addMarker(markerOptions)
    }



    fun prepareView() {
        imageView = findViewById(R.id.flag_image_view_detail)
        nameTV = findViewById(R.id.name_text_view_detail)
        capitalTV = findViewById(R.id.capital_text_view_detail)
        areaTV = findViewById(R.id.area_text_view_detail)
        populationTV = findViewById(R.id.population_text_view_detail)
        regionTV = findViewById(R.id.region_text_view_detail)
        timeZoneTV = findViewById(R.id.time_zone_text_view_detail)
        languageTV = findViewById(R.id.language_text_view_detail)
        currencyTV = findViewById(R.id.currency_text_view_detail)
    }

    override fun bindDataForCountry(country: country) {
        var latLng = LatLng(country.latlng[0],country.latlng[1])
        globalLatLng  = latLng
        imageView = findViewById(R.id.flag_image_view_detail)
        Picasso
            .with(this)
            .load(AppConstants.BASE_URL_FLAG + country.alpha2Code + ".png")
            .into(imageView)

        nameTV!!.text = country.name
        capitalTV!!.text = country.capital
        areaTV!!.text = country.area.toString() + " km^2"
        populationTV!!.text = country.population.toString()
        regionTV!!.text = country.region + ", " + country.subregion
        timeZoneTV!!.text = country.timezones[0]
        languageTV!!.text = country.languages[0].name
        currencyTV!!.text = country.currencies[0].name + " " + "(" + country.currencies[0].symbol + ")"

        Picasso
            .with(this)
            .load(AppConstants.BASE_URL_FLAG + country.alpha2Code + ".png")
            .into(imageView)

        prepareMap()
    }

    override fun onMapReady(p0: GoogleMap?) {
        // place the marker when map is ready
        placeMarker(p0!!,globalLatLng!!)
    }
}

