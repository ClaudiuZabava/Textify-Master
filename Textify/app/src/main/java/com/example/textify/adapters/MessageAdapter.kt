package com.example.textify.adapters
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.textify.R
import com.example.textify.models.Message
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class MessagesAdapter(private val context: Context, private var chatList :List<Message>, private var image: String, private var receiverName: String, private val delSetSender: HashMap<String,Message>, private val delSetReceiver: HashMap<String,Message>, private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>()
{
    private lateinit var mListener: OnItemLongClickListener
    private lateinit var click_Listener: OnItemClickListener
    inner class MessageViewHolder(itemView: View, private val listener: OnItemLongClickListener): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemLongClick(position, itemViewType)
                }
                true
            }

            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    click_Listener.onItemClick(position, itemViewType)
                }
            }
        }


        fun bindDataSent(message: Message) {
            val cvSent = itemView.findViewById<CardView>(R.id.sent_reply_cv)
            cvSent.setOnClickListener {
                recyclerView.smoothScrollToPosition(message.reply_pos.toInt())
            }
            val barSent = itemView.findViewById<View>(R.id.sent_reply_bar)
            val nameSent = itemView.findViewById<TextView>(R.id.sent_reply_name)
            val msgSent = itemView.findViewById<TextView>(R.id.sent_reply_msg)
            val messageImage = itemView.findViewById<ImageView>(R.id.sent_iv_image)
            if (!message.imageUrl.isNullOrEmpty()) {
                messageImage.visibility = View.VISIBLE
                Glide.with(context)
                    .load(message.imageUrl)
                    .into(messageImage)
            } else {
                messageImage.visibility = View.GONE
            }
            if(message.reply_attached ) {
                cvSent.visibility = View.VISIBLE
                msgSent.text = message.reply_text
                if(message.reply_to== FirebaseAuth.getInstance().uid) {
                    nameSent.text = "You"
                } else {
                    nameSent.text = receiverName
                }
            } else {
                cvSent.visibility = View.GONE
            }
            itemView.findViewById<LinearLayout>(R.id.sent_ll_reply).foreground = null
            val text = itemView.findViewById<TextView>(R.id.sent_tv_text)
            val dt = itemView.findViewById<TextView>(R.id.sent_tv_time)
            text.text = message.text
            dt.text = message.datetime
        }
        fun bindDataReceive(message: Message) {
            val cvSent = itemView.findViewById<CardView>(R.id.receiver_reply_cv)
            cvSent.setOnClickListener {
                recyclerView.smoothScrollToPosition(message.reply_pos.toInt())
            }
            val barSent = itemView.findViewById<View>(R.id.received_reply_bar)
            val nameSent = itemView.findViewById<TextView>(R.id.received_reply_name)
            val msgSent = itemView.findViewById<TextView>(R.id.received_reply_msg)
            val messageImage = itemView.findViewById<ImageView>(R.id.received_iv_image)
            if (!message.imageUrl.isNullOrEmpty()) {
                messageImage.visibility = View.VISIBLE
                Glide.with(context)
                    .load(message.imageUrl)
                    .into(messageImage)
            } else {
                messageImage.visibility = View.GONE
            }
            if(message.reply_attached) {
                cvSent.visibility = View.VISIBLE
                msgSent.text = message.reply_text
                if(message.reply_to== FirebaseAuth.getInstance().uid) {
                    nameSent.text = "You"
                } else {
                    nameSent.text = receiverName
                }
            } else {
                cvSent.visibility = View.GONE
            }
            itemView.findViewById<LinearLayout>(R.id.received_ll_reply).foreground = null
            val text = itemView.findViewById<TextView>(R.id.received_tv_text)
            val dt = itemView.findViewById<TextView>(R.id.received_tv_datetime)
            text.text = message.text
            dt.text = message.datetime
            val iv = itemView.findViewById<CircleImageView>(R.id.receivec_iv_profile)
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.profile_placeholder)
                .into(iv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view: View = if(viewType == VIEW_TYPE_SENT) {
            LayoutInflater.from(context).inflate(R.layout.sent_messages,parent,false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.recived_message, parent, false)
        }
        return MessageViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        if(getItemViewType(position)==VIEW_TYPE_RECEIVER) {
            holder.bindDataReceive(chatList[position])

        } else {
            holder.bindDataSent(chatList[position])
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun setOnLongClickListener(listener: OnItemLongClickListener) {
        mListener = listener
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        click_Listener = listener
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int, viewType: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, viewType: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().uid.equals(chatList[position].from_id)) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVER
        }
    }

    private val VIEW_TYPE_SENT: Int = 1
    private val VIEW_TYPE_RECEIVER: Int = 2
}