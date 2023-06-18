package com.example.tradeu.ui.productdetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.tradeu.databinding.ActivityProductDetailBinding
import com.example.tradeu.ui.ViewModelFactory
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import com.example.tradeu.*
import com.example.tradeu.data.entities.relations.ItemUserAndFavorite
import com.example.tradeu.ui.payment.PaymentActivity
import com.example.tradeu.ui.transaction.TransactionSuccessActivity


class ProductDetailActivity : AppCompatActivity() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var binding:ActivityProductDetailBinding
    private lateinit var productDetailViewModel: ProductDetailViewModel

    companion object{
        const val EXTRA_ITEM_ID = "itemId"
        const val SHARED_ELEMENT = "productPhoto"

    }

    private val transactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == TransactionSuccessActivity.EXTRA_INTENT_TRANSACTION_SUCCESS){
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productDetailViewModel = obtainViewModel()
        init()

        productDetailViewModel.message.observe(this){ singleEvent->
            singleEvent.getContentIfNotHandled()?.let {
                showToast(this, it)
            }
        }

        productDetailViewModel.productData.observe(this){itemData ->
            setLayout(itemData)
        }

        productDetailViewModel.latestSelectedItemSize.observe(this){itemSize ->
            setItemSizeButtons(itemSize)
        }

        binding.apply {
            ibBack.setOnClickListener {
                onBackPressed()
            }
            ibFavorite.setOnClickListener {
                setIbFavAction()
            }
            btnItemSizeS.setOnClickListener {
                productDetailViewModel.setLatestItemSize(ItemSizeEnum.S)
            }
            btnItemSizeM.setOnClickListener {
                productDetailViewModel.setLatestItemSize(ItemSizeEnum.M)
            }
            btnItemSizeL.setOnClickListener {
                productDetailViewModel.setLatestItemSize(ItemSizeEnum.L)
            }
            btnItemSizeXl.setOnClickListener {
                productDetailViewModel.setLatestItemSize(ItemSizeEnum.XL)
            }

            btnBuy.setOnClickListener {
                toPaymentActivity()
            }
        }
    }

    private fun toPaymentActivity() {
        val intent = Intent(this, PaymentActivity::class.java).apply {
            productDetailViewModel.productData.value?.let {
                putExtra(
                    PaymentActivity.EXTRA_ITEM_ID,
                    it.itemId
                )
            }?:return showToast(
                this@ProductDetailActivity,
                getString(
                    R.string.sorry_theres_some_mistake
                )
            )
            productDetailViewModel.latestSelectedItemSize.value?.let {
                putExtra(
                    PaymentActivity.EXTRA_ITEM_SIZE,
                    it.itemSizeId
                )
            }?:return showToast(
                this@ProductDetailActivity,
                getString(
                            R.string.please_choose_item_size
                )
            )
        }
        transactionLauncher.launch(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }

    private fun setIbFavAction() {
        val isFavorite = binding.ibFavorite.isActivated
        if (isFavorite){
            productDetailViewModel.deleteFavorite()
        }else productDetailViewModel.addFavorite()
    }

    private fun setLayout(itemData:ItemUserAndFavorite){
        binding.apply {
            ivItemPhoto.loadImage(this@ProductDetailActivity, itemData.itemPhoto)
            tvItemName.text = itemData.itemName
            tvLocation.text = itemData.address
            ibFavorite.isActivated = itemData.statusFavorite
            tvItemPrice.text = getString(R.string.balance_count, formatBalanceString(itemData.price))
            tvItemStock.text = getString(R.string.stock_info, itemData.stock.toString())
            tvDescription.text = itemData.description
            val isUserOwnedItem = itemData.sellerId == productDetailViewModel.userId.value
            if(isUserOwnedItem){
                btnBuy.isVisible = false
                tvInfoUserOwned.isVisible = true
            }else{
                if (itemData.stock > 0){
                    btnBuy.isEnabled = true
                }else{
                    btnBuy.isEnabled = false
                    productDetailViewModel.setMessage(getString(R.string.out_of_sale))
                }
                tvInfoUserOwned.isVisible = false
                btnBuy.isVisible = true
            }

        }
    }

    private fun setItemSizeButtons(itemSizeEnum: ItemSizeEnum){
        binding.apply {
            when(itemSizeEnum){
                ItemSizeEnum.S ->{
                    btnItemSizeM.isActivated = false
                    btnItemSizeL.isActivated = false
                    btnItemSizeXl.isActivated = false
                    btnItemSizeS.isActivated = true
                }
                ItemSizeEnum.M ->{
                    btnItemSizeS.isActivated = false
                    btnItemSizeL.isActivated = false
                    btnItemSizeXl.isActivated = false
                    btnItemSizeM.isActivated = true
                }
                ItemSizeEnum.L ->{
                    btnItemSizeS.isActivated = false
                    btnItemSizeM.isActivated = false
                    btnItemSizeXl.isActivated = false
                    btnItemSizeL.isActivated = true
                }
                ItemSizeEnum.XL ->{
                    btnItemSizeS.isActivated = false
                    btnItemSizeM.isActivated = false
                    btnItemSizeL.isActivated = false
                    btnItemSizeXl.isActivated = true
                }
            }
        }
    }

    private fun init(){
        val itemId = intent.getLongExtra(EXTRA_ITEM_ID, -1L)
        productDetailViewModel.initData(itemId)
    }

    private fun obtainViewModel():ProductDetailViewModel{
        val factory = ViewModelFactory.getInstance(application as MyApplication, dataStore)
        return ViewModelProvider(this, factory)[ProductDetailViewModel::class.java]
    }

}