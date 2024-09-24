package com.example.mobile_application.feature_products.data.repository

import com.example.mobile_application.core.util.Resource
import com.example.mobile_application.feature_products.data.remote.ProductsApiService
import com.example.mobile_application.feature_products.data.remote.dto.ProductDto
import com.example.mobile_application.feature_products.data.remote.mappers.toDomain
import com.example.mobile_application.feature_products.domain.model.Product
import com.example.mobile_application.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ProductsRepositoryImpl(private val productsApiService: ProductsApiService) :
    ProductsRepository {
    override suspend fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val response = productsApiService.getProducts()
            emit(Resource.Success(response.map { it.toDomain() }))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Oops, something went wrong!"))
        }
    }

    override suspend fun getProductCategories(): List<String> {
        return productsApiService.getProductCategories()
    }

    override suspend fun getProductById(productId: Int): Product {
        return productsApiService.getProductById(productId) // Assuming this API method exists
    }
}