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

import com.cas.jiamin.mogic.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegisterActivity class
 *
 * The RegisterActivity class which set up the activity_register.xml layout, and this is the first
 * step for users to create their accounts: entering their emails and desired passwords.
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;


    /**
     * The onCreate method, state what should be set up when user just enter activity_register.xml
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
        setContentView(R.layout.activity_register);
        ImageView acsettab = (ImageView) findViewById(R.id.acsetbk);
        acsettab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: nav to profile");
                Intent intent = new Intent(RegisterActivity.this, AccountsettingActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * The back method which performs like a onClick listener, which allow user to use the
     * build-in method of firebase to sign up with their emails and passwords, and allow user
     * to login immediately with their new account after signed up.
     *
     * @param view The view object of the button clicked
     */
    public void back (View view){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);

        EditText id = (EditText) findViewById(R.id.Emaill);
        String UserID = id.getText().toString();
        EditText pw = (EditText) findViewById(R.id.Passwordd);
        String Password = pw.getText().toString();

        Matcher matcher = pattern.matcher(UserID);
        if (!matcher.matches()){
            Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
            return;
        }

        if (Password.length() < 8){
            Toast.makeText(RegisterActivity.this, "Please enter a password with at least 8 digits", Toast.LENGTH_LONG).show();
            return;

        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.createUserWithEmailAndPassword(UserID, Password)
                .addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "please choose your icon and username", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this, Register2Activity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(RegisterActivity.this,"Something wrong, try another",Toast.LENGTH_SHORT).show();
            }
        });




    }
}