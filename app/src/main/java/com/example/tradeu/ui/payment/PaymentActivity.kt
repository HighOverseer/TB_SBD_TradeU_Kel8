package com.example.tradeu.ui.payment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.tradeu.MyApplication
import com.example.tradeu.R
import com.example.tradeu.databinding.ActivityPaymentBinding
import com.example.tradeu.formatBalanceString
import com.example.tradeu.showToast
import com.example.tradeu.ui.ViewModelFactory
import com.example.tradeu.ui.transaction.TransactionSuccessActivity

class PaymentActivity : AppCompatActivity(), TextWatcher {
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("userAuth")
    private lateinit var paymentViewModel:PaymentViewModel
    private lateinit var binding:ActivityPaymentBinding
    companion object{
        const val EXTRA_ITEM_ID = "itemId"
        const val EXTRA_ITEM_SIZE  = "itemSize"
    }

    private val transactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == TransactionSuccessActivity.EXTRA_INTENT_TRANSACTION_SUCCESS){
            setResult(TransactionSuccessActivity.EXTRA_INTENT_TRANSACTION_SUCCESS)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentViewModel = obtainViewModel()
        init()

        paymentViewModel.messsage.observe(this){singleEvent ->
            singleEvent.getContentIfNotHandled()?.let {
                showToast(this@PaymentActivity, it)
            }
        }

        paymentViewModel.idTransactionSuccess.observe(this){idTransactionSuccess ->
            toTransactionSuccessAct(idTransactionSuccess)
        }

        paymentViewModel.totalPayment.observe(this){
            setLayout(it)
        }


        binding.apply {
            ibBack.setOnClickListener {
                onBackPressed()
            }
            btnOrder.setOnClickListener {
                makeTransaction()
            }
        }

    }

    private fun toTransactionSuccessAct(idTransactionSuccess: Long?) {
        binding.apply {
            if (idTransactionSuccess!=null && idTransactionSuccess != -1L){
                val intent = Intent(this@PaymentActivity, TransactionSuccessActivity::class.java).apply {
                    putExtra(TransactionSuccessActivity.EXTRA_TRANSACTION_ID, idTransactionSuccess)
                }
                btnOrder.isEnabled = true
                progressBar.isVisible = false
                setFormBlank()
                transactionLauncher.launch(intent)
            }else{
                btnOrder.isEnabled = true
                progressBar.isVisible = false
                paymentViewModel.setMessage(getString(R.string.sorry_theres_some_mistake))
            }
        }
    }

    private fun setFormBlank() {
        binding.apply {
            etQuantity.text?.clear()
            etBuyerName.text?.clear()
            etBuyerAddress.text?.clear()
            etBuyerContact.text?.clear()
        }
    }

    private fun makeTransaction(){
        binding.apply {
            val buyerName = etBuyerName.text.toString().trim()
            val buyerAddress = etBuyerAddress.text.toString().trim()
            val buyerContact = etBuyerContact.text.toString().trim()
            if (buyerName.isNotEmpty() && buyerAddress.isNotEmpty() && buyerContact.isNotEmpty()){
                btnOrder.isEnabled = false
                progressBar.isVisible = true
                paymentViewModel.makeTransaction(
                    buyerName,
                    buyerAddress,
                    buyerContact
                )
            }else showToast(this@PaymentActivity, getString(R.string.fill_the_form))
        }
    }

    private fun setLayout(totalPayment:Long){
        binding.apply {
            paymentViewModel.itemData.value?.price?.let {

                tvSubtotal.text = getString(R.string.balance_count, formatBalanceString(it) )
                tvDeliveryFee.text = getString(R.string.balance_count, formatBalanceString(10_000) )
                tvTotal.text = getString(R.string.balance_count, formatBalanceString(totalPayment))
                paymentViewModel.userData.value?.balance?.let { userBalance ->
                    tvUserBalance.text = getString(R.string.balance_count, formatBalanceString(userBalance) )
                    btnOrder.isEnabled = userBalance >= totalPayment
                }

            }
        }
    }

    private fun obtainViewModel():PaymentViewModel{
        val factory = ViewModelFactory.getInstance(application as MyApplication, dataStore)
        return ViewModelProvider(this, factory)[PaymentViewModel::class.java]
    }

    private fun init(){
        val itemId = intent.getLongExtra(EXTRA_ITEM_ID, -1L)
        val itemSizeId = intent.getLongExtra(EXTRA_ITEM_SIZE,-1L)
        paymentViewModel.initData(itemId, itemSizeId)
        binding.etQuantity.addTextChangedListener(this)
        paymentViewModel.setQuantity(1)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (s.toString().isNotEmpty()){
            paymentViewModel.setQuantity(s.toString().toShort())
        }else paymentViewModel.setQuantity(1)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}