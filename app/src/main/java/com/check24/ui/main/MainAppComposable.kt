package com.check24.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.check24.ui.common.AppTabBar
import com.check24.ui.navigation.MainNavGraph
import com.check24.ui.navigation.Screen
import com.check24.ui.navigation.Screen.Companion.isMainDestination
import com.check24.ui.theme.CodeChallengeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppComposable() {
    CodeChallengeTheme() {
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = Screen.getScreenByRout(
            route = navBackStackEntry?.destination?.route ?: Screen.getMainScreen().route
        )

        Scaffold(modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                AppTabBar(title_id = currentScreen.title_id,
                    isMainDestination = currentScreen.isMainDestination(),
                    backArrowOnClick = { navController.navigateUp() })
            },
            content = { innerPadding ->
                MainNavGraph(
                    modifier = Modifier.padding(innerPadding), navController = navController
                )
            })
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CodeChallengeTheme() {
        MainAppComposable()
    }
}