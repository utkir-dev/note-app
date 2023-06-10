package com.example.mynotes.di

import android.app.Application
import android.content.Context
import com.example.mynotes.presentation.ui.directions.*
import com.example.mynotes.presentation.ui.directions.common.*
import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.auth.signin.SignInDirection
import com.example.mynotes.presentation.ui.screens.auth.signup.SignUpDirection
import com.example.mynotes.presentation.ui.screens.main.balance.BalanceDirection
import com.example.mynotes.presentation.ui.screens.main.convertation.ConvertationDirection
import com.example.mynotes.presentation.ui.screens.main.currencies.CurrencyDirection
import com.example.mynotes.presentation.ui.screens.main.getcredit.GetCreditDirection
import com.example.mynotes.presentation.ui.screens.main.givecredit.GiveCreditDirection
import com.example.mynotes.presentation.ui.screens.main.history.HistoryDirection
import com.example.mynotes.presentation.ui.screens.home.HomeDirection
import com.example.mynotes.presentation.ui.screens.main.income.IncomeDirection
import com.example.mynotes.presentation.ui.screens.main.outcome_currency.OutcomeCurrencyDirection
import com.example.mynotes.presentation.ui.screens.main.outcome_pocket.OutcomePocketDirection
import com.example.mynotes.presentation.ui.screens.main.person_info.PersonDirection
import com.example.mynotes.presentation.ui.screens.main.persons.PersonsDirection
import com.example.mynotes.presentation.ui.screens.main.pocket_info.PocketDirection
import com.example.mynotes.presentation.ui.screens.main.pockets.PocketsDirection
import com.example.mynotes.presentation.ui.screens.splash.SplashDirection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(
        app: Application
    ): Context {
        return app
    }

    @Provides
    fun provideSignUpDirection(
        navigator: AppNavigator
    ): SignUpDirection {
        return SignUpDirectionImpl(
            navigator
        )
    }

    @Provides
    fun provideHomeDirection(
        navigator: AppNavigator,
        appScreens: AppScreens
    ): HomeDirection {
        return HomeDirectionImpl(
            navigator,
            appScreens
        )
    }

    @Provides
    fun provideBalanceDirection(
        navigator: AppNavigator,
    ): BalanceDirection {
        return BalanceDirectionImpl(
            navigator
        )
    }

    @Provides
    fun provideBackDirection(
        navigator: AppNavigator,
    ): BackDirection {
        return BackDirectionImpl(
            navigator
        )
    }

    @Provides
    fun provideSplashDirection(
        navigator: AppNavigator,
        appScreens: AppScreens
    ): SplashDirection {
        return SplashDirectionImpl(
            navigator,
            appScreens
        )
    }

    @Provides
    fun provideSignInDirection(
        navigator: AppNavigator,
        appScreens: AppScreens
    ): SignInDirection {
        return SignInDirectionImpl(
            navigator,
            appScreens
        )
    }

    @Provides
    fun provideCurrencyDirection(
        navigator: AppNavigator
    ): CurrencyDirection {
        return CurrencyDirectionImpl(
            navigator,
        )
    }

    @Provides
    fun provideIncomeDirection(
        navigator: AppNavigator
    ): IncomeDirection {
        return IncomeDirectionImpl(
            navigator
        )
    }

    @Provides
    fun provideOutcomePocketDirection(
        navigator: AppNavigator,
        appScreens: AppScreens
    ): OutcomePocketDirection {
        return OutcomePocketDirectionImpl(
            navigator,
            appScreens
        )
    }

    @Provides
    fun provideOutcomeCurrencyDirection(
        navigator: AppNavigator
    ): OutcomeCurrencyDirection {
        return OutcomeCurrencyDirectionImpl(
            navigator
        )
    }


    @Provides
    fun provideGetCreditDirection(
        navigator: AppNavigator
    ): GetCreditDirection {
        return GetCreditDirectionImpl(
            navigator
        )
    }

    @Provides
    fun provideGiveCreditDirection(
        navigator: AppNavigator
    ): GiveCreditDirection {
        return GiveCreditDirectionImpl(
            navigator
        )
    }

    @Provides
    fun provideConvertationDirection(
        navigator: AppNavigator
    ): ConvertationDirection {
        return ConvertationDirectionImpl(
            navigator
        )
    }

    @Provides
    fun providePersonsDirection(
        navigator: AppNavigator,
        appScreens: AppScreens,

        ): PersonsDirection {
        return PersonsDirectionImpl(
            navigator,
            appScreens
        )
    }

    @Provides
    fun providePersonDirection(
        navigator: AppNavigator
    ): PersonDirection {
        return PersonDirectionImpl(
            navigator
        )
    }

    @Provides
    fun providePocketsDirection(
        navigator: AppNavigator,
        appScreens: AppScreens
    ): PocketsDirection {
        return PocketsDirectionImpl(
            navigator, appScreens
        )
    }

    @Provides
    fun providePocketDirection(
        navigator: AppNavigator
    ): PocketDirection {
        return PocketDirectionImpl(
            navigator
        )
    }


    @Provides
    fun provideHistoryDirectionImpl(
        navigator: AppNavigator
    ): HistoryDirection {
        return HistoryDirectionImpl(
            navigator
        )
    }

    @AppScope
    @Provides
    @Singleton
    fun provideAppScope() = CoroutineScope(SupervisorJob())


}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AppScope