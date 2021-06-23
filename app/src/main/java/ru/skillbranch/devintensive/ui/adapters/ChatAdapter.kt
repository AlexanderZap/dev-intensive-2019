package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.databinding.ItemChatGroupBinding
import ru.skillbranch.devintensive.databinding.ItemChatSingleBinding
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem

class ChatAdapter(val listener: (ChatItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: List<ChatItem> = listOf()

    companion object {
        private const val ARCHIVE_TYPE = 0
        private val SINGLE_TYPE = 1
        private val GROUP_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when(items[position].chatType){
        Chat.ChatType.ARCHIVE -> ARCHIVE_TYPE
        Chat.ChatType.SINGLE -> SINGLE_TYPE
        Chat.ChatType.GROUP -> GROUP_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertViewSingle = inflater.inflate(R.layout.item_chat_single, parent, false)
        val bindingSingle = ItemChatSingleBinding.bind(convertViewSingle)

        val convertViewGroup = inflater.inflate(R.layout.item_chat_group, parent, false)
        val bindingGroup = ItemChatGroupBinding.bind(convertViewGroup)

        //return SingleViewHolder(binding)

        return when (viewType) {
            SINGLE_TYPE -> SingleViewHolder(bindingSingle)
            GROUP_TYPE -> GroupViewHolder(bindingGroup)
            else -> SingleViewHolder(bindingSingle)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChatItemViewHolder).bind(items[position], listener)
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

    interface ChatItemViewHolder {

         fun bind(item: ChatItem, listener: (ChatItem) -> Unit)
    }

    class SingleViewHolder(private val binding: ItemChatSingleBinding) :
        RecyclerView.ViewHolder(binding.root), ChatItemTouchHelperCallback.ItemTouchHolder, ChatItemViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            if (item.avatar == null) {
                Glide.with(itemView)
                    .clear(binding.ivAvatarSingle)
                binding.ivAvatarSingle.setInitials(item.initials)
            } else {
                Glide.with(itemView)
                    .load(item.avatar)
                    .into(binding.ivAvatarSingle)
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

    class GroupViewHolder(private val binding: ItemChatGroupBinding) :
        RecyclerView.ViewHolder(binding.root), ChatItemTouchHelperCallback.ItemTouchHolder, ChatItemViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            binding.ivAvatarGroup.setInitials(item.title[0].toString())

            with(binding.tvDateGroup) {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }

            with(binding.tvCounterGroup) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            with(binding.tvMessageAuthor) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            binding.tvTitleGroup.text = item.title
            binding.tvMessageGroup.text = item.shortDescription

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