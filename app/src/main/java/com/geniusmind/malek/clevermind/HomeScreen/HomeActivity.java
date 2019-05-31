package com.geniusmind.malek.clevermind.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.geniusmind.malek.clevermind.R;
import com.geniusmind.malek.clevermind.Splash_Login_Register.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerlayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initalizeViews();
        setSupportActionBar(mToolbar);

        // set home fragment when create this activity by default . . . ;
        setFragment(HomeFragment.newInstance());

        // create ActionBarDrawerToogle for open drawer when click in navigation drawer icon . . . ;
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerlayout, mToolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerlayout.addDrawerListener(drawerToggle);

        // set navigation views when click on navigation icon header . . . . ;
        mNavigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerlayout.closeDrawer(Gravity.START);
            }
        });

        // set navigation view listen when user click in items . . . ;
        mNavigationView.setNavigationItemSelectedListener(this);


    }

    // initalize views and casting (if required) . . . ;
    private void initalizeViews() {
        mAuth=FirebaseAuth.getInstance();
        drawerlayout = findViewById(R.id.drawer_layout);
        mToolbar =  findViewById(R.id.home_toolbar);
        mNavigationView =  findViewById(R.id.nav_drawer);
    }

    // create newIntent to intent from  any activity to this activity . . .;
    public static Intent newIntent(Context currentContext) {
        Intent intent= new Intent(currentContext, HomeActivity.class);
        return intent;


    }

    // set Fragment in fragment container to replace views . . . . .
    public void setFragment(Fragment targetFragment) {
        // initialize fragment manager (manager for listFragment and backstack) . . . . ;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_home_container);

        // check if fragment equal null then begin transaction and add fragment to fragment contaner . . . ;
        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_home_container, targetFragment)
                    .commit();
        }
    }

    // to set fragment when selected item on navigation drawer . . . . ;
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_home_container, fragment)
                .commit();
    }

    // when click on item in navigation drawer . . . .;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.menu_profile:
                getSupportActionBar().setTitle(R.string.profile_label);
                replaceFragment(profileFragment.newInstance());
                break;

            case R.id.menu_attended:
                getSupportActionBar().setTitle(R.string.attended_label);
                replaceFragment(AttendedFragment.newInstance());
                break;

            case R.id.menu_latest_event:
              getSupportActionBar().setTitle(R.string.latestevent_label);
              replaceFragment(LatestEventFragment.getInstance());
                break;

            case R.id.menu_jobs:
                getSupportActionBar().setTitle(R.string.jobs_label);
                replaceFragment(JobsFragment.newInstance());
                break;

            case R.id.menu_payment:
                getSupportActionBar().setTitle(R.string.payment_label);
                replaceFragment(PaymentFragment.newInstance());
                break;

            case R.id.menu_setting:
                getSupportActionBar().setTitle(R.string.setting_label);
                replaceFragment(SettingFragment.newInstance());
                break;

            case R.id.menu_policy:
                getSupportActionBar().setTitle(R.string.policy_label);
                replaceFragment(PolicyPrivacy.newInstance());
                break;

            case R.id.menu_logout:
                mAuth.signOut();
                Intent intent = LoginActivity.newIntent(HomeActivity.this);
                startActivity(intent);
                finish();
                break;

        }
        drawerlayout.closeDrawer(Gravity.START);
        return true;
    }

    // when pressed back button . . . ;
    @Override
    public void onBackPressed() {
        if(drawerlayout.isDrawerOpen(mNavigationView)){
            drawerlayout.closeDrawer(Gravity.START);
            return;
        }
        // set home fragment if user press back button and fragment contain any fragment . . . ;
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.fragment_home_container);
        if(fragment instanceof HomeFragment){
            finish();
        }else {
            getSupportActionBar().setTitle(R.string.home);
            replaceFragment(HomeFragment.newInstance());

        }
    }
}
