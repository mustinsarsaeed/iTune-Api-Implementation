package com.example.itune

data class TuneResponse(
    val resultCount: Int,
    val results: List<Results>
)