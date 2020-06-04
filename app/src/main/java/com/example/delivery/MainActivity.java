package com.example.delivery;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.delivery.Fragments.CartFragment;
import com.example.delivery.Fragments.HistoryFragment;
import com.example.delivery.Fragments.StoresFragment;
import com.example.delivery.Login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FrameLayout frameLayout;
    StoresFragment storesFragment;
    CartFragment cartFragment;
    HistoryFragment historyFragment;
    boolean isHomeSelected;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView=findViewById(R.id.main_nav);
        frameLayout=findViewById(R.id.main_frame);

        storesFragment=new StoresFragment();
        cartFragment=new CartFragment();
        historyFragment=new HistoryFragment();

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //change fragment by click on nav bar item
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navHome:setFragment(storesFragment);isHomeSelected=true; return true;
                    case R.id.navCart: setFragment(cartFragment);isHomeSelected=false;return true;
                    case R.id.navHistory:setFragment(historyFragment);isHomeSelected=false ;return true;
                    case R.id.navLogout: logout() ;return false;
                    default:return false;
                }

            }
        });

        setFragment(storesFragment);
        isHomeSelected=true;
    }

    private void logout() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(getString(R.string.sure_you_want_log_out));
        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sharedPreferences.edit().putBoolean("loggedIn",false).apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

            }
        });
        dialog.setNegativeButton(getString(R.string.cancel),null);
        dialog.show();

    }


    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack("store", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        if (!isHomeSelected){
            setFragment(storesFragment);
            navigationView.setSelectedItemId(R.id.navHome);
            isHomeSelected=true;
        }else
            super.onBackPressed();
    }
}
