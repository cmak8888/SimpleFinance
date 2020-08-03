package com.csci448.FP.simplefinance.data.credentials_data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import java.util.*

@Dao
interface AccountCredentialsDAO {

    @Query("SELECT * FROM accountcredentials")
    fun getCredentials(): DataSource.Factory<Int, AccountCredentials>

    @Query("SELECT * FROM accountcredentials WHERE id=(:id)")
    fun getCredential(id: UUID): LiveData<AccountCredentials?>

    @Update
    fun updateCredential(ac: AccountCredentials)

    @Insert
    fun addCredentials(ac: AccountCredentials)

    @Delete
    fun deleteGoal(ac: AccountCredentials)

    @Query("SELECT firstName FROM accountcredentials WHERE id=(:id)")
    fun getFirstName(id: UUID): String

}