package com.example.cacaotesouro.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cacaotesouro.data.clues
import com.example.cacaotesouro.screens.ClueScreen
import com.example.cacaotesouro.screens.HomeScreen
import com.example.cacaotesouro.screens.TreasureScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Clue : Screen("clue/{clueIndex}") {
        fun createRoute(index: Int) = "clue/$index"
    }
    data object Treasure : Screen("treasure/{elapsedSeconds}") {
        fun createRoute(seconds: Int) = "treasure/$seconds"
    }
}

private const val ANIM_DURATION = 350

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var startTime by remember { mutableLongStateOf(0L) }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(ANIM_DURATION)
            ) + fadeIn(animationSpec = tween(ANIM_DURATION))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(ANIM_DURATION)
            ) + fadeOut(animationSpec = tween(ANIM_DURATION))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(ANIM_DURATION)
            ) + fadeIn(animationSpec = tween(ANIM_DURATION))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(ANIM_DURATION)
            ) + fadeOut(animationSpec = tween(ANIM_DURATION))
        }
    ) {
        // ── Tela Inicial ──────────────────────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                onStartGame = {
                    startTime = System.currentTimeMillis()
                    navController.navigate(Screen.Clue.createRoute(0))
                }
            )
        }

        // ── Telas de Pista ────────────────────────────────────────────────────
        composable(
            route = Screen.Clue.route,
            arguments = listOf(navArgument("clueIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val clueIndex = backStackEntry.arguments?.getInt("clueIndex") ?: 0

            ClueScreen(
                clueIndex = clueIndex,
                totalClues = clues.size,
                onNextClue = { nextIndex ->
                    if (nextIndex >= clues.size) {
                        // Última pista: calcula tempo e navega para o tesouro
                        val elapsed = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        navController.navigate(Screen.Treasure.createRoute(elapsed)) {
                            // Remove todas as pistas da pilha — botão Voltar
                            // no dispositivo não retorna para as pistas
                            popUpTo(Screen.Home.route)
                        }
                    } else {
                        navController.navigate(Screen.Clue.createRoute(nextIndex))
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Tela do Tesouro ───────────────────────────────────────────────────
        composable(
            route = Screen.Treasure.route,
            arguments = listOf(navArgument("elapsedSeconds") { type = NavType.IntType })
        ) { backStackEntry ->
            val elapsed = backStackEntry.arguments?.getInt("elapsedSeconds") ?: 0

            TreasureScreen(
                elapsedSeconds = elapsed,
                onRestart = {
                    // Limpa toda a pilha e volta ao Home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
