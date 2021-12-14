package com.example.rxjavastudy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rxjavastudy.databinding.FragmentGiphyListBinding
import com.example.rxjavastudy.di.viewmodel.ViewModelFactory
import javax.inject.Inject

class GiphyListFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: GiphyListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(GiphyListViewModel::class.java)
    }

    private val presentationComponent by lazy {
        (activity as MainActivity).activityComponent.newPresentationComponent()
    }

    private var _binding: FragmentGiphyListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GiphyItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presentationComponent.inject(this)

        viewModel.fetchRandomGiphyList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGiphyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() {
        val gridLayoutManager = GridLayoutManager(context, 3)
        adapter = GiphyItemAdapter()
        binding.giphyListView.adapter = adapter
        binding.giphyListView.layoutManager = gridLayoutManager
    }

    private fun initViewModel() {
        viewModel.randomGiphy.observe(viewLifecycleOwner, {
            adapter.bindData(it)
        })
    }

    companion object {
        fun newInstance(): GiphyListFragment {
            return GiphyListFragment()
        }
    }
}
