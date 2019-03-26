package com.cas.jiamin.mogic.AccountsettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cas.jiamin.mogic.Home.HomeActivity;
import com.cas.jiamin.mogic.Profile.ProfileActivity;
import com.cas.jiamin.mogic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * AccountsettingActivity.class
 *
 * This is a class based on "activity_account_settings.xml" which establishes a layout for users
 * to choose the account setting options based on their current accounts' status.
 */
public class AccountsettingActivity extends AppCompatActivity {
    private static final String TAG = "AccountsettingActivity";
    FirebaseAuth mAuth;
    FirebaseUser user;

    /**
     * The onCreate method, state what should be build in the corresponding layout when user
     * just entering this layout
     *
     * @param savedInstanceState The current state of the application
     */
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Log.d(TAG, "onCreate Started");
        setupAcset();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    /**
     * The method which sets up the onclicker listeners of the one image(acsettab) and three
     * buttons(acsetreg, acsetlog, acsetsingout) on the layout page. User is able to reach different
     * activities by clicking.
     */
    private void setupAcset() {
        Log.d(TAG, "setupSetting: initializing AccountSetting");
        ImageView acsettab = (ImageView) findViewById(R.id.acsetbk);
        acsettab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: nav to profile");
                Intent intent = new Intent(AccountsettingActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button reg = (Button) findViewById(R.id.acsetreg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    Toast.makeText(AccountsettingActivity.this, "Already login!", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d(TAG, "OnClick: nav to register");
                    Intent intent = new Intent(AccountsettingActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button login = (Button) findViewById(R.id.acsetlog);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    Toast.makeText(AccountsettingActivity.this, "Already login!", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.d(TAG, "OnClick: nav to login");
                    Intent intent = new Intent(AccountsettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button signout = (Button) findViewById(R.id.acsetsignout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    mAuth.signOut();
                }
                Log.d(TAG, "OnClick: nav to login");
                Intent intent = new Intent(AccountsettingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
