package com.cas.jiamin.mogic.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cas.jiamin.mogic.AccountsettingActivity.AccountsettingActivity;
import com.cas.jiamin.mogic.Home.HomeActivity;
import com.cas.jiamin.mogic.R;
import com.cas.jiamin.mogic.Share.ShareActivity;
import com.cas.jiamin.mogic.Utility.UniversalImageLoader;
import com.cas.jiamin.mogic.Utility.uploads;
import com.cas.jiamin.mogic.Utility.urls;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Profileactivity class
 *
 * The Profileactivity class set up the activity_profile.xml layout which shows the profile of the user,
 * and some pre-requested data of home layout is also set up in this class.
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private ProgressBar localprogressBar;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 234;
    private ImageView imageView;
    private FirebaseUser user;
    private Uri filePath;
    private TextView UN;
    private TextView DE;
    private DatabaseReference mDatabase;

    /**
     * The onCreate method, state what should be set up when user just enter activity_profile.xml
     * layout.
     *
     * In this method, the bottom bar with three buttons is set up which allows user to switch
     * among home, share and profile layout. The icon, username description of the user is loaded
     * into the corresponding itemview also in this method.
     *
     * @param savedInstanceState The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        if (user != null){
            String x = user.getPhotoUrl().toString();
        }
        imageView = (ImageView) findViewById(R.id.profile_image);
        UN = (TextView) findViewById(R.id.display_name);
        DE = (TextView) findViewById(R.id.display_descrip);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:{
                        Intent intent1 = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    }
                    /*case R.id.ic_search:{
                        Intent intent2 = new Intent(ProfileActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;
                    }*/
                    case R.id.ic_share:{
                        Intent intent3 = new Intent(ProfileActivity.this, ShareActivity.class);
                        startActivity(intent3);
                        break;
                    }
                    /*
                    case R.id.ic_profile:{
                        Intent intent4 = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                    }
                    */
                }
                return false;
            }
        });

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        setupToolbar();

        localprogressBar = (ProgressBar) findViewById(R.id.profprogbar);
        localprogressBar.setVisibility(View.GONE);

        if (user != null) {
            /*filePath = user.getPhotoUrl();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            String UName = user.getDisplayName();
            UN.setText(UName);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference URef = database.getReference("Users");
            DatabaseReference myRef = URef.child(user.getUid());
            DatabaseReference des = myRef.child("UserDesc");
            des.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String value = dataSnapshot.getValue(String.class);
                    DE.setText(value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (user != null) {
            initImageLoader();
            setImageProfile();
        }

        initImageBitmaps();



    }

    /**
     * The initImageBitmaps method takes no arguments and no return, it go through all of the
     * child of the "post" reference and stores the desired information of all post as a list
     * of uploads objects, and called the initRecyclerView method to set up the RecyclerView
     */
    private void initImageBitmaps(){

        mDatabase = FirebaseDatabase.getInstance().getReference("post");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //progressDialog.dismiss();
                urls.clean();
                //urls.contentclean();
                //urls.nameclean();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //
                    uploads up = postSnapshot.getValue(uploads.class);
                    //String x = postSnapshot.child("content").getValue().toString();
                    //up.setContent(x);
                    urls.upArray.add(0, up);
                    //urls.mUrls.add(0, up.getUrl());
                    //urls.uname.add(0, up.getUsername());
                    //urls.content.add(0, up.getContents());
                }
                //creating adapter
                // adapter = new MyAdaptor(getApplicationContext(),mUrls );

                //adding adapter to recyclerview
                //recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //progressDialog.dismiss();
            }
        });

    }


    /**
     * The method takes no arguments which set up the tool bar at the top, an onClicker listener
     * is also set up which allow the users to go to the account setting layout for more operation
     * of their accounts
     */
    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profToolBar);
        setSupportActionBar(toolbar);
        ImageView profileMenu = (ImageView)findViewById(R.id.profilemenu);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: nav to account setting");
                    Intent intent = new Intent(ProfileActivity.this, AccountsettingActivity.class);
                    startActivity(intent);

            }
        });
    }

    /**
     * The icon method takes no arguments which performs like a onClick listener, which allow user
     * enter the profile edit layout if the user is already login, or it will show user he(she) is not login yet.
     *
     * @param view The view object of the button clicked
     */
    public void buttonEdit(View view)
    {
        if (user != null){
            Intent intent=new Intent(ProfileActivity.this, EditprofileActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(ProfileActivity.this, "Please login", Toast.LENGTH_LONG).show();
            Intent intentx = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intentx);
        }
    }

    /**
     * The method takes no arguments, called by onCreate method, which set up the iamge loader, make it ready for loading a
     * image
     */
    private void initImageLoader(){
        UniversalImageLoader imageLoader = new UniversalImageLoader(ProfileActivity.this);
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }

    /**
     * The method takes no arguments, called by onCreate method, which gets the url of the current user's icon and loading it
     * into the image loader
     */
    private void setImageProfile(){
        user = mAuth.getCurrentUser();
        String imgURL = user.getPhotoUrl().toString();
        UniversalImageLoader.setImage(imgURL, imageView, null,"");
    }
}
