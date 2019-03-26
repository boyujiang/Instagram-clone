package com.cas.jiamin.mogic.Share;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cas.jiamin.mogic.Utility.DateUtil;
import com.cas.jiamin.mogic.Home.HomeActivity;
import com.cas.jiamin.mogic.Profile.ProfileActivity;
import com.cas.jiamin.mogic.R;
import com.cas.jiamin.mogic.Search.SearchActivity;
import com.cas.jiamin.mogic.Utility.uploads;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * ShareActivity class
 *
 * The ShareActivity class set up the activity_share.xml layout, and users are allowed to choose one
 * image to upload with a description
 */
public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";
    private ProgressBar localprogressBar;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 234;
    private ImageView imageView;
    private FirebaseUser user;
    private Uri filePath;
    private StorageReference StorageReference;
    private DatabaseReference postRef;


    /**
     * The onCreate method, state what should be set up when user just enter activity_share.xml
     * layout.
     *
     * In this method, the bottom bar with three buttons is set up which allows user to switch
     * among home, share and profile layout, and when user is not login with a valid account,
     * a "Please login" message will be shown and go to the profile layout.
     *
     * @param savedInstanceState The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");



        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        imageView = (ImageView) findViewById(R.id.preview);
        StorageReference= FirebaseStorage.getInstance().getReference();

        if (user == null){
            Toast.makeText(ShareActivity.this, "Please login", Toast.LENGTH_LONG).show();
            Intent intentx = new Intent(ShareActivity.this, ProfileActivity.class);
            startActivity(intentx);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home: {
                        Intent intent1 = new Intent(ShareActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    }
                    case R.id.ic_search: {
                        Intent intent2 = new Intent(ShareActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;
                    }

                    /*case R.id.ic_share:{
                        Intent intent3 = new Intent(ShareActivity.this, ShareActivity.class);
                        startActivity(intent3);
                    }
                    */
                    case R.id.ic_profile: {
                        Intent intent4 = new Intent(ShareActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                    }
                }
                return false;
            }
        });

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);


    }

    /**
     * The share method which performs like a onClick listener, which allow user to choose one image
     * which they want to share
     *
     * @param view The view object of the button clicked
     */
    public void share (View view){
        showFileChooser();
    }

    public void save (View view){
        if (filePath == null){
            Toast.makeText(ShareActivity.this, "Select your image please", Toast.LENGTH_LONG).show();
            return;
        }



        final String time = DateUtil.getNowDateTime();
        String x = user.getUid();
        final String n = user.getDisplayName();
        EditText text = (EditText) findViewById(R.id.comment);
        final String T = text.getText().toString();
        //List<String> comments = new ArrayList<>();




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference URef = database.getReference("post");
        postRef = URef;


        uploadFile(time, x, n ,T);



        //postRef.child("time").setValue(time);
        //postRef.child("uid").setValue(x);
        //postRef.child("username").setValue(n);
        //postRef.child("content").setValue(0);
        //postRef.child("like").setValue(T);
        //postRef.child("comments").setValue(comments);




        Intent intent=new Intent(ShareActivity.this, HomeActivity.class);
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
     * The uploadFile method takes 4 arguments, uploads the image chosen by the user to the firebase storage
     * and uploads the text information of the post to the firebase database
     *
     * @param time The time of the post created which is one of the text information which will be
     *             upload to database
     * @param x The user id of the user who create the post which is one of the text information which will be
     *          upload to database
     * @param n The user name of the user who create the post which is one of the text information which
     *          will be upload to database
     * @param T The description of the post which is one of the text information which will be
     *          upload to database
     */
    private void uploadFile(final String time, final String x, final String n, final String T) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading..");
            progressDialog.show();
            final StorageReference PicRef = StorageReference.child("images").child(x).child(time);

            PicRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            PicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    /*postRef.child("url").setValue(uri.toString());
                                    postRef.child("time").setValue(time);
                                    postRef.child("uid").setValue(x);
                                    postRef.child("username").setValue(n);
                                    postRef.child("content").setValue(T);
                                    postRef.child("like").setValue(0);*/
                                    uploads up = new uploads(T,0,n, uri.toString(), x, time );
                                    postRef.child(time).setValue(up);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(ShareActivity.this, "fail", Toast.LENGTH_LONG).show();
                                }
                            });
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage((int) progress + "% Uploading");
                }
            })
            ;


        } else {

        }
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
                //fileName = data.getData();

                filePath = data.getData();
                //Toast.makeText(FirebaseUIActivity.this, filePath.toString(), Toast.LENGTH_LONG).show();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}