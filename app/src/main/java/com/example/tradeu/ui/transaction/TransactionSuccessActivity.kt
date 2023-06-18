package com.example.tradeu.ui.transaction

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.tradeu.*
import com.example.tradeu.data.entities.relations.TransactionItemAndItemSize
import com.example.tradeu.databinding.ActivityTransactionSuccessBinding
import com.example.tradeu.ui.ViewModelFactory

class TransactionSuccessActivity : AppCompatActivity() {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var transactionSuccessViewModel:TransactionSuccessViewModel
    private lateinit var binding:ActivityTransactionSuccessBinding
    companion object{
        const val EXTRA_TRANSACTION_ID = "transactionId"

        const val EXTRA_INTENT_TRANSACTION_SUCCESS = 10
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        transactionSuccessViewModel = obtainViewModel()
        init()

        transactionSuccessViewModel.transactionSuccessData.observe(this){
            setLayout(it)
        }

        binding.apply {
            btnBackHome.setOnClickListener {
                setResult(EXTRA_INTENT_TRANSACTION_SUCCESS)
                finish()
            }
            btnSaveTransaction.setOnClickListener {
                finish()
            }
        }
    }

    private fun setLayout(transactionData:TransactionItemAndItemSize){
        binding.apply {
            tvTotalPrice.text = getString(R.string.balance_count, formatBalanceString(transactionData.transaction.total))
            tvNoTransaction.text = transactionData.transaction.transactionNumber.toString()
            tvBuyer.text = transactionData.transaction.buyerName
            tvProductName.text = transactionData.item.itemName
            tvItemSize.text = transactionData.itemSize.sizeCode
            tvDateTime.text = transactionData.transaction.transactionDate.toFormatString()
        }
    }

    private fun obtainViewModel(): TransactionSuccessViewModel {
        val factory = ViewModelFactory.getInstance(application as MyApplication, dataStore)
        return ViewModelProvider(this, factory)[TransactionSuccessViewModel::class.java]
    }

    private fun init(){
        val idTransactionSuccess = intent.getLongExtra(EXTRA_TRANSACTION_ID, -1L)
        transactionSuccessViewModel.setIdTransactionSuccess(idTransactionSuccess)
    }
}