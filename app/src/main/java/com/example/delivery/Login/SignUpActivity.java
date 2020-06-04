package com.example.delivery.Login;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delivery.Model.User;
import com.example.delivery.R;
import com.example.delivery.retrofit.RetrofitClient;

public class SignUpActivity extends AppCompatActivity {


    private View mProgressView;
    private View mSignUpFormView;
    private TextView tvLoad;

    EditText etFirstName,etLastName,etEmail,etPassword,etPhone,etAddress;
    RadioButton rbMale,rbFemale;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
    }

    private void initView(){
        mSignUpFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etPhone=findViewById(R.id.etPhone);
        etAddress=findViewById(R.id.etAddress);
        rbMale=findViewById(R.id.rbMale);
        rbFemale=findViewById(R.id.rbFemale);

        btnSignUp=findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void signUp() {

        if(checkValidation()){
            showProgress(true);

            User user=new User();
            user.setFirstName(etFirstName.getText().toString().trim());
            user.setLastName(etLastName.getText().toString().trim());
            user.setEmail(etEmail.getText().toString().trim());
            user.setPassword(etPassword.getText().toString().trim());
            user.setC_password(user.getPassword());
            user.setPhone(etPhone.getText().toString().trim());
            user.setAddress(etAddress.getText().toString().trim());
            user.setGender(rbMale.isChecked()?"Male":"Female");

            RetrofitClient.getApi().register(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        showProgress(false);
                        Toast.makeText(SignUpActivity.this, getString(R.string.sign_up_success_please_login), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{
                        showProgress(false);
                        Toast.makeText(SignUpActivity.this, getString(R.string.email_already_used), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showProgress(false);
                    Toast.makeText(SignUpActivity.this, getString(R.string.sign_up_failed_please_try_again), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(this, getString(R.string.please_enter_all_field), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkValidation(){
        if(etFirstName.getText().toString().isEmpty())
            return false;
        if(etLastName.getText().toString().isEmpty())
            return false;
        if(etEmail.getText().toString().isEmpty())
            return false;
        if(etPassword.getText().toString().isEmpty())
            return false;
        if(etPhone.getText().toString().isEmpty())
            return false;
        if(etAddress.getText().toString().isEmpty())
            return false;

        return true;
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

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
