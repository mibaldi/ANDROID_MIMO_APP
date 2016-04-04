package com.android.mikelpablo.otakucook.MyApiClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mikelbalducieldiaz on 4/4/16.
 */
public class MyApiClient {
    public static final String API_BASE_URL = "http://otakucook.herokuapp.com";

    private static OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(30,TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
