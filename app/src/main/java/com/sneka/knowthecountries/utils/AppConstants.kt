package com.sneka.knowthecountries.utils

import android.content.Context
import android.net.ConnectivityManager
import java.util.*
import android.R
import android.widget.ImageView
import com.pixplicity.sharp.Sharp
import okhttp3.*
import java.io.IOException


/*
Author: Sneka Shanmughasundaram
Date: 25-11-2019
Objective: This file declares the constants required for the application
 */
class AppConstants {
    companion object {
        // base url to get country information
        val BASE_URL = "https://restcountries.eu/"
        //base url for country flag
        val BASE_URL_FLAG = "http://www.geognos.com/api/en/countries/flag/"
        val ALPHA_2_CODE_EXTRA = "COUNTRY_NAME"

        fun isConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    }
}