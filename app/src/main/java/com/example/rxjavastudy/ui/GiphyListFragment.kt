package com.example.rxjavastudy.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rxjavastudy.databinding.FragmentGiphyListBinding
import com.example.rxjavastudy.di.viewmodel.ViewModelFactory
import com.example.rxjavastudy.ui.SearchModeType.DEBOUNCE
import com.example.rxjavastudy.ui.SearchModeType.THROTTLE
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.scrollChangeEvents
import com.jakewharton.rxbinding3.widget.textChanges
import javax.inject.Inject

class GiphyListFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
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

        viewModel.getRandomGiphy(LOAD_COUNT.toLong())
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

    @SuppressLint("CheckResult")
    private fun initView() {
        binding.searchEditText
            .textChanges()
            .subscribe {
                when (viewModel.searchMode.value) {
                    THROTTLE -> {
                        viewModel.throttlingSubject.onNext(it.toString())
                    }
                    DEBOUNCE -> {
                        viewModel.debouncingSubject.onNext(it.toString())
                    }
                }
            }

        binding.searchToggle
            .clicks()
            .subscribe {
                viewModel.searchMode.postValue(viewModel.searchMode.value?.toggle())
            }

        binding.giphyListView
            .scrollChangeEvents()
            .subscribe {
                val layoutManager = binding.giphyListView.layoutManager as GridLayoutManager
                val lastVisibleLine = (layoutManager.findLastVisibleItemPosition() + 1) / COLUMN_NUM
                val itemTotalLine = adapter.itemCount / COLUMN_NUM

                val searchText = binding.searchEditText.text.toString()

                if (searchText.isNotBlank() && lastVisibleLine == (itemTotalLine - SCROLL_LINE_OFFSET)) {
                    viewModel.getSearchGiphyList(searchText, LOAD_COUNT)
                }
            }

        val gridLayoutManager = GridLayoutManager(context, COLUMN_NUM)
        adapter = GiphyItemAdapter()
        binding.giphyListView.adapter = adapter
        binding.giphyListView.layoutManager = gridLayoutManager
    }

    private fun initViewModel() {
        viewModel.initSubject()
        viewModel.giphyListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }

        viewModel.searchMode.observe(viewLifecycleOwner) {
            binding.searchToggle.text = it.text
        }

        viewModel.blinkListLiveData.observe(viewLifecycleOwner) {
            adapter.blinkIndexList = it
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val COLUMN_NUM = 3
        private const val SCROLL_LINE_OFFSET = 2
        const val LOAD_COUNT = 30
        fun newInstance(): GiphyListFragment {
            return GiphyListFragment()
        }
    }
}
