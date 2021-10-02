package com.nadhif.consumerapp

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var cursor: Cursor? = null

    fun setData(cursor: Cursor) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_user,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        cursor?.moveToPosition(position)?.let { holder.bind(it) }
    }


    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(moveToPosition: Boolean) {
            if (moveToPosition) {
                with(itemView) {
                    val username = findViewById<TextView>(R.id.tvName)
                    username.text = cursor?.getString(cursor?.getColumnIndexOrThrow(Utility.COLUMN_LOGIN) ?: 0)

                    val avatarUrl = findViewById<ImageView>(R.id.imageView)
                    Glide.with(itemView)
                        .load(cursor?.getString(cursor?.getColumnIndexOrThrow(Utility.COLUMN_AVATAR_URL) ?: 0))
                        .circleCrop()
                        .into(avatarUrl)
                }
            }
        }
    }

}



