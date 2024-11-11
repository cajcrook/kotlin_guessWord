package com.example.guessword

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guessword.ui.theme.GuessWordTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuessWordTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    App(viewModel())
                }
            }
        }
    }
}

@Composable
fun App(wordViewModel: WordViewModel = viewModel()) {
    val words = wordViewModel.words

    var currentWord = stringResource(words[wordViewModel.currentIndex.intValue].wordID)

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            GuessWord(
                guess = currentWord,
                onSubmitAnswer = { answer ->
                    if (answer.equals(currentWord, ignoreCase = true)) {
                        wordViewModel.incrementCorrectCount()
                        wordViewModel.nextWord() // Move to the next word in ViewModel
                    }
                }
            )
        }
    }
}

fun getDisplayWord(word: String, revealedIndices: List<Int>): String {
    return word.mapIndexed { index, char ->
        if (index in revealedIndices) char.toString() else "_"
    }.joinToString(" ") // Join with a space for readability
}


@Composable
fun GuessWord(
    guess: String,
    onSubmitAnswer: (String) -> Unit
) {
    var answer by remember { mutableStateOf("") } // State to hold the user's answer input
    var isCorrect by remember { mutableStateOf(false) } // State to track if the answer is correct
    var feedbackMessage by remember { mutableStateOf("") } // State for feedback message

    val revealedIndices = listOf(0, guess.length - 1) // Revealing first and last letters
    val displayWord = getDisplayWord(guess, revealedIndices) // Get the display word with underscores

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the current word with revealed letters
        Text(
            text = "Take a guess: $displayWord", // Update to show underscores and revealed letters
            textAlign = TextAlign.Center,
        )
        TextField(
            value = answer,
            onValueChange = {
                answer = it
                feedbackMessage = "" // Clear feedback message on input change
                isCorrect = false // Reset correctness status
            },
            label = { Text(text = "Guess here") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Button(onClick = {
            onSubmitAnswer(answer)
            isCorrect = (answer.equals(guess, ignoreCase = true)) // Check if the answer is correct
            feedbackMessage = if (isCorrect) {
                "Correct! Click the button below for the next word."
            } else {
                "Try again!"
            }
            answer = "" // Clear the input field after submission
        }) {
            Text(text = "Submit Guess")
        }
        // Display the feedback message
        Text(
            text = feedbackMessage,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Show the Next Question button only when the answer is correct
        if (isCorrect) {
            Button(
                onClick = {
                    answer = ""
                    isCorrect = false // Reset correctness status for the next word
                    onSubmitAnswer("") // Update your ViewModel accordingly
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Next Question")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GuessWordPreview() {
    GuessWordTheme {
        GuessWord(guess = "example", onSubmitAnswer = {})
    }
}
