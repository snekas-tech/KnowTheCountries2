package com.sneka.knowthecountries.model

/* Data model corresponding to the response from API*/

data class country (
    val name: String,
    val capital: String,
    val alpha2Code: String,
    val region: String,
    val subregion: String,
    val altSpellings: ArrayList<String>,
    val population: Long,
    val latlng: ArrayList<Double>,
    val area: Double,
    val timezones: ArrayList<String>,
    val currencies: ArrayList<Currency>,
    val languages: ArrayList<Language>,
    val flag: String
)

data class Currency (
       val code: String,
       val name: String,
       val symbol: String
)

data class Language (
        val name: String,
        val nativeName: String
)
