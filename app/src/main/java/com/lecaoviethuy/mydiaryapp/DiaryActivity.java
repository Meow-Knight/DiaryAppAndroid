package com.lecaoviethuy.mydiaryapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lecaoviethuy.mydiaryapp.entities.Note;
import com.lecaoviethuy.mydiaryapp.supporters.DiaryAdapter;
import com.lecaoviethuy.mydiaryapp.supporters.DiaryViewModel;

import java.util.List;

public class DiaryActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;

    private RecyclerView rvDiary;
    private DiaryAdapter mAdapter;
    private DiaryViewModel mViewModel;

    private FloatingActionButton fabAdd;

    private DatabaseReference mNoteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        initialComponents();
        initialFirebaseReference();
        initialEvents();

    }

    private void initialFirebaseReference() {
        mNoteDatabase = FirebaseDatabase.getInstance().getReference("notes");
        mNoteDatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            System.out.println("Receive data");
            List<Note> notes = mViewModel.getNoteLiveData().getValue();
            notes.clear();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                notes.add(dataSnapshot.getValue(Note.class));
            }
            System.out.println("Size: " + notes.size());
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    private void initialEvents() {
    }

    private void initialComponents() {
        // recycler view
        rvDiary = findViewById(R.id.recyclerView);
        rvDiary.setLayoutManager(new LinearLayoutManager(DiaryActivity.this));
        mViewModel = ViewModelProviders.of(DiaryActivity.this).get(DiaryViewModel.class);
        mViewModel.getNoteLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter = new DiaryAdapter(DiaryActivity.this, notes);
                rvDiary.setAdapter(mAdapter);
            }
        });

        // navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_info);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        fabAdd = findViewById(R.id.fab_add);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}