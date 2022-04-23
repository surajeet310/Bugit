package com.herokuapp.serverbugit.bugit.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.herokuapp.serverbugit.bugit.data.AccountRepo

class AccountViewModelFactory(private val accountRepo: AccountRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(accountRepo) as T
    }
}