package com.example.app_37_brilliantapp.data

enum class Difficulty {
    ELEMENTARY,
    EASY,
    MEDIUM,
    HARD,
    CHALLENGING
}

data class EarnedDiamond(var title: String, var deadline: String, var time: String, var difficulty: Difficulty) {

    @Suppress("unused")
    constructor() : this("", "", "", Difficulty.ELEMENTARY)

}