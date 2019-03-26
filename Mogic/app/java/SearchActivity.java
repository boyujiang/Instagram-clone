package com.cas.jiamin.mogic.Search;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cas.jiamin.mogic.Home.HomeActivity;
import com.cas.jiamin.mogic.Profile.ProfileActivity;
import com.cas.jiamin.mogic.R;
import com.cas.jiamin.mogic.Share.ShareActivity;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started.");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:{
                        Intent intent1 = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    }
                    /*
                    case R.id.ic_search:{
                        Intent intent2 = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(intent2);
                    }
                    */
                    case R.id.ic_share:{
                        Intent intent3 = new Intent(SearchActivity.this, ShareActivity.class);
                        startActivity(intent3);
                        break;
                    }
                    case R.id.ic_profile:{
                        Intent intent4 = new Intent(SearchActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                    }
                }
                return false;
            }
        });

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }
}
