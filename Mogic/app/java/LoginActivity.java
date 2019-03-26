package com.cas.jiamin.mogic.AccountsettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cas.jiamin.mogic.Home.HomeActivity;
import com.cas.jiamin.mogic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * LoginActivity class
 *
 * The LoginActivity class which set up the activity_login.xml layout for user to login
 * with their email and passwords.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    /**
     * The onCreate method, state what should be set up when user just enter activity_login.xml
     * layout.
     *
     * The onclick listener which allows the user to back to the previous layout is set up in this
     * method.
     *
     * @param savedInstanceState The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView acsettab = (ImageView) findViewById(R.id.acsetbk);
        acsettab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: nav to profile");
                Intent intent = new Intent(LoginActivity.this, AccountsettingActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * The register method which performs like a onClick listener, which allow user to reach to
     * the RegisterActivity after clicking.
     *
     * @param view The view object of the button clicked
     */
    public void register(View view) {
        Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * The register method which performs like a onClick listener, which allow user to use the
     * build-in method of firebase to login with their email and password.
     *
     * @param view The view object of the button clicked
     */
    public void login (View view){
        EditText id = (EditText) findViewById(R.id.Lemail);
        String UserID = id.getText().toString();
        EditText pw = (EditText) findViewById(R.id.Lpassword);
        String Password = pw.getText().toString();

        mAuth.signInWithEmailAndPassword(UserID, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login success",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }
}
