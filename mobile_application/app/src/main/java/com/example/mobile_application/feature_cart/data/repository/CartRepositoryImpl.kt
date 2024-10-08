package com.example.mobile_application.feature_cart.data.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_cart.data.remote.CartApiService
import com.example.mobile_application.feature_cart.data.remote.mappers.toDomain
import com.example.mobile_application.feature_cart.data.remote.request.AddCartItemRequest
import com.example.mobile_application.feature_cart.domain.model.CartProduct
import com.example.mobile_application.feature_cart.domain.repository.CartRepository
import com.example.mobile_application.feature_products.data.remote.mappers.toDomain
import com.example.mobile_application.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class CartRepositoryImpl(
    private val cartApiService: CartApiService,  private val authPreferences: AuthPreferences
) : CartRepository {
    private val accessToken: String by lazy {
        runBlocking { authPreferences.getAccessToken.first() }
    }
    private val bearerToken = "Bearer $accessToken"
    override suspend fun getAllCartItems(): Flow<Resource<List<CartProduct>>> = flow {
        Timber.d("Get all cart items called")
        emit(Resource.Loading())
        try {
            // Call the API to get the user cart
            val response = cartApiService.getUserCart(bearerToken)

            // Map the response DTO items to the domain model and return the distinct list of Product
            val cartItems = response.items.map { cartProductDto ->
                CartProduct(
                    product = cartProductDto.toDomain(),  // Convert CartProductDto to Product
                    selectedQuantity = cartProductDto.quantity // Store the selected quantity
                )
            }
            emit(Resource.Success(cartItems.distinctBy { it.product.productName }))
        } catch (e: IOException) {
            Timber.e("Could not reach the server: ${e.localizedMessage}")
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            Timber.e("Server error:${e.localizedMessage}")
            emit(Resource.Error(message = "Server error: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error(message = "An unexpected error occurred: ${e.localizedMessage}"))
        }
    }

    override suspend fun addCartItem(cartProduct: CartProduct): Flow<Resource<Unit>> = flow {
        Timber.d("Add cart item called for product: ${cartProduct.product.productName}")
        emit(Resource.Loading())

        try {
            // Create the request DTO
            val addCartItemRequest = AddCartItemRequest(
                productId = cartProduct.product.id,
                quantity = cartProduct.selectedQuantity
            )

            // Make the API call to add the cart item
            cartApiService.addOrUpdateCartItem(bearerToken, addCartItemRequest)

            emit(Resource.Success(Unit)) // Emit success
            Timber.d("Cart item added successfully")
        } catch (e: IOException) {
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Server error: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error(message = "An unexpected error occurred: ${e.localizedMessage}"))
        }
    }

    override suspend fun deleteCartItems(cartItems: List<CartProduct>) {
        Timber.d("Delete cart items called")
        cartItems.forEach { cartProduct ->
              try {
                  val response = cartApiService.removeCartItem(bearerToken, cartProduct.product.id)

                  // Check if the response is null
                  if (response == null) {
                      Timber.e("Error: API returned a null response for productId ${cartProduct.product.id}")
                  } else {
                      Timber.d("Cart item with productId ${ cartProduct.product.id} deleted successfully")
                  }
              } catch (e: IOException) {
                  Timber.e("IOException: Could not reach the server, please check your internet connection!")
              } catch (e: HttpException) {
                  Timber.e("HttpException: Oops, something went wrong!")
              }
        }
    }
}