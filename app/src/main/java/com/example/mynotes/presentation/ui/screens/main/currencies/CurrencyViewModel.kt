package com.example.mynotes.presentation.ui.screens.main.currencies


interface CurrencyViewModel {
    fun add(name: String, rate: Double)
    fun update()
    fun delete()
}