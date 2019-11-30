package com.sneka.knowthecountries.presenter

import android.view.View
import com.sneka.knowthecountries.utils.AppConstants
import com.sneka.knowthecountries.model.country
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.sneka.knowthecountries.ui.CountryView;
import android.content.Context
import com.sneka.knowthecountries.ui.CountryDetailsView

/*
The presenter interface for displaying the country details
 */
public class CountryDetailsPresenter
{
    private var countryView:CountryDetailsView?=null
    private var mContext: Context? = null
   constructor(mContext:Context,countryView:CountryDetailsView)
    {
        this.mContext=mContext;
        this.countryView=countryView;
    }


    fun makeGetCountryNetworkCall(alphaCode: String) {
        var apiService = ApiClient.create()
        apiService.getCountry(alphaCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                    result ->
                countryView?.bindDataForCountry(result)
            }, { error ->
                error.printStackTrace()
            })
    }

}

