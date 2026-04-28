package com.example.formulario.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.formulario.ui.screens.FormScreen
import com.example.formulario.ui.screens.RequestsScreen

sealed class Screen(val route: String) {
    object Form : Screen("form")
    object Requests : Screen("requests")
}

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Form.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Form.route) {
            FormScreen(
                onNavigateToRequests = {
                    navController.navigate(Screen.Requests.route)
                }
            )
        }
        
        composable(Screen.Requests.route) {
            RequestsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
