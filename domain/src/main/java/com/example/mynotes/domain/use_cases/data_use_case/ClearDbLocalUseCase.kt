package com.example.mynotes.domain.use_cases.data_use_case

import com.example.data.db.dao.MyRoom
import com.example.data.repositories.intrefaces.SharedPrefRepository
import javax.inject.Inject

class ClearDbLocalUseCase @Inject constructor(
    private val db: MyRoom,
    private val shared: SharedPrefRepository
) {
    suspend operator fun invoke() {
        shared.clearCash()
        db.CurrencyDao().clear()
        db.WalletDao().clear()
        db.PersonDao().clear()
        db.PocketDao().clear()
        db.TransactionDao().clear()
    }
}