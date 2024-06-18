package com.example.textify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.textify.R
import com.example.textify.models.ChatRoom
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

class ChatsAdapter(private val context: Context, private val chatRooms: List<ChatRoom>): RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>()
{
    private lateinit var mListener: onItemClickListener
    inner class ChatViewHolder(itemView: View, private val listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val userImage: ImageView = itemView.findViewById(R.id.chat_user_image)
        private val userName: TextView = itemView.findViewById(R.id.chat_user_name)
        private val lastMessage: TextView = itemView.findViewById(R.id.chat_last_message)
        private val chatTime: TextView = itemView.findViewById(R.id.chat_timestamp)
        private val unreadCount: TextView = itemView.findViewById(R.id.chat_unread_count)
        private val chatDate: TextView = itemView.findViewById(R.id.chat_date)
        private val tick: ImageView = itemView.findViewById(R.id.chat_tick)
        private val unreadHolder: CardView = itemView.findViewById(R.id.chat_unread_holder)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
        fun bind(chatRoom: ChatRoom) {
            val sender_id: String = FirebaseAuth.getInstance().uid!!
            if(chatRoom.receiver_id.equals(sender_id)) {
                Glide
                    .with(context)
                    .load(chatRoom.sender_image)
                    .centerCrop()
                    .placeholder(R.drawable.profile_placeholder)
                    .into(userImage)
                userName.text = chatRoom.sender_name
                lastMessage.text = chatRoom.last_text
                if(chatRoom.last_text_from != sender_id) {
                    tick.visibility = View.GONE
                    unreadHolder.visibility = View.VISIBLE
                } else {
                    tick.visibility = View.VISIBLE
                    unreadHolder.visibility = View.GONE
                }
                if(chatRoom.unread_count<=0)
                    unreadHolder.visibility = View.GONE
                val t1 = chatRoom.timestamp?.toDate()
                val t2 = SimpleDateFormat("HH:mm", Locale.getDefault()).format(t1!!)
                val t3 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(t1)
                unreadCount.text = chatRoom.unread_count.toString()
                chatTime.text = t2.toString()
                chatDate.text = t3.toString()
            } else {
                Glide
                    .with(context)
                    .load(chatRoom.receiver_image)
                    .centerCrop()
                    .placeholder(R.drawable.profile_placeholder)
                    .into(userImage)
                userName.text = chatRoom.receiver_name
                lastMessage.text = chatRoom.last_text
                if(chatRoom.last_text_from != sender_id) {
                    tick.visibility = View.GONE
                    unreadHolder.visibility = View.VISIBLE
                } else {
                    tick.visibility = View.VISIBLE
                    unreadHolder.visibility = View.GONE
                }
                if(chatRoom.unread_count<=0)
                    unreadHolder.visibility = View.GONE
                val t1 = chatRoom.timestamp?.toDate()
                val t2 = SimpleDateFormat("HH:mm", Locale.getDefault()).format(t1!!)
                val t3 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(t1)
                unreadCount.text = chatRoom.unread_count.toString()
                chatTime.text = t2.toString()
                chatDate.text = t3.toString()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_card, parent, false)
        return ChatViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatRooms[position])
    }

    override fun getItemCount(): Int = chatRooms.size

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

}