package com.example.testlibv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.testlibv1.ui.ProfileFragment;
import com.example.testlibv1.ui.gallery.GalleryFragment;
import com.example.testlibv1.ui.home.HomeFragment;
import com.example.testlibv1.ui.slideshow.SlideshowFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;


    private long pressedTime;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DocumentReference docRef;
    FirebaseFirestore db;
    FirebaseDatabase fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fdb = FirebaseDatabase.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_gallery:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GalleryFragment()).commit();
                break;
            case R.id.nav_slideshow:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SlideshowFragment()).commit();
                break;
            case R.id.manage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageFragment()).commit();
                break;
            case R.id.pic:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PictureFragment()).commit();
                break;
            case R.id.requests:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RequestFragment()).commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent change = new Intent(NavigationActivity.this, LoginActivity.class);
                Toast.makeText(this, "Successfully Logged Out.", Toast.LENGTH_SHORT).show();
                startActivity(change);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFrag = fm.findFragmentById(R.id.fragment_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (currentFrag instanceof HomeFragment) {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Press again to exit.", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            Intent change = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivity(change);
        } else {

            View headerView = navigationView.getHeaderView(0);
            ShapeableImageView drawerImage = (ShapeableImageView) headerView.findViewById(R.id.uimage);
            TextView drawerUsername = (TextView) headerView.findViewById(R.id.User_Name);
            TextView drawerAccount = (TextView) headerView.findViewById(R.id.User_Email);

            fdb.getReference().child("User_Details").child(mUser.getDisplayName()).child("profile").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String profile = snapshot.getValue(String.class);
                    if (profile != null) {
                        Glide.with(getApplicationContext()).load(profile).into(drawerImage);
                    } else {
                        drawerImage.setImageResource(R.drawable.user_icon);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(NavigationActivity.this, "Task Cancelled.", Toast.LENGTH_SHORT).show();
                }
            });

            drawerUsername.setText(mUser.getDisplayName());
            drawerAccount.setText(mUser.getEmail());

            db = FirebaseFirestore.getInstance();
            try {
                docRef = db.collection("Admins").document(mUser.getDisplayName());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(NavigationActivity.this, "Admin detected!", Toast.LENGTH_SHORT).show();
                                Menu nav_menu = navigationView.getMenu();
                                nav_menu.findItem(R.id.manage).setVisible(true);
                                nav_menu.findItem(R.id.requests).setVisible(true);
                                nav_menu.findItem(R.id.pic).setVisible(true);
                            } else {
                                Menu nav_menu = navigationView.getMenu();
                                nav_menu.findItem(R.id.manage).setVisible(false);
                                nav_menu.findItem(R.id.requests).setVisible(false);
                                nav_menu.findItem(R.id.pic).setVisible(false);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception");
            }
        }
    }
}