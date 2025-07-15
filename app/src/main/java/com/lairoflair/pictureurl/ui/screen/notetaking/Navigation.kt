package com.lairoflair.pictureurl.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NoteAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNewNoteClick = { navController.navigate("new_note") }
            )
        }
        composable("new_note") {
            NoteTakingScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
