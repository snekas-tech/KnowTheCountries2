package com.sneka.knowthecountries.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sneka.knowthecountries.R
import com.sneka.knowthecountries.model.country
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import com.squareup.picasso.Picasso;
import com.sneka.knowthecountries.utils.AppConstants;
import android.content.Intent
import com.sneka.knowthecountries.ui.CountryDetailsActivity;
import androidx.core.content.ContextCompat.startActivity;

class CountryListHolder(view: View): RecyclerView.ViewHolder(view) {
    var countryName: TextView? = null
    var capitalName: TextView? = null
    var flagImageView: ImageView ?=null


    init {
        prepareView(view)
    }

    fun prepareView(view:View) {
        countryName = view.findViewById<TextView>(R.id.countryName)
        capitalName = view.findViewById<TextView>(R.id.capitalName)
        flagImageView = view.findViewById<ImageView>(R.id.icon)

    }
}

class countryListAdapter(val countryList: ArrayList<country>): RecyclerView.Adapter<CountryListHolder>() {
    var countryListFiltered = countryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.customcountrylist, parent, false)
        return CountryListHolder(v)
    }

    override fun onBindViewHolder(holder: CountryListHolder, position: Int) {
        var country = countryListFiltered[position]
        holder?.countryName?.text = country.name
        holder?.capitalName?.text= holder.itemView.context.getString(R.string.belongs_to) +" " + country.capital
        println(AppConstants.BASE_URL_FLAG + country.alpha2Code + ".png")
        Picasso
            .with(holder?.itemView?.context)
            .load(AppConstants.BASE_URL_FLAG + country.alpha2Code + ".png")
            .placeholder(R.drawable.default_image)
            .into(holder?.flagImageView)

        holder?.itemView!!.setOnClickListener({ v ->
            val intent = Intent(holder?.itemView!!.context, CountryDetailsActivity::class.java)
            intent.putExtra(AppConstants.ALPHA_2_CODE_EXTRA,country.alpha2Code)
            startActivity(holder?.itemView!!.context,intent,null)
        })
    }

    override fun getItemCount(): Int {
        return countryListFiltered.size
    }


    // perform the filtering when query is typed in search view
    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()

                //if query string is empty add all in filtered list
                if (charString.isEmpty()) {
                    countryListFiltered = countryList
                } else {
                    var filteredList = ArrayList<country>()
                    for (row in countryList) {
                        // if found query string add country in filtered lsit
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList!!.add(row)
                        }
                    }

                    countryListFiltered = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = countryListFiltered
                return filterResults
            }

            // call back for showing result
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                countryListFiltered = filterResults.values as ArrayList<country>
                notifyDataSetChanged()
            }
        }
    }

}