package com.example.mobile_application.feature_products.presentation.product_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mobile_application.R
import com.example.mobile_application.core.presentation.ui.theme.MainWhiteColor
import com.example.mobile_application.core.presentation.ui.theme.YellowMain
import com.example.mobile_application.feature_products.domain.model.Product
import androidx.navigation.NavController
import com.example.mobile_application.feature_cart.presentation.cart.CartViewModel


@ExperimentalMaterial3Api
@Composable
fun ProductDetailsScreen(
    product: Product,
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel(),
) {

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) {padding->
        DetailsScreenContent(
            product = product,
            modifier = Modifier.fillMaxSize().padding(start = 0.dp, top = 16.dp, end = 0.dp, bottom = 0.dp),
            onAddToCartClick = {
                cartViewModel.addCartItem(product, 1) // Add the product to the cart with default quantity 1
            }
        )
    }
}

@Composable
fun DetailsScreenContent(
    product: Product,
    modifier: Modifier = Modifier,
    onAddToCartClick: () -> Unit
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
                modifier = modifier.fillMaxWidth().height(250.dp).align(Alignment.Center),
                contentScale = ContentScale.Inside
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
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = product.productName,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                   // val rating: Float by remember { mutableStateOf(product.rating.rate.toFloat()) }

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        RatingBar(
//                            value = rating,
//                            config = RatingBarConfig()
//                                .activeColor(YellowMain)
//                                .inactiveColor(GrayColor)
//                                .stepSize(StepSize.HALF)
//                                .numStars(5)
//                                .isIndicator(true)
//                                .size(16.dp)
//                                .padding(3.dp)
//                                .style(RatingBarStyle.HighLighted),
//                            onValueChange = {},
//                            onRatingChanged = {}
//                        )
                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "(${product.rating.count})",
//                            color = Color.Black,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Light
//                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$${product.description}",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$${product.price}",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {onAddToCartClick() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = YellowMain
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_to_cart),
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth().padding(5.dp),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
