package com.example.mynotes.presentation.ui.directions.common

sealed class DirectionType {
    object BACK : DirectionType()
    object HOME : DirectionType()
    object SIGNIN : DirectionType()
    object BALANCE : DirectionType()
    object INCOME : DirectionType()
    object OUTCOME : DirectionType()
    object GETCREDIT : DirectionType()
    object GIVECREDIT : DirectionType()
    object CONVERTATION : DirectionType()
    object PERSONS : DirectionType()
    object POCKETS : DirectionType()
    object CURRENCIES : DirectionType()
    object HISTORY : DirectionType()
    object CHANGE_NIGHT_MODE : DirectionType()
    object SIGNOUT : DirectionType()
    object SETTINGS : DirectionType()
    object SHARE : DirectionType()
}