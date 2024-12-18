package com.noxis.notebookmviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.noxis.notebookmviapp.navigation.RootScreen
import com.noxis.notebookmviapp.navigation.Routes.ROUTE_DETAIL_ARG_NAME
import com.noxis.notebookmviapp.navigation.Routes.ROUTE_DETAIL_PATH
import com.noxis.notebookmviapp.ui.theme.NotebookMviAppTheme
import com.noxis.notes.domain.model.NoteUi
import com.noxis.notes.presentation.event.EventManager
import com.noxis.notes.presentation.screens.NoteDetailScreen
import com.noxis.notes.presentation.state.NoteDetailUiState
import com.noxis.notes.util.stringResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotebookMviAppTheme {
                val context = LocalContext.current

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                RootScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )

                // Observe the centralized event flow
                LaunchedEffect(EventManager) {
                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            EventManager.eventsFlow.collect { event ->
                                when (event) {
                                    is EventManager.AppEvent.ShowSnackbar -> {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(
                                                context.stringResource(event.message),
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }

                                     is EventManager.AppEvent.ExitScreen -> navController.navigateUp()
                                    is EventManager.AppEvent.NavigateToDetail -> {
                                        navController.navigate(
                                            ROUTE_DETAIL_PATH.replace(
                                                "{$ROUTE_DETAIL_ARG_NAME}",
                                                "${event.noteId}"
                                            )
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
