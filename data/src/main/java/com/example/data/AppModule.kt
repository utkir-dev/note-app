package com.example.data

import android.app.Application
import com.example.mynotes.data.db.dao.CurrencyDao
import com.example.mynotes.data.db.dao.WalletDao
import com.example.mynotes.data.db.room.MyRoom
import com.example.mynotes.data.repositories.AuthRepository
import com.example.mynotes.data.repositories.imp.AuthRepositoryImp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAuthRepository(): AuthRepository =
        AuthRepositoryImp(
            auth = Firebase.auth
        )

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application) = MyRoom.getInstance(app)

    @Provides
    @Singleton
    fun provideDaoCurrency(db: MyRoom): CurrencyDao = db.CurrencyDao()

    @Provides
    @Singleton
    fun provideDaoWallet(db: MyRoom): WalletDao = db.WalletDao()

}