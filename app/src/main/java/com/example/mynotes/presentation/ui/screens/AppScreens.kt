package com.example.mynotes.presentation.ui.screens


import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.domain.models.PocketDomain
import com.example.mynotes.presentation.ui.screens.auth.signin.SignInScreen
import com.example.mynotes.presentation.ui.screens.auth.signup.SignUpScreen
import com.example.mynotes.presentation.ui.screens.main.balance.BalanceScreen
import com.example.mynotes.presentation.ui.screens.main.convertation.ConvertationScreen
import com.example.mynotes.presentation.ui.screens.main.currencies.CurrencyScreen
import com.example.mynotes.presentation.ui.screens.main.getcredit.GetCreditScreen
import com.example.mynotes.presentation.ui.screens.main.givecredit.GiveCreditScreen
import com.example.mynotes.presentation.ui.screens.main.history.HistoryScreen
import com.example.mynotes.presentation.ui.screens.home.HomeScreen
import com.example.mynotes.presentation.ui.screens.main.income.IncomeScreen
import com.example.mynotes.presentation.ui.screens.main.outcome_currency.OutcomeCurrencyScreen
import com.example.mynotes.presentation.ui.screens.main.outcome_pocket.OutcomePocketScreen
import com.example.mynotes.presentation.ui.screens.main.person_info.PersonScreen
import com.example.mynotes.presentation.ui.screens.main.persons.PersonsScreen
import com.example.mynotes.presentation.ui.screens.main.pocket_info.PocketScreen
import com.example.mynotes.presentation.ui.screens.main.pockets.PocketsScreen
import com.example.mynotes.presentation.ui.screens.main.share.ShareScreen
import com.example.mynotes.presentation.ui.screens.settings.SettingsScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppScreens @Inject constructor() {
    fun settingsScreen(): SettingsScreen = SettingsScreen()
    fun shareScreen(): ShareScreen = ShareScreen()
    fun signInScreen(): SignInScreen = SignInScreen()
    fun signUpScreen(): SignUpScreen = SignUpScreen()
    fun homeScreen(): HomeScreen = HomeScreen()
    fun balanceScreen(): BalanceScreen = BalanceScreen()
    fun currencyScreen(): CurrencyScreen = CurrencyScreen()
    fun incomeScreen(): IncomeScreen = IncomeScreen()
    fun getCreditScreen(): GetCreditScreen = GetCreditScreen()
    fun giveCreditScreen(): GiveCreditScreen = GiveCreditScreen()
    fun getPersonsScreen(): PersonsScreen = PersonsScreen()
    fun convertationScreen(): ConvertationScreen = ConvertationScreen()
    fun getPocketsScreen(): PocketsScreen = PocketsScreen()
    fun getPocketScreen(pocket: PocketDomain): PocketScreen = PocketScreen(pocket)
    fun outcomePocketScreen(): OutcomePocketScreen = OutcomePocketScreen()
    fun outcomeCurrencyScreen(pocketId: String): OutcomeCurrencyScreen =
        OutcomeCurrencyScreen(pocketId)

    fun historyScreen(): HistoryScreen = HistoryScreen()
    fun personScreen(person: PersonDomain): PersonScreen = PersonScreen(person)
}