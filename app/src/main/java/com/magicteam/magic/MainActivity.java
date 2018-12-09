package com.magicteam.magic;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mainToolbar;
    private FloatingActionButton addButton;
    private FirebaseFirestore db;
    private String currentUserId;
    private BottomNavigationView mainNav;
    private DateFragment dateFragment;
    private RateFragment rateFragment;
    private ExpirationFragment expirationFragment;
    private CommentFragment commentFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        componentsDeclaration();
        customizeToolbar();
        firebaseUtils();
        floatingButton(); // Floating Action Button (add a new deal)
        fragmentUtils();
    }

    private void fragmentUtils() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // set icons
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_date_range_grey_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_star_grey_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_hourglass_empty_grey_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_comment_grey_24dp);

        // set color of icons
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.grey), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        // change color if it's selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.grey), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DateFragment(), "Date");
        adapter.addFrag(new RateFragment(), "Rate");
        adapter.addFrag(new ExpirationFragment(), "Expiration");
        adapter.addFrag(new CommentFragment(), "Comment");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    private void fragmentUtils() {
//        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            switch (menuItem.getItemId()){
//                case R.id.dateFrag:
//                    fragmentTransition(dateFragment);
//                    return true;
//                case R.id.rateFrag:
//                    fragmentTransition(rateFragment);
//                    return true;
//                case R.id.expirationFrag:
//                    fragmentTransition(expirationFragment);
//                    return true;
//                case R.id.commentFrag:
//                    fragmentTransition(commentFragment);
//                    return true;
//                default:
//                    return false;
//            }
//            }
//        });
//        dateFragment = new DateFragment();
//        rateFragment = new RateFragment();
//        expirationFragment = new ExpirationFragment();
//        commentFragment = new CommentFragment();
//    }

    private void floatingButton() {
        // Floating Action Button (add a new deal)
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDealIntent();
            }
        });
    }

    private void firebaseUtils() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void customizeToolbar() {
        setSupportActionBar(mainToolbar);
    }

    private void componentsDeclaration() {
        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
//        mainNav = (BottomNavigationView) findViewById(R.id.mainNav);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in. If not, switch to login activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            loginIntent();
        }
        else { // ask to fill Username and profile image
            currentUserId = mAuth.getCurrentUser().getUid();
            db.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        if(!task.getResult().exists()) {
                            settingsIntent(); // if user doesn't have a picture or a username
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutMenu:
                signOut();
                return true;
            case R.id.settingsMenu:
                settingsIntent();
                return true;
            default:
                return false;
        }
    }

    private void signOut() {
        mAuth.signOut();
        loginIntent();
    }

    private void addDealIntent() { // Add a new deal intent
        Intent newDealActivity = new Intent(MainActivity.this, NewDealActivity.class);
        startActivity(newDealActivity);
        finish();
    }

    private void loginIntent() { // Login Activity Intent
        Intent intentLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLoginActivity);
        finish();
    }

    private void settingsIntent() { // Settings Activity Intent
        Intent intentSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intentSettingsActivity);
        finish();
    }

    /*private void fragmentTransition(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }*/
}
