package com.example.laba5.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laba5.databinding.ItemChatBinding
import com.example.laba5.models.Chat

class ChatAdapter(private val chatList: List<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater, parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        with(holder.binding) {
            senderName.text = chat.sender
            messageText.text = chat.message
            messageTime.text = chat.time
        }
    }

    override fun getItemCount(): Int = chatList.size
}
