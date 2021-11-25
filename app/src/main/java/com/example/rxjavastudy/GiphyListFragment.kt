package com.example.rxjavastudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rxjavastudy.databinding.FragmentGiphyListBinding

class GiphyListFragment : Fragment() {
    private var _binding: FragmentGiphyListBinding? = null
    private val binding get() = _binding!!

    private val giphyItemList = ArrayList<GiphyItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGiphyListBinding.inflate(inflater, container, false)

        val gridLayoutManager = GridLayoutManager(context, 3)
        val adapter = GiphyItemAdapter(giphyItemList)
        binding.giphyListView.adapter = adapter
        binding.giphyListView.layoutManager = gridLayoutManager

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(): GiphyListFragment {
            return GiphyListFragment()
        }
    }
}
