package com.hotta.hoho.view.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotta.hoho.R
import com.hotta.hoho.databinding.FragmentHome2Binding
import com.hotta.hoho.datamodel.PopularMovieResult
import com.hotta.hoho.datamodel.PopularTvResult
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.view.adapter.PopMovieAdapter
import com.hotta.hoho.view.adapter.PopTvAdapter
import com.hotta.hoho.view.custom.CustomPopup
import com.hotta.hoho.view.main.custom.CustomScrollView
import com.hotta.hoho.view.more.NowMoreActivity


class HomeFragment2 : Fragment() {
    private var _binding: FragmentHome2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHome2Binding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfo(FireBaseAuthUtils.getUid())

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.scrollView.scrollY

            if (scrollY > 0) {
                binding.topLayout.animate().alpha(0f).setDuration(500).start()
            } else {
                binding.topLayout.animate().alpha(1f).setDuration(500).start()
            }
        }
        // val inputLayout = binding.inputLayout

        val autoCompleteTextView = binding.textItem
        val hintColor = Color.BLACK
        autoCompleteTextView.setHintTextColor(hintColor)
        autoCompleteTextView.hint = "영화"

        viewModel.getPopularMovie()
        viewModel.getCurrentMovie()
        viewModel.getTopMovie()
        viewModel.getUpMovie()

        /*  binding..setOnClickListener {
              binding.scrollView.scrollTo(0, 0)
              binding.searchImg.visibility = View.GONE
              binding.cancelImg.visibility = View.VISIBLE
              binding.editLayout.visibility = View.VISIBLE
          }
          binding.cancelImg.setOnClickListener {
              binding.scrollView.scrollTo(0, 0)
              binding.searchImg.visibility = View.VISIBLE
              binding.cancelImg.visibility = View.GONE
              binding.editLayout.visibility = View.GONE
          }*/

        val item: List<String> = listOf("영화", "TV")
        val itemAdapter = ArrayAdapter<String>(requireContext(), R.layout.drop_down_item, item)
        autoCompleteTextView.setAdapter(itemAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            /*  binding.mainTitle.setText(selectedItem)*/

            if (selectedItem.equals("영화")) {
                viewModel.getPopularMovie()
                viewModel.getCurrentMovie()
                viewModel.getTopMovie()
                viewModel.getUpMovie()

                binding.popTitle.setText("인기" + selectedItem)

            } else if (selectedItem.equals("TV")) {
                viewModel.getPopularTv()
                binding.popTitle.setText("인기" + selectedItem)

            }


        }

        binding.nowMore.setOnClickListener {
            val intent = Intent(requireContext(), NowMoreActivity::class.java)
            intent.putExtra("type", "now")
            startActivity(intent)
        }

        binding.popMore.setOnClickListener {
            val intent = Intent(requireContext(), NowMoreActivity::class.java)
            intent.putExtra("type", "pop")
            startActivity(intent)
        }

        binding.topMore.setOnClickListener {
            val intent = Intent(requireContext(), NowMoreActivity::class.java)
            intent.putExtra("type", "top")
            startActivity(intent)
        }

        binding.upMore.setOnClickListener {
            val intent = Intent(requireContext(), NowMoreActivity::class.java)
            intent.putExtra("type", "up")
            startActivity(intent)
        }

        viewModel.curMovieResult.observe(viewLifecycleOwner, Observer {
            val popMovieAdapter = PopMovieAdapter(
                requireContext(),
                it, "now"
            )
            binding.curRv.adapter = popMovieAdapter
            binding.curRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        })
        viewModel.popMovieResult.observe(viewLifecycleOwner, Observer {
            val popMovieAdapter = PopMovieAdapter(
                requireContext(),
                it, "pop"
            )
            binding.popRv.adapter = popMovieAdapter
            binding.popRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        })
        viewModel.topMovieResult.observe(viewLifecycleOwner, Observer {
            val popMovieAdapter = PopMovieAdapter(
                requireContext(),
                it, "top"
            )
            binding.topRv.adapter = popMovieAdapter
            binding.topRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        })
        viewModel.upMovieResult.observe(viewLifecycleOwner, Observer {
            val popMovieAdapter = PopMovieAdapter(
                requireContext(),
                it, "up"
            )
            binding.upRv.adapter = popMovieAdapter
            binding.upRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        })

        /*   viewModel.popTvResult.observe(viewLifecycleOwner, Observer {
               val popTvAdapter =
                   PopTvAdapter(requireContext(), it)
               binding.popRv.adapter = popTvAdapter
               binding.popRv.layoutManager =
                   LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
           })*/


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val popup = CustomPopup(context, object : CustomPopup.PopupListener {
                    override fun conFirmClick() {
                        if (FireBaseAuthUtils.getUid() != null) {
                            FireBaseAuthUtils.signOut()
                        }
                        requireActivity().finish()
                    }

                    override fun cancelClick() {

                    }
                }, "종료", "Hoho를 종료 하시겠습니까?", true)
                popup.setCancelable(false)
                popup.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }




}