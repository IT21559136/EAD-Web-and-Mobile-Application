package com.example.mobile_application.feature_products.presentation.product_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mobile_application.R
import com.example.mobile_application.core.presentation.ui.theme.GrayColor
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor
import com.example.mobile_application.core.presentation.ui.theme.YellowMain
import com.example.mobile_application.feature_products.domain.model.Product
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.StepSize
import com.example.mobile_application.feature_wish_list.domain.model.Wishlist
import com.example.mobile_application.feature_wish_list.presentation.WishlistViewModel
import androidx.navigation.NavController
import com.example.mobile_application.feature_wish_list.data.mapper.toWishlistRating
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle


@ExperimentalMaterial3Api
@Composable
fun ProductDetailsScreen(
    product: Product,
    navController: NavController,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    val inWishlist = viewModel.inWishlist(product.id).observeAsState().value != null

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = product.title)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_chevron_left), contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (inWishlist) {
                            viewModel.deleteFromWishlist(
                                Wishlist(
                                    image = product.image,
                                    title = product.title,
                                    id = product.id,
                                    liked = true,
                                    price = product.price,
                                    description = product.description,
                                    category = product.category,
                                    rating = product.rating.toWishlistRating()
                                )
                            )
                        } else {
                            viewModel.insertFavorite(
                                Wishlist(
                                    image = product.image,
                                    title = product.title,
                                    id = product.id,
                                    liked = true,
                                    price = product.price,
                                    description = product.description,
                                    category = product.category,
                                    rating = product.rating.toWishlistRating()
                                )
                            )
                        }
                    }) {
                        Icon(
                            painter = if (inWishlist) painterResource(id = R.drawable.ic_heart_fill) else painterResource(id = R.drawable.ic_heart),
                            tint = if (inWishlist) YellowMain else GrayColor,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {padding ->
        DetailsScreenContent(product = product, modifier = Modifier.fillMaxSize().padding(padding))
    }
}

@Composable
fun DetailsScreenContent(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column {
        Box(modifier = modifier.weight(1f), contentAlignment = Alignment.Center) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = product.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_placeholder)
                        }).build()
                ),
                contentDescription = null,
                modifier = modifier.fillMaxWidth().height(250.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = modifier.fillMaxWidth().weight(2f),
            elevation = CardDefaults.elevatedCardElevation(0.dp),
            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor =MainWhiteColor
            )
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = product.title,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.height(12.dp))

                val rating: Float by remember { mutableStateOf(product.rating.rate.toFloat()) }

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        value = rating,
                        config = RatingBarConfig()
                            .activeColor(YellowMain)
                            .inactiveColor(GrayColor)
                            .stepSize(StepSize.HALF)
                            .numStars(5)
                            .isIndicator(true)
                            .size(16.dp)
                            .padding(3.dp)
                            .style(RatingBarStyle.HighLighted),
                        onValueChange = {},
                        onRatingChanged = {}
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${product.rating.count})",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$${product.price}",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Handle add to cart */ },
                    colors = ButtonDefaults.buttonColors(containerColor = YellowMain),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_to_cart),
                        color = Color.Black
                    )
                }
            }
        }
    }
}
