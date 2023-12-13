package com.example.noteappproject.domain.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
