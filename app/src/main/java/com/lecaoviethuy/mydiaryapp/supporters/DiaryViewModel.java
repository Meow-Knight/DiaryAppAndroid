package com.lecaoviethuy.mydiaryapp.supporters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lecaoviethuy.mydiaryapp.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class DiaryViewModel extends AndroidViewModel {
    private MutableLiveData<List<Note>> mNoteLiveData;
    private List<Note> notes;
    private DatabaseReference mNoteDatabase;

    public DiaryViewModel(@NonNull Application application) {
        super(application);
        mNoteLiveData = new MutableLiveData<>();
        mNoteDatabase = FirebaseDatabase.getInstance().getReference("notes");
        notes = new ArrayList<>();
        mNoteLiveData.setValue(notes);
    }

    public MutableLiveData<List<Note>> getNoteLiveData() {
        return mNoteLiveData;
    }

    public void addNote(Note note){
        mNoteDatabase.child(note.getId() + "").setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    System.out.println("Success");
                } else {
                    System.out.println("Failed");
                }
            }
        });
    }
}
