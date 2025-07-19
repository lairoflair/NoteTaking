package com.lairoflair.notes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lairoflair.notes.ui.screen.home.HomeScreen
import com.lairoflair.notes.ui.screen.notetaking.NoteTakingScreen


@Composable
fun NoteAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNewNoteClick = { navController.navigate("new_note") },
                onNoteClick = { fileName -> navController.navigate("edit_note/$fileName")}
            )
        }
        composable("new_note") {
            NoteTakingScreen(
                onBack = { navController.popBackStack() },
                backHome = { navController.navigate("home")}
            )
        }

        composable("edit_note/{fileName}") { backStackEntry ->
            val initialFileName = backStackEntry.arguments?.getString("fileName")
            NoteTakingScreen(
                givenFileName = initialFileName,
                onBack = { navController.popBackStack() },
                backHome = { navController.navigate("home") }
            )
        }
    }
}
