package com.cas.jiamin.mogic.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cas.jiamin.mogic.Profile.ProfileActivity;
import com.cas.jiamin.mogic.R;
import com.cas.jiamin.mogic.Utility.RecyclerViewAdapter;
import com.cas.jiamin.mogic.Share.ShareActivity;
import com.cas.jiamin.mogic.Utility.uploads;
import com.cas.jiamin.mogic.Utility.urls;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * HomeActivity class
 *
 * The HomeActivity class set up the activity_home.xml layout, and users are allowed to view
 * others posts on this layout.
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private FirebaseAuth mAuth;
    //database reference
    private DatabaseReference mDatabase;
    //list to hold all the uploaded objects
    private List<uploads> upload = new ArrayList<>();

    /**
     * The onCreate method, state what should be set up when user just enter activity_home.xml
     * layout.
     *
     * In this method, the bottom bar with three buttons is set up which allows user to switch
     * among home, share and profile layout, and also set up the recyclerview which allows user to
     * view all of the posts by calling initImageBitmaps() method.
     *
     * @param savedInstanceState The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started.");

        //sleep(1000);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    /*
                    case R.id.ic_home:{
                        Intent intent1 = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent1);
                    }

                    case R.id.ic_search: {
                        Intent intent2 = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;
                    }*/
                    case R.id.ic_share: {
                        Intent intent3 = new Intent(HomeActivity.this, ShareActivity.class);
                        startActivity(intent3);
                        break;
                    }
                    case R.id.ic_profile: {
                        Intent intent4 = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                    }
                }
                return false;
            }
        });

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        initImageBitmaps();

        FirebaseDatabase.getInstance().getReference("test").setValue("1");

        //Toast.makeText(HomeActivity.this, urls.upArray.get(0).getContents(),Toast.LENGTH_LONG).show();

    }

    /**
     * The refresh method which performs like a onClick listener, which allow user to refresh
     * the home layout.
     *
     * @param view The view object of the button clicked
     */
    public  void refresh(View view){
        Intent intent1 = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent1);
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
                urls.clean();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    uploads up = postSnapshot.getValue(uploads.class);

                    urls.upArray.add(0,up);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //progressDialog.dismiss();
            }
        });

        initRecyclerView();
    }

    /**
     * The initRecyclerView method called by initImageBitmaps method, takes no arguments and returns
     * nothing. It go through the whole list of uploads objects which is generated in initImageBitmaps
     * method and load the desired data into recyclerview by using the RecyclerViewAdapter class
     */
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,urls.upArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}


