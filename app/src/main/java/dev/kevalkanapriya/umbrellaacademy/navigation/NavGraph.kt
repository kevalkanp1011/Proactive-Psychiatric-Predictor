package dev.kevalkanapriya.umbrellaacademy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kevalkanapriya.umbrellaacademy.presentation.home.HomeScreen
import dev.kevalkanapriya.umbrellaacademy.presentation.home.HomeViewModel
import dev.kevalkanapriya.umbrellaacademy.presentation.onboarding.OnBoardingScreen
import dev.kevalkanapriya.umbrellaacademy.presentation.questionnaire.QuestionnaireScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    startDestination: String
) {

    val viewModel: HomeViewModel = koinViewModel()
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Home.route) {
            HomeScreen(viewModel)
        }

        composable(Screen.Questionnaire.route) {
            QuestionnaireScreen()
        }

        composable(Screen.Onboarding.route) {
            OnBoardingScreen()
        }
    }

}