package com.example.mobile_application.feature_products.presentation.home

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mobile_application.R
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor
import com.example.mobile_application.core.presentation.ui.theme.YellowMain
import com.example.mobile_application.core.util.LoadingAnimation
import com.example.mobile_application.core.util.UiEvents
import com.example.mobile_application.feature_products.domain.model.Product
import androidx.navigation.NavHostController
import com.example.mobile_application.core.presentation.ui.theme.DarkBlue
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var filtersExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val productsState = viewModel.productsState.value
    val categories = viewModel.categoriesState.value

    Scaffold(
        topBar = {
            MyTopAppBar(
                currentSearchText = viewModel.searchTerm.value,
                onSearchTextChange = {
                    viewModel.setSearchTerm(it)
                },
                onSearch = {
                    keyboardController?.hide()
                    viewModel.getProducts(
                        searchTerm = viewModel.searchTerm.value
                    )
                },
                onToggleExpand = {
                    filtersExpanded = !filtersExpanded
                },
            )
        },
    ) { padding ->
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = true) {
            viewModel.eventFlow.collectLatest { event -> // Specify the type here
                when (event) {
                    is UiEvents.SnackbarEvent -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                        )
                    }
                    else -> {}
                }
            }
        }

        DropdownMenu(
            expanded = filtersExpanded,
            offset = DpOffset(x = 200.dp, y = -600.dp),
            onDismissRequest = {
                filtersExpanded = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    Toast.makeText(context, "Clothes", Toast.LENGTH_SHORT).show()
                    filtersExpanded = false // Close the menu after selection
                },
                text = { Text("Clothes") }
            )

            DropdownMenuItem(
                onClick = {
                    Toast.makeText(context, "Shoes", Toast.LENGTH_SHORT).show()
                    filtersExpanded = false // Close the menu after selection
                },
                text = { Text("Shoes") }
            )

            DropdownMenuItem(
                onClick = {
                    Toast.makeText(context, "Electronics", Toast.LENGTH_SHORT).show()
                    filtersExpanded = false // Close the menu after selection
                },
                text = { Text("Electronics") }
            )
        }

        HomeScreenContent(
            modifier = Modifier.padding(padding),
            categories = categories,
            productsState = productsState,
            navController = navController,
            bannerImageUrl = viewModel.bannerImageState.value,
            selectedCategory = viewModel.selectedCategory.value,
            onSelectCategory = { category ->
                viewModel.setCategory(category)
                viewModel.getProducts(viewModel.selectedCategory.value)
            }
        )
    }
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    categories: List<String>,
    productsState: ProductsState,
    navController: NavHostController,
    bannerImageUrl: String,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    elevation = CardDefaults.elevatedCardElevation(0.dp),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = bannerImageUrl)
                                .apply(block = fun ImageRequest.Builder.() {
                                    placeholder(R.drawable.ic_placeholder)
                                    crossfade(true)
                                }).build()
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Black Friday Banner"
                    )
                }
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item(span = { GridItemSpan(2) }) {
                Categories(
                    categories = categories,
                    onSelectCategory = onSelectCategory,
                    selectedCategory = selectedCategory
                )
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(productsState.products) { product ->
                ProductItem(
                    product = product,
                    navController = navController,
                    modifier = Modifier.width(150.dp)
                )
            }
        }

        if (productsState.isLoading) {
            LoadingAnimation(
                modifier = Modifier.align(Alignment.Center),
                circleSize = 16.dp,
            )
        }

        if (productsState.error != null) Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            text = productsState.error,
            color = Color.Red
        )
    }
}


@Composable
private fun ProductItem(
    product: Product,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable {
                // Use navController to navigate to ProductDetailsScreen
                navController.navigate("product_details/${product.id}") // Assuming you use product ID in route
            },
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = product.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.ic_placeholder)
                            crossfade(true)
                        }).build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.category,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    tint = YellowMain
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "${product.rating.rate} (${product.rating.count})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${product.price}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.End),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainWhiteColor,
                )
            ) {
                Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    currentSearchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onToggleExpand: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = "https://firebasestorage.googleapis.com/v0/b/mealtime-7a501.appspot.com/o/tinywow_Joomia%20Black%20Friday_16608968%20(1).png?alt=media&token=8b874def-e543-482e-80f7-c8cbe9d9f206")
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                            }).build()
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(35.dp),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Hi, John", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_allert),
                contentDescription = null,
                tint = DarkBlue
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = currentSearchText,
                onValueChange = {
                    onSearchTextChange(it)
                },
                placeholder = {
                    Text(
                        text = "Search",
                    )
                },

                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .background(MainWhiteColor, shape = RoundedCornerShape(8.dp))
                    .clickable {

                    },
                shape = RoundedCornerShape(size = 8.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = MainWhiteColor,
                    focusedContainerColor = MainWhiteColor,
                    unfocusedContainerColor = MainWhiteColor,
                    disabledContainerColor = MainWhiteColor,
                    focusedIndicatorColor = MainWhiteColor,
                    unfocusedIndicatorColor = MainWhiteColor,
                    disabledIndicatorColor = MainWhiteColor,
                ),
                textStyle = TextStyle(color = Color.Black),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = DarkBlue
                    )
                }
            )

            IconButton(onClick = onToggleExpand) {
                Icon(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                size = 8.dp
                            )
                        )
                        .background(
                            MainWhiteColor
                        )
                        .padding(
                            start = 4.dp,
                            end = 4.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = null,
                    tint = DarkBlue
                )
            }
        }
    }
}


@Composable
fun Categories(
    categories: List<String>,
    onSelectCategory: (String) -> Unit,
    selectedCategory: String,
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(categories) { category ->
            Text(
                text = category,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            size = 8.dp,
                        ),
                    )
                    .clickable {
                        onSelectCategory(category)
                    }
                    .background(
                        if (category == selectedCategory) {
                            YellowMain
                        } else {
                            MainWhiteColor
                        }
                    )
                    .padding(
                        10.dp
                    )
            )
        }
    }
}