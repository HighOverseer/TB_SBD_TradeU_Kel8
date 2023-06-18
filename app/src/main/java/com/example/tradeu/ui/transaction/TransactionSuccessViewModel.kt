package com.example.tradeu.ui.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.tradeu.data.TransactionRepository
import com.example.tradeu.data.dao.TransactionDao

class TransactionSuccessViewModel(
    private val transactionRepository: TransactionRepository
):ViewModel() {
    private val _idTransactionSuccess = MutableLiveData<Long>()

    val transactionSuccessData = _idTransactionSuccess.switchMap {
        transactionRepository.getTransactionById(it)
    }

    fun setIdTransactionSuccess(id:Long){
        if (id != -1L){
            _idTransactionSuccess.value = id
        }else return

    }
}