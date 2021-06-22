package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.databinding.ItemChatSingleBinding
import ru.skillbranch.devintensive.models.data.ChatItem

class ChatAdapter(val listener: (ChatItem) -> Unit) : RecyclerView.Adapter<ChatAdapter.SingleViewHolder>() {
    var items: List<ChatItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_chat_single, parent, false)
        val binding = ItemChatSingleBinding.bind(convertView)

        return SingleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun updateData(data: List<ChatItem>) {

        val diffCallback = object :DiffUtil.Callback(){
            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = items[oldItemPosition].id == data[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = items[oldItemPosition].hashCode() == data[newItemPosition].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)

    }

    override fun getItemCount(): Int = items.size

    class SingleViewHolder(private val binding: ItemChatSingleBinding) :
        RecyclerView.ViewHolder(binding.root), ChatItemTouchHelperCallback.ItemTouchHolder {

        fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            if (item.avatar == null) {
                binding.ivAvatarSingle.setInitials(item.initials)
            } else {
                //TODO
            }
            binding.svIndicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
            with(binding.tvDateSingle) {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(binding.tvCounterSingle) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            binding.tvTitleSingle.text = item.title
            binding.tvMessageSingle.text = item.shortDescription

            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }
}