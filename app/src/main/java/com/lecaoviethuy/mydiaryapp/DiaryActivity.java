package com.lecaoviethuy.mydiaryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lecaoviethuy.mydiaryapp.entities.Note;
import com.lecaoviethuy.mydiaryapp.supporters.DiaryAdapter;
import com.lecaoviethuy.mydiaryapp.supporters.DiaryViewModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DiaryActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private RoundedImageView rivAvatar;
    private TextView tvName;
    private TextView tvEmail;

    private RecyclerView rvDiary;
    private DiaryAdapter mAdapter;
    private DiaryViewModel mViewModel;

    private FloatingActionButton fabAdd;

    private DatabaseReference mNoteDatabase;
    private FirebaseUser mUser;

    public static final int ADD_NEW_NOTE_CODE = 888;
    public static final int EDIT_NOTE_CODE = 965;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        initialComponents();
        initialFirebaseReference();
        initialEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NEW_NOTE_CODE && resultCode == RESULT_OK){
            if(data.getSerializableExtra("returnedNote") != null){
                Note note = (Note) data.getSerializableExtra("returnedNote");
                mViewModel.addNote(note);
            }
        }

        if(requestCode == EDIT_NOTE_CODE){
            if(resultCode == RESULT_OK){
                if(data.getSerializableExtra("returnedNote") != null){
                    Note note = (Note) data.getSerializableExtra("returnedNote");
                    mViewModel.addNote(note);
                }
            } else if (resultCode == NoteDetailActivity.DELETE_NOTE_CODE){
                if(data.getSerializableExtra("returnedNote") != null){
                    Note note = (Note) data.getSerializableExtra("returnedNote");
                    mViewModel.deleteNote(note.getId());
                }
            }
        }
    }

    private void initialFirebaseReference() {
        mNoteDatabase = FirebaseDatabase.getInstance().getReference("notes");
        mNoteDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Note> notes = mViewModel.getNoteLiveData().getValue();
                notes.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    notes.add(dataSnapshot.getValue(Note.class));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
    });
    }

    private void initialEvents() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note();
                note.setId(mViewModel.getNoteLiveData().getValue().size());
                Intent intent = new Intent(DiaryActivity.this, NoteDetailActivity.class);
                intent.putExtra("note", note);
                intent.putExtra("requestCode", ADD_NEW_NOTE_CODE);
                startActivityForResult(intent, ADD_NEW_NOTE_CODE);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.item_assistance:
                        Toast.makeText(DiaryActivity.this, "This function has not supported", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_version:
                        Toast.makeText(DiaryActivity.this, "Diary App version 1.0", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
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
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0);
        rivAvatar = headerView.findViewById(R.id.riv_avatar);
        tvName = headerView.findViewById(R.id.tv_name);
        tvEmail = headerView.findViewById(R.id.tv_email);

        for (UserInfo profile : mUser.getProviderData()) {
            // Name, email address, and profile photo Url
            tvName.setText(profile.getDisplayName());
            tvEmail.setText(profile.getEmail());
            Uri photoUrl = profile.getPhotoUrl();
            Picasso.with(DiaryActivity.this).load(photoUrl).into(rivAvatar);
        }

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