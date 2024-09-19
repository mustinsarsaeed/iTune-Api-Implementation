package com.example.itune.model

data class TuneResponse(
    val resultCount: Int,
    val results: List<Results>
)