package com.ahmedkhaled.lovindubai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmedkhaled.lovindubai.presentation.screens.home.HomeScreen
import com.ahmedkhaled.lovindubai.presentation.screens.videodetails.VideoDetailsScreen
import com.ahmedkhaled.lovindubai.utils.openInYouTube

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onVideoClick = { video ->
                    navController.navigate(Routes.videoDetails(video.id))
                }
            )
        }
        composable(
            route = Routes.VIDEO_DETAILS,
            arguments = listOf(navArgument(Routes.VIDEO_ID_ARG) {
                type = NavType.StringType
            })
        ) {
            VideoDetailsScreen(
                onBack = { navController.popBackStack() },
                onPlay = { video -> openInYouTube(context, video) }
            )
        }
    }
}