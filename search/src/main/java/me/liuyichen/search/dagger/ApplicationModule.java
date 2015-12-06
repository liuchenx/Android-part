package me.liuyichen.search.dagger;

import android.util.Base64;

import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.liuyichen.search.network.Bing;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by liuchen on 15/12/5.
 * and ...
 */
@Module
public class ApplicationModule {


    /*
    *
       private String API_KEY = "OnU2Mxti1LltKV0xiBXh0wvlN3DbXwXngWfcHZ+1tME";
        String auth = API_KEY + ":" + API_KEY;
        String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
 httpget.addHeader("Authorization", "Basic " + encodedAuth);
    * */


    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {

        final String API_KEY = "yNJ/QG2zLDnRFhVGoCox3jsJDcZIdjMQXwhgH4pOgac";
        final String auth = API_KEY + ":" + API_KEY;
        final String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);

        OkHttpClient okHttpClient = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(interceptor);
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Basic " + encodedAuth).build();
                return chain.proceed(request);
            }
        });
        return okHttpClient;
    }

    @Singleton
    @Provides
    Bing provideBing(OkHttpClient client) {

        Gson gson = new Gson();
                //new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Bing.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(Bing.class);
    }
}
