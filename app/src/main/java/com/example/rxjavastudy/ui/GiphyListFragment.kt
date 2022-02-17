package com.example.rxjavastudy.ui

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxjavastudy.databinding.FragmentGiphyListBinding
import com.example.rxjavastudy.di.viewmodel.ViewModelFactory
import com.example.rxjavastudy.ui.SearchModeType.DEBOUNCE
import com.example.rxjavastudy.ui.SearchModeType.THROTTLE
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.focusChanges
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

    private lateinit var giphyItemAdapter: GiphyItemAdapter
    private lateinit var autoCompleteItemAdapter: AutoCompleteItemAdapter

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

        binding.searchEditText
            .focusChanges()
            .subscribe {
                if (it) {
                    Log.d("test", "searchEditText focus on")
                    viewModel.isSelectKeyword.value = false
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
                val itemTotalLine = giphyItemAdapter.itemCount / COLUMN_NUM

                val searchText = binding.searchEditText.text.toString()

                if (searchText.isNotBlank() && lastVisibleLine == (itemTotalLine - SCROLL_LINE_OFFSET)) {
                    viewModel.getSearchGiphyList(searchText, LOAD_COUNT)
                }
            }

        binding.searchComplete
            .clicks()
            .subscribe {
                selectSearchKeyword(binding.searchEditText.text.toString())
            }

        giphyItemAdapter = GiphyItemAdapter()
        binding.giphyListView.adapter = giphyItemAdapter
        binding.giphyListView.layoutManager = GridLayoutManager(context, COLUMN_NUM)

        val autoCompleteClickListener = View.OnClickListener {
            val textView = it as TextView
            selectSearchKeyword(textView.text.toString())
        }
        autoCompleteItemAdapter = AutoCompleteItemAdapter(autoCompleteClickListener)
        binding.giphySearchAutoCompleteListView.adapter = autoCompleteItemAdapter
        binding.giphySearchAutoCompleteListView.layoutManager = LinearLayoutManager(context)
    }

    private fun initViewModel() {
        viewModel.initSubject()
        viewModel.giphyListLiveData.observe(viewLifecycleOwner) {
            giphyItemAdapter.submitList(it.toMutableList())
        }

        viewModel.searchMode.observe(viewLifecycleOwner) {
            binding.searchToggle.text = it.text
        }

        viewModel.blinkListLiveData.observe(viewLifecycleOwner) {
            giphyItemAdapter.blinkIndexList = it
            giphyItemAdapter.notifyDataSetChanged()
        }

        viewModel.autoCompleteListLiveData.observe(viewLifecycleOwner) {
            binding.giphySearchAutoCompleteListView.visibility = View.VISIBLE
            autoCompleteItemAdapter.submitList(it)
        }
    }

    private fun selectSearchKeyword(text: String) {
        viewModel.isSelectKeyword.value = true
        binding.searchEditText.setText(text)
        binding.giphySearchAutoCompleteListView.visibility = View.GONE

        val inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)

        binding.searchEditText.clearFocus()
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
