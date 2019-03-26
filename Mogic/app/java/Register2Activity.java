package com.cas.jiamin.mogic.AccountsettingActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.cas.jiamin.mogic.Home.HomeActivity;
import com.cas.jiamin.mogic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

/**
 * Register2Activity class
 *
 * The Register2Activity class which set up the activity_register2.xml layout, and this is the
 * second step for users to create their accounts: choose their icons and usernames.
 */
public class Register2Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Register2";
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private ImageView imageView2;
    FirebaseUser user;

    /**
     * The onCreate method, state what should be set up when user just enter activity_register2.xml
     * layout.
     *
     * @param savedInstanceState The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        imageView2 = (ImageView) findViewById(R.id.imageView2);
    }

    /**
     * The icon method which performs like a onClick listener, which allow user to choose their
     * icons
     *
     * @param view The view object of the button clicked
     */
    public void icon (View view){
        showFileChooser();
    }

    /**
     * The register2 method which performs like a onClick listener, which updates
     * icon and username chosen by the user into the firebase, and set up the basic information
     * of the user in the database, finishes the sign up and go to the main layout.
     *
     * @param view The view object of the button clicked
     */
    public void register2 (View view){
        EditText name = (EditText) findViewById(R.id.namee);
        String N = name.getText().toString();

        if (filePath == null){
            Toast.makeText(Register2Activity.this, "Please select an icon", Toast.LENGTH_LONG).show();
            return;
        }

        user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(N)
                .setPhotoUri(filePath)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register2Activity.this, "updated", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Register2Activity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        while (user.getDisplayName()==null);
        String x = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference URef = database.getReference("Users");
        DatabaseReference myRef = URef.child(x);
        String name1 = user.getDisplayName();
        //Toast.makeText(Register2Activity.this, name1, Toast.LENGTH_LONG).show();
        myRef.child("UserDesc").setValue("Hi, I am " + name1);
        myRef.child("PostNumber").setValue(0);

        Toast.makeText(this,"Success", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(Register2Activity.this, HomeActivity.class);
        startActivity(intent);

    }

    /**
     * The showFileChooser method, called by icon method, which allow the users to choose one of
     * the photo files from their devices.
     */
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    /**
     * The onActivityResult method called by showFileChooser method. In this method, the uri of image files
     * choosen by the users was stored in the variable filePath.
     *
     * @param requestCode The integer variable used to identify the type of the request
     * @param resultCode The integer variable used to identify the status of the choosing result
     * @param data The intent object of the image chosen
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView2.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}