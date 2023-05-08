package com.example.data.di

import android.app.Application
import com.example.data.db.CurrencyDao
import com.example.data.db.MyRoom
import com.example.data.repositories.AuthRepository
import com.example.data.repositories.CurrencyRepository
import com.example.data.repositories.RemoteDatabase
import com.example.data.repositories.impl.AuthRepositoryImp
import com.example.data.repositories.impl.CurrencyRepositoryImp
import com.example.data.repositories.impl.RemoteDatabaseImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository =
        AuthRepositoryImp(
            auth = Firebase.auth
        )

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        remote: RemoteDatabase,
        db: MyRoom
    ): CurrencyRepository = CurrencyRepositoryImp(
        remote = remote,
        local = db.CurrencyDao()
    )

    @Provides
    @Singleton
    fun provideRemoteDatabase(): RemoteDatabase = RemoteDatabaseImpl(
        remote = Firebase
    )

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application): MyRoom = MyRoom.getInstance(app)

    @Provides
    @Singleton
    fun provideDaoCurrency(db: MyRoom): CurrencyDao = db.CurrencyDao()

//    @Provides
//    @Singleton
//    fun provideDaoWallet(db: MyRoom): WalletDao = db.WalletDao()

}