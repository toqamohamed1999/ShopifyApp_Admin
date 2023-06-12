package eg.gov.iti.jets.shopifyapp_user.base.remote

import com.google.gson.GsonBuilder
import eg.gov.iti.jets.shopifyapp_admin.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AppRetrofit {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit: Retrofit =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}