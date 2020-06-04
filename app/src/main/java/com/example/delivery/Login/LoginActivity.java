package com.example.delivery.Login;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delivery.MainActivity;
import com.example.delivery.Model.User;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;

public class LoginActivity extends AppCompatActivity {


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText etEmail,etPassword;
    TextView tvSignUp;
    Button btnLogin;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        initView();

        if(preferences.getBoolean("loggedIn",false)){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    private void initView(){
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        tvSignUp=findViewById(R.id.tvSignUp);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void login(){
        if(etEmail.getText().toString().trim().isEmpty()||etPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getString(R.string.please_enter_all_field), Toast.LENGTH_SHORT).show();
            return;
        }
        showProgress(true);
        User user=new User();
        user.setEmail(etEmail.getText().toString().trim());
        user.setPassword(etPassword.getText().toString().trim());

        RetrofitClient.getApi().login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user=response.body();
                    preferences.edit()
                            .putLong("userId",user.getId())
                            .putString("token",user.getToken())
                            .putBoolean("loggedIn",true)
                            .apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.email_or_password_wrong), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, getString(R.string.failure), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signUp(){

       startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
