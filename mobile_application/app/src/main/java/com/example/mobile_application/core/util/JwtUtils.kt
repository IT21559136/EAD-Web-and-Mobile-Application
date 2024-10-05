package com.example.mobile_application.core.util

import android.util.Base64
import org.json.JSONObject
import timber.log.Timber

object JwtUtils {

    fun getUserIdFromToken(token: String): String? {
        return try {
            // JWT tokens are divided into three parts: header, payload, and signature
            val parts = token.split(".")
            if (parts.size == 3) {
                // Decode the payload (second part of the JWT)
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))

                // Parse the payload JSON
                val jsonObject = JSONObject(payload)

                // Assuming the user ID is stored under the key "user_id"
                jsonObject.getString("UserId")
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e("Error decoding JWT: ${e.localizedMessage}")
            null
        }
    }
}
