package com.nadhif.githubuser.adapter

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nadhif.githubuser.R
import com.nadhif.githubuser.network.response.User

class FavoriteUserAdapter(
    private val activity: AppCompatActivity,
    private val clickListener: (User) -> Unit,
    private val deleteSelectedItemListener: (ArrayList<String>) -> Unit
) : ListAdapter<User, FavoriteUserAdapter.ViewHolder>(DiffCallback), ActionMode.Callback {

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {

        var isActionModeEnable = false
        var selected = 0
        var actionMode: ActionMode? = null
        val selectedItem = arrayListOf<String>()

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
           return oldItem == newItem
        }

    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.contextual_action_bar, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete -> {
                deleteSelectedItemListener(selectedItem)
                mode?.finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        selected = 0
        isActionModeEnable = false
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView =itemView.findViewById(R.id.tvName)
        private val avatar: ImageView = itemView.findViewById(R.id.imageView)
        private val check: ImageView = itemView.findViewById(R.id.ivCheck)

        fun bind (user: User, activity: AppCompatActivity, listener: (User) -> Unit, actionModeCallback: ActionMode.Callback) {

            if (!isActionModeEnable) {
                check.visibility = View.GONE
            }

            name.text = user.login

            Glide.with(itemView)
                .load(user.avatarUrl)
                .circleCrop()
                .into(avatar)

            itemView.setOnLongClickListener {
                if (!isActionModeEnable) {
                    check.visibility = View.VISIBLE
                    isActionModeEnable = true
                    selectedItem.add(user.login)
                    selected++
                    actionMode = activity.startSupportActionMode(actionModeCallback)
                    actionMode?.title = "$selected selected"
                }

                true
            }

            itemView.setOnClickListener {
                if (check.isVisible) {
                    check.visibility = View.GONE
                    selectedItem.remove(user.login)
                    selected--
                    actionMode?.title = "$selected selected"
                } else {
                    if (isActionModeEnable) {
                        check.visibility = View.VISIBLE
                        selectedItem.add(user.login)
                        selected++
                        actionMode?.title = "$selected selected"
                    } else {
                        listener(user)
                    }
                }
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
        holder.bind(getItem(position), activity, clickListener, this)
    }
}



