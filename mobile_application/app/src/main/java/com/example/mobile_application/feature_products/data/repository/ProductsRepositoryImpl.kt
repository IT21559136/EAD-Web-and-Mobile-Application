package com.example.mobile_application.feature_products.data.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_auth.data.local.AuthPreferences
import com.example.mobile_application.feature_products.data.remote.ProductsApiService
import com.example.mobile_application.feature_products.data.remote.mappers.toDomain
import com.example.mobile_application.feature_products.domain.model.Category
import com.example.mobile_application.feature_products.domain.model.Product
import com.example.mobile_application.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class ProductsRepositoryImpl(private val productsApiService: ProductsApiService,  private val authPreferences: AuthPreferences) :
    ProductsRepository {

    private val accessToken: String by lazy {
        runBlocking { authPreferences.getAccessToken.first() }
    }

    private val bearerToken = "Bearer $accessToken"

    override suspend fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val response = productsApiService.getProducts(bearerToken)
            emit(Resource.Success(response.map { it.toDomain() }))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Oops, something went wrong!"))
        }
    }

    override suspend fun getProductCategories(): List<Category> {
        return try {
            val categories = productsApiService.getProductCategories(bearerToken)
            // Log the list of categories
            Timber.d("Fetched Categories: $categories")

            categories
        } catch (e: IOException) {
            // Log the IO error
            Timber.e("Network error fetching categories: ${e.localizedMessage}")
            throw IOException("Could not reach the server, please check your internet connection!")
        } catch (e: HttpException) {
            // Log the HTTP error with status code
            Timber.e("HTTP error fetching categories: ${e.message()} (Code: ${e.code()})")
            throw HttpException(e.response()) // You can choose to rethrow it or handle it differently
        }
    }


    override suspend fun getProductById(productId: String): Product {
        return try {
            Timber.d("Fetched Token: $bearerToken")
            val product = productsApiService.getProductById(bearerToken,productId)
            // Log the list of categories
            Timber.d("Fetched Product: $product")
            product
        } catch (e: IOException) {
            throw IOException("Could not reach the server, please check your internet connection!")
        }
    }
}