package com.example.tradeu.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.tradeu.data.entities.User
import kotlinx.coroutines.flow.first

class UserAuthManager private constructor(
    private val dataStore: DataStore<Preferences>
){

    suspend fun getUserId():Long{
        val preferences = dataStore.data.first()
        return preferences[USER_ID]?:-1
    }

    suspend fun saveUserId(userId:Long){
        dataStore.edit {pref ->
            pref[USER_ID] = userId
        }
    }

    suspend fun deleteUserId(){
        dataStore.edit {pref ->
            pref[USER_ID] = -1
        }
    }

    companion object{
        private val USER_ID = longPreferencesKey("user_id")

        private var INSTANCE:UserAuthManager?=null

        fun getInstance(dataStore: DataStore<Preferences>):UserAuthManager{
            return INSTANCE?: synchronized(this){
                INSTANCE?:UserAuthManager(dataStore)
            }.also { INSTANCE = it }
        }
    }
}