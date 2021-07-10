package com.hieuwu.groceriesstore.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hieuwu.groceriesstore.R
import com.hieuwu.groceriesstore.presentation.adapters.LineListItemAdapter
import com.hieuwu.groceriesstore.databinding.FragmentFavouriteBinding

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentFavouriteBinding>(
            inflater, R.layout.fragment_favourite, container, false
        )

        var navController = NavHostFragment.findNavController(this)
        var bottomNavView = binding.bottomNavView
        bottomNavView.setupWithNavController(navController)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var dataSet: ArrayList<String> = ArrayList()
        dataSet.add("a")
        dataSet.add("b")
        dataSet.add("a")
        dataSet.add("b")
        dataSet.add("a")
        dataSet.add("b")
        dataSet.add("a")
        dataSet.add("b")
        dataSet.add("a")
        dataSet.add("b")

        setUpRecyclerView(dataSet)
    }

    private fun setUpRecyclerView(dataSet: ArrayList<String>) {
//        val adapter =
//            LineListItemAdapter(
//                dataSet
//            )
//        binding.favouriteRecyclerview.adapter = adapter
//        binding.favouriteRecyclerview.layoutManager = LinearLayoutManager(context)
    }
}