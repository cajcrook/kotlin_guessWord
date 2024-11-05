package com.example.guessword

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class WordViewModel : ViewModel() {
    data class Dictionary(
        val wordID: Int,
    )

    val dictionary = mutableStateListOf(
        Dictionary(R.string.word1),
        Dictionary(R.string.word2),
        Dictionary(R.string.word3),
        Dictionary(R.string.word4),
        Dictionary(R.string.word5),
    )

    val words: List<Dictionary> get() = dictionary
    var currentIndex = mutableIntStateOf(0) // Track the index of the current question
    var correctCount = mutableIntStateOf(0) // Track the number of correct answers

    fun incrementCorrectCount() {
        correctCount.intValue++
    }

    fun nextWord() {
        val newIndex = (dictionary.indices - currentIndex.intValue).random() // Choose a random index that’s different from the current
        currentIndex.intValue = newIndex
    }

    fun initialWord() {
        dictionary.shuffle()
        currentIndex.intValue =Random.nextInt(0, dictionary.size)
    }

    fun resetWords() {
        currentIndex.intValue = 0
        correctCount.intValue = 0
        dictionary.shuffle()
    }


}
