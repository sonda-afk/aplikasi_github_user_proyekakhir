package com.sonda.bangkit.fundamentaltiga.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sonda.bangkit.fundamentaltiga.R
import com.sonda.bangkit.fundamentaltiga.model.Favorite
import com.sonda.bangkit.fundamentaltiga.ui.UserDetail

class FavoriteAdapter(val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    var listFav = ArrayList<Favorite>()
        set(listFav) {
            if (listFav.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFav)

            notifyDataSetChanged()
        }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(favorite: Favorite) {
            with(itemView) {
                var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
                var username: TextView = itemView.findViewById(R.id.tv_item_name)
                var detail: TextView = itemView.findViewById(R.id.tv_item_detail)

                Glide.with(itemView.context)
                        .load(favorite.avatar)
                        .apply(RequestOptions().override(350, 550))
                        .into(imgPhoto)

                username.text = favorite.name
                detail.text = favorite.username

                itemView.setOnClickListener(View.OnClickListener {
                    Toast.makeText(activity, "" + favorite.username, Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, UserDetail::class.java)
                    intent.putExtra(UserDetail.EXTRA_FAV, favorite)
                    activity.startActivity(intent)

                })
            }
        }

    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): FavoriteAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ListViewHolder, position: Int) {
        holder.bind(listFav[position])


    }

    override fun getItemCount(): Int {
        return listFav.size
    }


}
