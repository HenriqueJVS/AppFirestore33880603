package com.google.firebase.example.rgm33880603.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.up.rgm33880603.databinding.ItemRestaurantBinding
import com.bumptech.glide.Glide
import com.google.firebase.example.rgm33880603.model.Restaurant
import com.google.firebase.example.rgm33880603.util.RestaurantUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import br.edu.up.rgm33880603.R

/**
 * RecyclerView adapter for a list of Restaurants.
 */
open class RestaurantAdapter(query: Query, private val listener: OnRestaurantSelectedListener) :
        FirestoreAdapter<RestaurantAdapter.ViewHolder>(query) {

    interface OnRestaurantSelectedListener {

        fun onRestaurantSelected(restaurant: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRestaurantBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class ViewHolder(val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnRestaurantSelectedListener?
        ) {

            val restaurant = snapshot.toObject<Restaurant>()
            if (restaurant == null) {
                return
            }

            val resources = binding.root.resources

            // Load image
            Glide.with(binding.restaurantItemImage.context)
                    .load(restaurant.photo)
                    .into(binding.restaurantItemImage)

            val numRatings: Int = restaurant.numRatings

            binding.restaurantItemName.text = restaurant.name
            binding.restaurantItemRating.rating = restaurant.avgRating.toFloat()
            binding.restaurantItemCity.text = restaurant.city
            binding.restaurantItemCategory.text = restaurant.category
            binding.restaurantItemNumRatings.text = resources.getString(
                    R.string.fmt_num_ratings,
                    numRatings)
            binding.restaurantItemPrice.text = RestaurantUtil.getPriceString(restaurant)

            // Click listener
            binding.root.setOnClickListener {
                listener?.onRestaurantSelected(snapshot)
            }
        }
    }
}
