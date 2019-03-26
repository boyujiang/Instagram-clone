package com.cas.jiamin.mogic.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cas.jiamin.mogic.Home.HomeActivity;
import com.cas.jiamin.mogic.R;
import com.cas.jiamin.mogic.Utility.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

/**
 * EditprofileActivity class
 *
 * The EditprofileActivity class set up the activity_profile.xml layout which allow the user to
 * edit some data on their profile.
 */
public class EditprofileActivity extends AppCompatActivity {
    private static final String TAG = "EditprofileActivity";
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 234;
    private ImageView imageView;
    private FirebaseUser user;
    private Uri filePath;

    /**
     * The onCreate method, state what should be set up when user just enter activity_editprofile.xml
     * layout.
     *
     * In this method, a onClicker listener is set up which allow users to go to the previous layout
     * and load the current icon of the user into the imageview
     *
     * @param savedInstanceState The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ImageView edprotab = (ImageView) findViewById(R.id.backArrow);
        edprotab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: nav to profile");
                Intent intent = new Intent(EditprofileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        imageView = (ImageView) findViewById(R.id.profile_photo);
        //filePath = user.getPhotoUrl();

        initImageLoader();
        setImageProfile();
    }

    /**
     * The change method which performs like a onClick listener, which allow user to change their
     * icons
     *
     * @param view The view object of the button clicked
     */
    public void change (View view){
        showFileChooser();
    }

    /**
     * The save method which performs as a onClick listener, which allow users to save the changes
     * to their profile, and return to the home layout after finishing.
     *
     * @param view
     */
    public void save (View view){
        EditText un = (EditText) findViewById(R.id.eusername);
        String UName = un.getText().toString();
        EditText desc = (EditText) findViewById(R.id.edescription);
        String description = desc.getText().toString();
        EditText email = (EditText) findViewById(R.id.eemail);
        String Email = email.getText().toString();
        EditText pw = (EditText) findViewById(R.id.epassword);
        String password = pw.getText().toString();

        if (filePath != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(filePath)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditprofileActivity.this, "User icon updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditprofileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        if (UName.length() != 0) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(UName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditprofileActivity.this, "User name updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditprofileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
        }

        if (description.length() != 0){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference URef = database.getReference("Users");
            DatabaseReference myRef = URef.child(user.getUid());
            myRef.child("UserDesc").setValue(description);
            Toast.makeText(EditprofileActivity.this, "User description updated", Toast.LENGTH_SHORT).show();
        }

        if (Email.length() != 0){
            user.updateEmail(Email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditprofileActivity.this, "Email update success", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(EditprofileActivity.this, "Email update fail, try another", Toast.LENGTH_LONG).show();
                                return;

                            }
                        }
                    });
        }

        if (password.length() != 0){
            user.updatePassword("3tgerghiuefg2kjghvigh3nljfyvishrgkwu")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditprofileActivity.this, "password update success", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(EditprofileActivity.this, "password update fail, try another", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });
        }


        Intent i = new Intent(EditprofileActivity.this, HomeActivity.class);
        startActivity(i);
    }

    /**
     * The showFileChooser method, called by change method, which allow the users to choose one of
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
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The method takes no arguments, called by onCreate method, which set up the iamge loader, make it ready for loading a
     * image
     */
    private void initImageLoader(){
        UniversalImageLoader imageLoader = new UniversalImageLoader(EditprofileActivity.this);
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }

    /**
     * The method takes no arguments, called by onCreate method, which gets the url of the current user's icon and loading it
     * into the image loader
     */
    private void setImageProfile(){
        user = mAuth.getCurrentUser();
        String imgURL = user.getPhotoUrl().toString();
        StorageReference StorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference PicRef = StorageReference.child("images").child("2e2SY03qKQOsPP9LJnTi0D8kU7f2").child("20181115122317");
        UniversalImageLoader.setImage(imgURL, imageView, null,"");
    }

}
