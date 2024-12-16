package com.noxis.notebookmviapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.noxis.notebookmviapp.navigation.Routes.ROUTE_DETAIL_ARG_NAME
import com.noxis.notebookmviapp.navigation.Routes.ROUTE_DETAIL_PATH
import com.noxis.notebookmviapp.navigation.Routes.ROUTE_HOME
import com.noxis.notes.presentation.components.MainTopAppBar
import com.noxis.notes.presentation.screens.NoteDetailScreen
import com.noxis.notes.presentation.screens.NotesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val showBackButton by remember(currentBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            MainTopAppBar(
                navController = navController,
                showBackButton = showBackButton,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (!showBackButton) {
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = { navController.navigate(ROUTE_DETAIL_PATH) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_HOME,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(route = ROUTE_HOME) {
                NotesScreen()
            }
            composable(
                route = ROUTE_DETAIL_PATH,
                arguments = listOf(
                    navArgument(ROUTE_DETAIL_ARG_NAME) { nullable = true },
                )
            ) {
                NoteDetailScreen()
            }
        }
    }
}