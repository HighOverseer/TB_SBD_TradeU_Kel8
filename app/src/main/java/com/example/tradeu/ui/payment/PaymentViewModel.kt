package com.example.tradeu.ui.payment

import androidx.lifecycle.*
import com.example.tradeu.data.ItemRepository
import com.example.tradeu.data.TransactionRepository
import com.example.tradeu.data.UserRepository
import com.example.tradeu.helper.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(
    private var userRepository: UserRepository,
    private var itemRepository: ItemRepository,
    private var transactionRepository: TransactionRepository
):ViewModel() {

    private val _itemId = MutableLiveData<Long>()
    private var userId = -1L
    private var itemSizeId = -1L

    private val _latestQuantity = MutableLiveData<Short>()



    val totalPayment = MediatorLiveData<Long>()

    val itemData = _itemId.switchMap {
        itemRepository.getItemByIdAndItsFavoriteStatus(userId, it)
    }

    val userData = _itemId.switchMap {
        userRepository.getUserData(userId)
    }

    private val _idTransactionSuccess = MutableLiveData<Long>()
    val idTransactionSuccess:LiveData<Long> = _idTransactionSuccess

    private val _message = MutableLiveData<SingleEvent<String>>()
    val messsage:LiveData<SingleEvent<String>> = _message

    fun makeTransaction(
        buyerName:String,
        buyerAddress:String,
        buyerContact:String,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _idTransactionSuccess.postValue(
                transactionRepository.insertTransaction(
                    userId,
                    buyerName,
                    buyerAddress,
                    buyerContact,
                    _latestQuantity.value!!,
                    _itemId.value!!,
                    itemData.value?.price!!,
                    itemSizeId
                )
            )
        }
    }

    fun setQuantity(newQuantity:Short){
        _latestQuantity.value = newQuantity
    }

    fun setMessage(message:String){
        _message.value = SingleEvent(message)
    }

    fun initData(itemId:Long, itemSizeId:Long){

        if (itemId != -1L && itemSizeId != -1L){
            viewModelScope.launch (Dispatchers.IO){
                userId = userRepository.getUserId()
                if (userId != -1L){
                    this@PaymentViewModel.itemSizeId = itemSizeId
                    _itemId.postValue(itemId)
                }
            }
        }else return
    }

    init {

        totalPayment.addSource(itemData){newItemData ->
            _latestQuantity.value?.let {
                val total = newItemData.price * it + 10_000
                totalPayment.value = total
                userData.value?.balance?.let {userBalance ->
                    if (userBalance < total){
                        setMessage("You may have not enough balance in your wallet !")
                    }
                }
            }
        }


        totalPayment.addSource(_latestQuantity){newQuantity ->
            itemData.value?.price?.let {
                val total = it * newQuantity + 10_000
                totalPayment.value =  total
                userData.value?.balance?.let {userBalance ->
                    if (userBalance < total){
                        setMessage("You may have not enough balance in your wallet !")
                    }
                }

            }
        }

        totalPayment.addSource(userData){user->
            totalPayment.value?.let {
                if (user.balance < it){
                    setMessage("You may have not enough balance in your wallet !")
                    totalPayment.value = totalPayment.value
                }
            }

        }
    }
}