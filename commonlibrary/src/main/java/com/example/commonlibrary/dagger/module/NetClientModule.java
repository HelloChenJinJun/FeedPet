package com.example.commonlibrary.dagger.module;

import android.app.Application;
import android.support.annotation.Nullable;

import com.example.commonlibrary.interceptor.LogInterceptor;
import com.example.commonlibrary.dagger.OkHttpGlobalHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by COOTEK on 2017/7/28.
 */
@Module
public class NetClientModule {


    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(Application application, @Nullable OkHttpConfig okHttpConfig, OkHttpClient.Builder okHttpBuilder
            , @Nullable final OkHttpGlobalHandler handler, @Nullable Interceptor logInterceptor) {
        okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS);
        if (logInterceptor != null) {
            okHttpBuilder.addNetworkInterceptor(logInterceptor);
        }
        if (handler != null) {
            okHttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onRequestBefore(chain, chain.request()));
                }
            });
        }
        return okHttpBuilder.build();
    }

    @Provides
    @Singleton
    public OkHttpClient.Builder provideOkHttpClientBuilder(Application application, OkHttpConfig okHttpConfig) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS);
        if (okHttpConfig != null) {
            okHttpConfig.config(application, builder);
        }
        return builder;
    }


    @Singleton
    @Provides
    public Interceptor provideLogInterceptor(LogInterceptor logInterceptor) {
        return logInterceptor;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Application application, @Nullable RetrofitConfig retrofitConfig, OkHttpClient okHttpClient, HttpUrl
            httpUrl, @Nullable Gson gson) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(httpUrl).client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        if (retrofitConfig != null) {
            retrofitConfig.config(application, builder);
        }
        return builder.build();
    }


    public interface OkHttpConfig {
        public void config(Application application, OkHttpClient.Builder builder);
    }


    public interface RetrofitConfig {
        public void config(Application application, Retrofit.Builder builder);
    }


    public interface GsonConfig {
        public void config(Application application, GsonBuilder gsonBuilder);
    }


}
