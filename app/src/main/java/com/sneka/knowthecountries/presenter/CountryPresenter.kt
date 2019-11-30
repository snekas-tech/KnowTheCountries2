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
public class CountryPresenter
{
    private var countryView:CountryView?=null
    private var mContext: Context? = null
   constructor(mContext:Context,countryView:CountryView)
    {
        this.mContext=mContext;
        this.countryView=countryView;
    }

    //Rx Java Bindings to make the network call
    fun makeGetAllCountryNetworkCall() {
       countryView?.showProgressBar()
        var apiService = ApiClient.create()
        apiService.getAllCountries()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
               countryView?.hideProgressBar()
                countryView?.notifyAdapter(result)
            }, { error ->
                countryView?.hideProgressBar()
                error.printStackTrace()
            })
    }
}
interface ApiClient {
    @GET("rest/v2/all")
    fun getAllCountries(): Observable<ArrayList<country>>

    @GET("rest/v2/alpha/{code}")
    fun getCountry(@Path("code") alpha2Code: String): Observable<country>

    companion object Factory {
        fun create(): ApiClient {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(AppConstants.BASE_URL)
                    .build()

            return retrofit.create(ApiClient::class.java)
        }
    }


}
