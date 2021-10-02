package com.nadhif.githubuser.adapter

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nadhif.githubuser.R
import com.nadhif.githubuser.network.response.User

class UserAdapter(private val clickListener: (User) -> Unit)
    : ListAdapter<User, UserAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
           return oldItem == newItem
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView =itemView.findViewById(R.id.tvName)
        private val avatar: ImageView = itemView.findViewById(R.id.imageView)
        fun bind(item: User, listener: (User) -> Unit) {
            name.text = item.login

            Glide.with(itemView)
                .load(item.avatarUrl)
                .circleCrop()
                .into(avatar)

            itemView.setOnClickListener {
                listener(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_user, parent, false)
                return ViewHolder(view)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

}



