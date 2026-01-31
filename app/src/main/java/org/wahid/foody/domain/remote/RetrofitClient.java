package org.wahid.foody.domain.remote;

import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RetrofitClient {
    public static String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static volatile Retrofit INSTANCE;
    private static OkHttpClient okHttpClient;
    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    public static synchronized Retrofit getInstance(){

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        if (INSTANCE == null)INSTANCE =
                new Retrofit
                        .Builder()
                        .client(okHttpClient)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                        .build();

        return INSTANCE;
    }

    public static synchronized MealsApiService getRetrofitServiceInstance(){
        return getInstance().create(MealsApiService.class);
    }
}