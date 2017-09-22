package br.com.rf.themovieapp.data.source.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRestClient {

    private static MoviesRestClient sRestClient;

    private final MoviesRemoteServer mRestApi;

    private MoviesRestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(logging);


        client.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesRemoteServer.BASE_API)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestApi = retrofit.create(MoviesRemoteServer.class);
    }


    public static MoviesRestClient getInstance() {
        if (sRestClient == null) {
            sRestClient = new MoviesRestClient();
        }
        return sRestClient;
    }

    public MoviesRemoteServer getRestApi() {
        return mRestApi;
    }
}
