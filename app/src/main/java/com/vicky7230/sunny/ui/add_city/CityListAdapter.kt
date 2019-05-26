package com.vicky7230.sunny.ui.add_city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vicky7230.sunny.R
import kotlinx.android.synthetic.main.city_list_item.view.*

class CityListAdapter(private val cityArrayList: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        fun onCityClick(city: String)
    }

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cityItemViewHolder = CityItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.city_list_item,
                parent,
                false
            )
        )

        cityItemViewHolder.itemView.setOnClickListener {
            val position = cityItemViewHolder.adapterPosition
            callback?.onCityClick(cityArrayList[position])
        }

        return cityItemViewHolder
    }

    override fun getItemCount(): Int {
        return cityArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CityItemViewHolder).onBind(cityArrayList[position])
    }

    fun addCities(suggestionsArrayList: MutableList<String>) {
        cityArrayList.clear()
        cityArrayList.addAll(suggestionsArrayList)
        notifyDataSetChanged()
    }

    class CityItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(city: String) {
            itemView.city_name.text = city
            itemView.country_name.text = "India"
        }
    }
}
