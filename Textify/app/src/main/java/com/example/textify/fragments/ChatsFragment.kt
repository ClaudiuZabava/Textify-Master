package com.example.textify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.textify.adapters.ChatsAdapter
import com.example.textify.databinding.FragmentChatsBinding
import com.example.textify.models.ChatRoom
import com.example.textify.repos.ChatroomsRepo
import com.example.textify.utils.Constants
import com.example.textify.utils.NetworkConnection
import com.example.textify.utils.PreferenceHandler

class ChatsFragment: Fragment() {

    private lateinit var bindingChats: FragmentChatsBinding;
    private lateinit var prefHandler: PreferenceHandler;
    private lateinit var chatsAdapter: ChatsAdapter
    private lateinit var chatroomsRepo: ChatroomsRepo
    private lateinit var networkConnection: NetworkConnection
    private val chats = mutableListOf<ChatRoom>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingChats = FragmentChatsBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(requireContext())
        chatroomsRepo = ChatroomsRepo(requireContext())
        chatsAdapter = ChatsAdapter(requireContext(), chats)
        //networkConnection = NetworkConnection(requireContext())
        setupRecyclerView()
        return bindingChats.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNetworkConnection()
    }

    override fun onResume() {
        super.onResume()
        observeNetworkConnection() // Ensure listeners are set up again when fragment resumes
    }

    override fun onPause() {
        super.onPause()
        chatroomsRepo.removeListeners() // Clean up listeners to prevent leaks
        networkConnection.removeObservers(viewLifecycleOwner)
    }

    private fun setupRecyclerView()
    {
        bindingChats.chatscreenRvChats.adapter = chatsAdapter
        bindingChats.chatscreenRvChats.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun loadChats() {
        val uid = prefHandler.getString(Constants.KEY_USER_ID)
        if (uid != null) {
            chatroomsRepo.syncChatsFromFirestore(uid) { chatList ->
                activity?.runOnUiThread {
                    chats.clear()
                    chats.addAll(chatList)
                    chatsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun loadChatsFromDb() {
        chatroomsRepo.loadChatsFromDb { chatList ->
            activity?.runOnUiThread {
                chats.clear()
                chats.addAll(chatList)
                chatsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun observeNetworkConnection() {
        networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                loadChats()
            } else {
                loadChatsFromDb()
            }
        }
    }
}