package com.example.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.data.db.dao.*
import com.example.data.repositories.impl.*
import com.example.data.repositories.impl.AuthRepositoryImp
import com.example.data.repositories.impl.CurrencyRepositoryImp
import com.example.data.repositories.impl.PocketRepositoryImp
import com.example.data.repositories.impl.RemoteDatabaseImpl
import com.example.data.repositories.impl.WalletRepositoryImp
import com.example.data.repositories.intrefaces.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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
        db: MyRoom,
        auth: AuthRepository
    ): CurrencyRepository = CurrencyRepositoryImp(
        remote = remote,
        local = db.CurrencyDao(),
        auth = auth
    )

    @Provides
    @Singleton
    fun providePocketRepository(
        remote: RemoteDatabase,
        db: MyRoom,
        auth: AuthRepository
    ): PocketRepository = PocketRepositoryImp(
        remote = remote,
        local = db.PocketDao(),
        auth = auth
    )

    @Provides
    @Singleton
    fun providePersonRepository(
        remote: RemoteDatabase,
        db: MyRoom,
        auth: AuthRepository
    ): PersonRepository = PersonRepositoryImp(
        remote = remote,
        local = db.PersonDao(),
        auth = auth
    )

    @Provides
    @Singleton
    fun provideWalletRepository(
        remote: RemoteDatabase,
        db: MyRoom,
        auth: AuthRepository
    ): WalletRepository = WalletRepositoryImp(
        remote = remote,
        local = db.WalletDao(),
        auth = auth
    )

    @Provides
    @Singleton
    fun provideTransactionRepository(
        remote: RemoteDatabase,
        db: MyRoom,
        remoteStorage: RemoteRepository,
        auth: AuthRepository
    ): TransactionRepository = TransactionRepositoryImp(
        remote = remote,
        local = db.TransactionDao(),
        remoteStorage = remoteStorage,
        auth = auth
    )

    @Provides
    @Singleton
    fun provideRemoteDatabase(): RemoteDatabase = RemoteDatabaseImpl(
        remote = Firebase
    )

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPrefRepository =
        SharedPrefRepositoryImpl(
            context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        )

    @Provides
    @Singleton
    fun provideRemoteStorage(): RemoteStorage = RemoteStorageImpl(
        remote = FirebaseStorage.getInstance().reference
    )

    @Provides
    @Singleton
    fun provideRemoteRepository(
        storage: RemoteStorage,
        database: RemoteDatabase,
        persons: PersonDao,
        pockets: PocketDao,
        currencies: CurrencyDao,
        wallets: WalletDao,
        transactions: TransactionDao,
        shared: SharedPrefRepository
    ): RemoteRepository = RemoteRepositoryImpl(
        storage,
        database,
        persons,
        pockets,
        currencies,
        wallets,
        transactions,
        shared
    )

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application): MyRoom = MyRoom.getInstance(app)

    @Provides
    @Singleton
    fun provideDaoCurrency(db: MyRoom): CurrencyDao = db.CurrencyDao()

    @Provides
    @Singleton
    fun provideDaoPocket(db: MyRoom): PocketDao = db.PocketDao()

    @Provides
    @Singleton
    fun provideDaoPersont(db: MyRoom): PersonDao = db.PersonDao()

    @Provides
    @Singleton
    fun provideDaoWallet(db: MyRoom): WalletDao = db.WalletDao()

    @Provides
    @Singleton
    fun provideDaoTransaction(db: MyRoom): TransactionDao = db.TransactionDao()

}