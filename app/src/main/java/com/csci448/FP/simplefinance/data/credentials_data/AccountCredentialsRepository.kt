package com.csci448.FP.simplefinance.data.credentials_data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.*
import java.util.concurrent.Executors

class AccountCredentialsRepository(private val acDao: AccountCredentialsDAO) {

    private val executor = Executors.newSingleThreadExecutor()

    fun getCredentials(): LiveData<PagedList<AccountCredentials>> {
        return LivePagedListBuilder(
            acDao.getCredentials(),
            PagedList.Config.Builder().setPageSize(125).setPrefetchDistance(50).setMaxSize(400).build()
        ).build()
    }

    fun getCredential(id: UUID): LiveData<AccountCredentials?> {
        return acDao.getCredential(id)
    }

    fun updateCredential(credential: AccountCredentials) {
        executor.execute{
            acDao.updateCredential(credential)
        }
    }

    fun addCredential(credential: AccountCredentials) {
        executor.execute {
            acDao.addCredentials(credential)
        }
    }

    fun getFirstName(id: UUID): String {
        return acDao.getFirstName(id)
    }

    fun deleteCredential(ac: AccountCredentials) {
        executor.execute{
            acDao.deleteGoal(ac)
        }
    }

    companion object {
        private var instance: AccountCredentialsRepository? = null
        fun getInstance(context: Context): AccountCredentialsRepository? {
            return instance ?: let {
                if (instance == null) {
                    val database = AccountCredentialsDatabase.getInstance(context)
                    instance = AccountCredentialsRepository(database.accountCredentialsDao())
                }
                return instance
            }
        }
    }

}

