package com.example.textify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.textify.databinding.FragmentContactsBinding
import com.example.textify.utils.PreferenceHandler

class ContactsFragment: Fragment() {

    private lateinit var bindingContacts: FragmentContactsBinding;
    private lateinit var prefHandler: PreferenceHandler;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingContacts = FragmentContactsBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(requireContext())
        return bindingContacts.root
    }
}