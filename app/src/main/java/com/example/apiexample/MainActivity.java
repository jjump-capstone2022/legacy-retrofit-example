package com.example.apiexample;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.apiexample.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import android.view.Menu;
import android.view.MenuItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

class ReqLoginData {
    String email;
    public ReqLoginData( String email ) {
        this.email = email;
    }
}

class ResLoginData {
    String message;

    @Override
    public String toString() {
        return "[ResLoginData] message=" + message;
    }
}

interface ApiInterface {
    @POST("login")
    Call<ResLoginData> requestLogin(@Body ReqLoginData req);
}

class HttpClient {

    private static Retrofit retrofit;

    // Http 통신을 위한 Retrofit 객체반환
    public static Retrofit getRetrofit() {
        if( retrofit == null )
        {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl( "http://10.0.2.2:3000/" );
            builder.addConverterFactory( GsonConverterFactory.create() );  // 받아오는 Json 구조의 데이터를 객체 형태로 변환

            retrofit = builder.build();
        }

        return retrofit;
    }
}



public class MainActivity extends AppCompatActivity {
    // POST 통신요청
    public void requestPost() {
        ReqLoginData reqLoginData = new ReqLoginData( "abc@abc.com");
        Call<ResLoginData> call = api.requestLogin( reqLoginData );

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue( new Callback<ResLoginData>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<ResLoginData> call, Response<ResLoginData> response) {
                Log.i("my tag", response.body().toString());
            }

            @Override
            public void onFailure(Call<ResLoginData> call, Throwable t) {
                Log.i("my tag", "fail!!!!!!!!!!!!!!!!");
                Log.i("erre", t.toString());
            }
        } );
    }


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // api call
                api = HttpClient.getRetrofit().create( ApiInterface.class );
                requestPost();
            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}