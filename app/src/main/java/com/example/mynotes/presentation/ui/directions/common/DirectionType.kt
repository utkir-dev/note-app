package com.example.mynotes.presentation.ui.directions.common

sealed class DirectionType {
    object BACK: DirectionType()
    object HOME: DirectionType()
    object SIGNIN: DirectionType()
    object BALANCE: DirectionType()
    object INCOME: DirectionType()
    object OUTCOME: DirectionType()
    object GETCREDIT: DirectionType()
    object GIVECREDIT: DirectionType()
    object CREDITORS: DirectionType()
    object DEBETORS: DirectionType()
    object POCKETS: DirectionType()
    object CURRENCIES: DirectionType()
    object HISTORY: DirectionType()
    object SIGNOUT: DirectionType()
}