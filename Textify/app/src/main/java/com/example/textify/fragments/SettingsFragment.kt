package com.example.textify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.textify.databinding.FragmentSettingsBinding
import com.example.textify.utils.PreferenceHandler

class SettingsFragment: Fragment(){

    private lateinit var bindingSettings: FragmentSettingsBinding;
    private lateinit var prefHandler: PreferenceHandler;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingSettings = FragmentSettingsBinding.inflate(layoutInflater)
        prefHandler = PreferenceHandler(requireContext())
        return bindingSettings.root
    }
}