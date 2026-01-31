package org.wahid.foody.domain.remote.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.wahid.foody.domain.remote.meal_service.MealDto;
import org.wahid.foody.utils.RemoteMealResponseTypeAdapter;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RetrofitClient {
    public static String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static volatile Retrofit INSTANCE;
    private static OkHttpClient okHttpClient;
    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(MealDto.class, new RemoteMealResponseTypeAdapter())
            .serializeNulls()
            .create();
    public static synchronized Retrofit getInstance(){

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();



        if (INSTANCE == null)INSTANCE =
                new Retrofit
                        .Builder()
                        .client(okHttpClient)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

        return INSTANCE;
    }

    public static synchronized<T> T getRetrofitServiceInstance(Class<T> cls){
        return getInstance().create(cls);
    }
}