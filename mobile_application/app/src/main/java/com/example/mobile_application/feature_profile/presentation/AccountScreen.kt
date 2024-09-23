package com.example.mobile_application.feature_profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@Destination
@Composable
fun AccountScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(true) {
        viewModel.getProfile()
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is UiEvents.NavigateEvent -> {
                    navigator.navigate(event.route) {
                        // Adjusting back stack
                        popUpTo(AccountScreenDestination.route) { inclusive = false }
                        popUpTo(HomeScreenDestination.route) { inclusive = false }
                        popUpTo(WishlistScreenDestination.route) { inclusive = false }
                        popUpTo(CartScreenDestination.route) { inclusive = false }
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                elevation = 1.dp,
                backgroundColor = Color.White,
                title = {
                    Text(
                        text = "My Profile",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) {
        AccountScreenContent(
            user = viewModel.profileState.value,
            onClickSignOut = { viewModel.logout() }
        )
    }
}

@Composable
private fun AccountScreenContent(
    user: User,
    onClickSignOut: () -> Unit,
) {
    LazyColumn {
        item {
            UserItem(
                user = user,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(4.dp)
            )
        }
        items(accountItems) { item ->
            AccountCard(item = item)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SignOutButton(onClick = onClickSignOut)
        }
    }
}

@Composable
private fun AccountCard(item: Account) {
    Card(
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(0.3.dp, GrayColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.title,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.content,
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = { /*TODO: Implement navigation or action*/ }) {
                Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null)
            }
        }
    }
}

@Composable
private fun SignOutButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(8.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            text = "Sign Out",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun UserItem(
    user: User,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 3.dp
    ) {
        Row {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data("https://firebasestorage.googleapis.com/v0/b/savingszetu.appspot.com/o/50293753.jpeg?alt=media&token=a7174053-5253-49ed-b885-08f428df0287")
                        .placeholder(R.drawable.ic_placeholder)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .clip(CircleShape)
                    .fillMaxHeight(),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.width(5.dp))
            UserDetails(user)
        }
    }
}

@Composable
private fun UserDetails(user: User) {
    Column(
        modifier = Modifier
            .weight(2f)
            .padding(5.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "${user.name?.firstname?.capitalize(Locale.getDefault())} ${user.name?.lastname?.capitalize(Locale.getDefault())}",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "@${user.username}",
            color = Color.Black,
            fontSize = 16.sp,
            maxLines = 3,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditProfileButton()
    }
}

@Composable
private fun EditProfileButton() {
    Button(
        modifier = Modifier.align(Alignment.End),
        onClick = { /* TODO: Implement edit profile action */ },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = YellowMain
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(3.dp),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            text = "Edit profile"
        )
    }
}

private val accountItems = listOf(
    Account("My Orders", "You have 10 completed orders"),
    Account("Shipping Address", "2 addresses have been set"),
    Account("My Reviews", "Reviewed 3 items"),
    Account("Settings", "Notifications, password, language")
)
