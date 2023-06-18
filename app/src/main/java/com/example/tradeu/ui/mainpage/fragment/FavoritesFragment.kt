package com.example.tradeu.ui.mainpage.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeu.MyApplication
import com.example.tradeu.R
import com.example.tradeu.data.entities.relations.ItemAndFavorite
import com.example.tradeu.databinding.FragmentFavoritesBinding
import com.example.tradeu.showToast
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.mainpage.adapter.ListItemFavoriteAdapter
import com.example.tradeu.ui.mainpage.domain.OnItemClickListener
import com.example.tradeu.ui.mainpage.viewmodels.FavoritesViewModel
import com.example.tradeu.ui.productdetail.ProductDetailActivity


class FavoritesFragment : Fragment() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var favoritesViewModel:FavoritesViewModel

    private var _binding:FragmentFavoritesBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel = obtainViewModel()
        init()

        favoritesViewModel.listFavoriteItems.observe(viewLifecycleOwner){listFavoriteItem ->
            setRv(listFavoriteItem)
        }

        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn).setOnClickListener {
            binding.searchView.apply {
                setQuery("", false)
                clearFocus()
                favoritesViewModel.getListFavoriteSearch(null)
            }
        }

        favoritesViewModel.message.observe(viewLifecycleOwner){singleEventMessage ->
            singleEventMessage.getContentIfNotHandled()?.let {
                showToast(requireContext(), it)
            }
        }
    }

    private fun setRv(listFavoriteItem:List<ItemAndFavorite>?){
        if(listFavoriteItem!=null && listFavoriteItem.isNotEmpty()){
            val searchQuery = favoritesViewModel.searchQuery
            if (searchQuery != null){
                println(searchQuery)
                val sortedList = listFavoriteItem.filter { it.itemAndUser.item.itemName.lowercase().contains(searchQuery) }
                binding.rvFavorite.adapter = getAdapter(sortedList)
                if (sortedList.isEmpty()) {
                    favoritesViewModel.setSingleEventMessage(getString(R.string.item_not_found))
                    favoritesViewModel.getListFavoriteSearch(null)
                }
            }else{
                binding.rvFavorite.adapter = getAdapter(listFavoriteItem)
            }
            binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        }else {
            binding.rvFavorite.adapter = getAdapter(emptyList())
            favoritesViewModel.setSingleEventMessage(getString(R.string.item_not_found))
        }


    }

    private fun getAdapter(listFavoriteItem: List<ItemAndFavorite>):ListItemFavoriteAdapter{
        return ListItemFavoriteAdapter(listFavoriteItem, object : OnItemClickListener<ItemAndFavorite>{
            override fun onClick(value: ItemAndFavorite, sharedElementTransition: ActivityOptionsCompat) {
                value.itemAndUser.item.itemId?.let {
                    val intent = Intent(requireActivity(),
                        ProductDetailActivity::class.java
                    ).putExtra(
                        ProductDetailActivity.EXTRA_ITEM_ID,
                        it
                    )
                    startActivity(intent, sharedElementTransition.toBundle())
                }?:favoritesViewModel.setSingleEventMessage(getString(R.string.sorry_theres_some_mistake))

            }

            override fun updateFavoriteStatus(itemId: Long, isFavorite: Boolean) {
                favoritesViewModel.deleteFavorite(itemId)
            }
        })
    }

    private fun obtainViewModel():FavoritesViewModel{
        val app = requireActivity().application as MyApplication
        val factory = ViewModelFactory.getInstance(app, requireContext().dataStore)
        return ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
    }

    private fun init(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { favoritesViewModel.getListFavoriteSearch(it) }
                closeKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun closeKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}