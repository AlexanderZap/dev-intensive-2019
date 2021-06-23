package ru.skillbranch.devintensive.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.databinding.ItemUserListBinding
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.UserItem

class UserAdapter(val listener: (UserItem) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var items: List<UserItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_user_list, parent, false)
        val binding = ItemUserListBinding.bind(convertView)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    fun updateData(data: List<UserItem>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].id == data[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].hashCode() == data[newItemPosition].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    class UserViewHolder(private val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserItem, listener: (UserItem) -> Unit) {
            if (user.avatar != null) {
                Glide.with(itemView)
                    .load(user.avatar)
                    .into(binding.ivAvatarUser)
            } else {
                Glide.with(itemView)
                    .clear(binding.ivAvatarUser)
                binding.ivAvatarUser.setInitials(user.initials ?: "??")
            }

            binding.svIndicator.visibility = if (user.isOnline) View.VISIBLE else View.GONE
            binding.tvUserName.text = user.fullName
            binding.tvLastActivity.text = user.lastActivity
            binding.ivSelected.visibility = if (user.isSelected) View.VISIBLE else View.GONE

            itemView.setOnClickListener { listener.invoke(user) }
        }
    }
}