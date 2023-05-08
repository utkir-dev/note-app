package com.example.mynotes.di

import android.app.Application
import com.example.mynotes.presentation.ui.directions.SignInDirectionImpl
import com.example.mynotes.presentation.ui.directions.SignUpDirectionImpl
import com.example.mynotes.presentation.ui.directions.SplashDirectionImpl
import com.example.mynotes.presentation.ui.dispatcher.AppNavigator
import com.example.mynotes.presentation.ui.screens.AppScreens
import com.example.mynotes.presentation.ui.screens.auth.signin.SignInDirection
import com.example.mynotes.presentation.ui.screens.auth.signup.SignUpDirection
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
    fun provideSignUpDirection(
        navigator: AppNavigator
    ): SignUpDirection {
        return SignUpDirectionImpl(
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

    @AppScope
    @Provides
    @Singleton
    fun provideAppScope() = CoroutineScope(SupervisorJob())


}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AppScope