package com.vicky7230.sunny.ui.saved_cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vicky7230.sunny.R
import kotlinx.android.synthetic.main.city_list_item.view.*
import kotlinx.android.synthetic.main.city_list_item.view.city_name
import kotlinx.android.synthetic.main.city_list_item.view.country_name
import kotlinx.android.synthetic.main.saved_city_list_view_item.view.*

class SavedCityListAdapter(private val cityArrayList: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        fun onRemoveCityClick(city: String)
    }

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cityItemViewHolder = CityItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.saved_city_list_view_item,
                parent,
                false
            )
        )

        cityItemViewHolder.itemView.remove_button.setOnClickListener {
            val position = cityItemViewHolder.adapterPosition
            callback?.onRemoveCityClick(cityArrayList[position])
            cityArrayList.removeAt(position)
            notifyDataSetChanged()
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
