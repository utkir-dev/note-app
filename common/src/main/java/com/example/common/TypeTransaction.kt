package com.example.common

fun getTypeNumber(type: Type): Int = when (type) {
    Type.INCOME -> 1
    Type.OUTCOME -> 2
    Type.CREDIT -> 3
    Type.DEBET -> 4
    Type.CONVERTATION -> 5
}

fun getTypeText(type: Type) = when (type) {
    Type.INCOME -> "Kirim"
    Type.OUTCOME -> "Chiqim"
    Type.CREDIT -> "Qarz olish"
    Type.DEBET -> "Qarz berish"
    Type.CONVERTATION -> "Konvertaciya"
}

fun getTypeText(n: Int) = when (n) {
    1 -> getTypeText(Type.INCOME)
    2 -> getTypeText(Type.OUTCOME)
    3 -> getTypeText(Type.CREDIT)
    4 -> getTypeText(Type.DEBET)
    else -> getTypeText(Type.CONVERTATION)
}

fun getTypeEnum(n: Int) = when (n) {
    1 -> Type.INCOME
    2 -> Type.OUTCOME
    3 -> Type.CREDIT
    4 -> Type.DEBET
    else -> Type.CONVERTATION
}