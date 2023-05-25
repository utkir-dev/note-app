package com.example.mynotes.domain.use_cases.person_use_case

import com.example.data.db.models.PersonWithWallets
import com.example.data.repositories.intrefaces.PersonRepository
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PersonWithWalletsDomain
import com.example.mynotes.domain.use_cases.wallet_use_case.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPersonsWithWallets @Inject constructor(
    private val repository: PersonRepository
) {
    suspend operator fun invoke(): Flow<List<PersonWithWalletsDomain>> =
        repository.getPersonWithWallets().map { it.map { it.toDomain() } }

}

fun PersonWithWallets.toDomain() = PersonWithWalletsDomain(
    person = this.person.toDomain(),
    wallets = this.wallets.map { it.toDomain() }
)