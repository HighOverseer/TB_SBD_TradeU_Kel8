package com.example.tradeu.ui.mainpage.fragment

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tradeu.*
import com.example.tradeu.data.entities.Item
import com.example.tradeu.data.entities.User
import com.example.tradeu.data.entities.relations.ItemUserAndFavorite
import com.example.tradeu.databinding.FragmentHomeBinding
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.mainpage.domain.HorizontalSpacingItemDecoration
import com.example.tradeu.ui.mainpage.adapter.ListItemAdapter
import com.example.tradeu.ui.mainpage.domain.HomeTab
import com.example.tradeu.ui.mainpage.domain.OnItemClickListener
import com.example.tradeu.ui.mainpage.viewmodels.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var homeViewModel:HomeViewModel
    private var _binding:FragmentHomeBinding?=null
    private val binding:FragmentHomeBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        homeViewModel = obtainViewModel()

        homeViewModel.listItem.observe(viewLifecycleOwner){listItem ->
            setRvAndTab(listItem)
        }

        homeViewModel.userData.observe(viewLifecycleOwner){userData ->
            setLayout(userData)
        }


        binding.apply {
            btnAll.setOnClickListener {
                homeViewModel.setCurrentTab(HomeTab.All)
            }
            btnNew.setOnClickListener {
                homeViewModel.setCurrentTab(HomeTab.New)
            }
            btnTrending.setOnClickListener {
                homeViewModel.setCurrentTab(HomeTab.TrendingNow)
            }

        }

        binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).setOnClickListener {
            binding.searchView.apply {
                setQuery("", false)
                clearFocus()
            }
            homeViewModel.setCurrentTab(HomeTab.All)
        }

    }



    private fun setLayout(user: User){
        binding.apply {
            civProfilePhoto.loadImage(requireContext(), user.profilePhoto)
            tvGreeting.text = getString(R.string.greeting, user.name)
            tvWalletCount.text = getString(R.string.balance_count, formatBalanceString(user.balance))

        }
    }


    private fun setRvAndTab(listItem:List<Any>){
        binding.apply {
            if (listItem.isNotEmpty()){
                progressBar.isVisible = true
                when(homeViewModel.getCurrentTab()){
                    null -> {
                        if (listItem[0] is Item){
                            btnNew.isActivated = false
                            btnTrending.isActivated = false
                            btnAll.isActivated = false
                            val listAllItem = mutableListOf<Item>()
                            listItem.asSequence().forEach { listAllItem.add(it as Item) }
                            rvListItem.adapter = getAdapater(listAllItem)
                            rvListItem.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                            progressBar.isVisible = false
                        }
                    }
                    HomeTab.All -> {
                        btnNew.isActivated = false
                        btnTrending.isActivated = false
                        btnAll.isActivated = true
                        if (listItem[0] is Item){
                            val listAllItem = mutableListOf<Item>()
                            listItem.asSequence().forEach { listAllItem.add(it as Item) }
                            rvListItem.adapter = getAdapater(listAllItem)
                            rvListItem.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                            progressBar.isVisible = false
                        }
                    }
                    HomeTab.New ->{
                        btnTrending.isActivated = false
                        btnAll.isActivated = false
                        btnNew.isActivated = true
                        if (listItem[0] is Item){
                            val listNewItems = mutableListOf<Item>()
                            listItem.asSequence().forEach { listNewItems.add(it as Item) }
                            rvListItem.adapter = getAdapater(listNewItems)
                            rvListItem.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                            progressBar.isVisible = false
                        }
                    }
                    HomeTab.TrendingNow ->{
                        btnAll.isActivated = false
                        btnNew.isActivated = false
                        btnTrending.isActivated = true
                        if (listItem[0] is ItemUserAndFavorite){
                            lifecycleScope.launch(Dispatchers.Default){
                                val listTrendingItemsAndFavorite = mutableListOf<ItemUserAndFavorite>()
                                listItem.asSequence().forEach { listTrendingItemsAndFavorite.add(it as ItemUserAndFavorite) }
                                val sortedList = listTrendingItemsAndFavorite.asSequence().sortedByDescending { it.listUserCount }
                                val listItems = mutableListOf<Item>()
                                sortedList.forEach { listItems.add(it.item) }
                                withContext(Dispatchers.Main){
                                    rvListItem.adapter = getAdapater(listItems)
                                    rvListItem.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                                    progressBar.isVisible = false
                                }

                            }
                        }
                    }

                }

            }else {
                showSnackbar(binding.root, getString(R.string.item_not_found))
                homeViewModel.setCurrentTab(HomeTab.All)
            }
        }
    }

    private fun getAdapater(listItem: List<Item>):ListItemAdapter{
        return ListItemAdapter(listItem, object : OnItemClickListener<Item> {
            override fun onClick(value: Item) {
                println(value)
            }
        })
    }


    private fun obtainViewModel():HomeViewModel{
        val app = requireActivity().application as MyApplication
        val factory = ViewModelFactory.getInstance(app, requireContext().dataStore)
        return ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    private fun Float.toDp():Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }

    private fun init(){
        binding.apply {
            val divider = HorizontalSpacingItemDecoration(13f.toDp().toInt())
            rvListItem.addItemDecoration(divider)
            searchView.setOnQueryTextListener(object : OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { homeViewModel.getListItemSearch(it) }
                    closeKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }

    private fun closeKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}