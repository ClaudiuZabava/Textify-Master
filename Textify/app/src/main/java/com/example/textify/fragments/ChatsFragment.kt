package com.example.textify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.textify.databinding.FragmentChatsBinding
import com.example.textify.utils.PreferenceHandler

class ChatsFragment: Fragment() {

    private lateinit var bindingChats: FragmentChatsBinding;
    private lateinit var prefHandler: PreferenceHandler;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingChats = FragmentChatsBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(requireContext())
        return bindingChats.root
    }
}