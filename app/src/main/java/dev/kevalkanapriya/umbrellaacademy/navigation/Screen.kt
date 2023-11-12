package dev.kevalkanapriya.umbrellaacademy.navigation

sealed class Screen(
    val title: String,
    val route: String
) {

    object Onboarding: Screen(route = "on_boarding", title = "OnBoarding")
    object Home: Screen(route = "home_screen", title = "Home")
    object Questionnaire: Screen(route = "questionnaire_screen", title = "Questionnaire")
    object History: Screen(title = "history", route = "history_screen")

    object Profile: Screen(title = "profile", route = "profile_screen")
}
