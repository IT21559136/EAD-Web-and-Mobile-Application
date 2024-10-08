package com.example.mobile_application.feature_auth.presentation.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mobile_application.feature_auth.util.Constants.SPLASH_SCREEN_DURATION
import com.example.mobile_application.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val scale = remember { Animatable(0f) }
        val overshootInterpolator = remember { OvershootInterpolator(1.5f) }

        LaunchedEffect(key1 = true) {
            withContext(Dispatchers.Main) {
                scale.animateTo(
                    targetValue = 1.0f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = {
                            overshootInterpolator.getInterpolation(it)
                        }
                    )
                )

                delay(SPLASH_SCREEN_DURATION)

                val eventState = viewModel.eventState.value

                // Navigate based on eventState
                if (eventState) {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    navController.navigate("auth_dashboard") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }

        Image(
            modifier = Modifier.padding(24.dp),
            painter = painterResource(id = R.drawable.ic_joomia_logo),
            contentDescription = null
        )
    }
}
