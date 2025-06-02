package com.example.pitstopapp.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pitstopapp.R
import com.example.pitstopapp.ui.PitStopRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val title = when {
        backStackEntry?.destination?.route?.startsWith(PitStopRoute.Home.toString()) == true -> stringResource(R.string.home_screen_name)
        backStackEntry?.destination?.route?.startsWith(PitStopRoute.Profile.toString()) == true -> stringResource(R.string.profile_screen_name)
        backStackEntry?.destination?.route?.startsWith(PitStopRoute.Settings.toString()) == true -> stringResource(R.string.settings_screen_name)
        backStackEntry?.destination?.route?.startsWith(PitStopRoute.Details.toString()) == true -> stringResource(R.string.details_screen_name)
        backStackEntry?.destination?.route?.startsWith(PitStopRoute.AddLapTime.toString()) == true -> stringResource(R.string.add_time_button)
        else -> "Unknown Screen"
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        navigationIcon = {
            if (title == stringResource(R.string.profile_screen_name) ||
                title == stringResource(R.string.settings_screen_name) ||
                title == stringResource(R.string.details_screen_name) ||
                title == stringResource(R.string.add_time_button)) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            if (title == stringResource(R.string.home_screen_name)) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.StarOutline, contentDescription = "Favourite", Modifier.size(30.dp))
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}