package com.hotta.hoho.view.join

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hotta.hoho.R
import com.hotta.hoho.databinding.FragmentEmailFindBinding
import com.hotta.hoho.databinding.FragmentHome1Binding


class EmailFindFragment : Fragment() {
    private var _binding: FragmentEmailFindBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailFindBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.emailAccessBtn.setOnClickListener {
            val email = binding.emailEdit




        }


    }


}