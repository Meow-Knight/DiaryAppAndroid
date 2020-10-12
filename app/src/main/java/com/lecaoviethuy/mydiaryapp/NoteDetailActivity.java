package com.lecaoviethuy.mydiaryapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lecaoviethuy.mydiaryapp.entities.Note;

import java.util.ArrayList;
import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class NoteDetailActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextView tvTitleActivity;
    private ImageView ivBack;
    private ImageView ivApply;
    private EditText edtTitle;
    private EditText edtContent;

    //
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;
    private TimePickerDialog mTimePickerDialog;
    private ColorPicker mColorPicker;
    private AlertDialog mDeleteDialog;

    // variables
    private int requestCodeFromParent;
    private Calendar calendar = Calendar.getInstance();
    private Note mNote;
    private int mColor = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Intent intent = getIntent();
        if(intent != null){
            mNote = (Note) intent.getSerializableExtra("note");
            requestCodeFromParent = intent.getIntExtra("requestCode", DiaryActivity.ADD_NEW_NOTE_CODE);
        }

        calendar.setTimeInMillis(System.currentTimeMillis());
        initialPickerDialogs();
        initialComponents();
        initialComponentEvents();
    }

    private void initialComponentEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if title and context had value -> finish acitvity
                // else show a toast and return void;
                if(edtTitle.getText().toString().isEmpty() || edtContent.getText().toString().isEmpty()){
                    Toast.makeText(NoteDetailActivity.this, "Please enter full information", Toast.LENGTH_SHORT).show();
                } else {
                    mNote.setTitle(edtTitle.getText().toString());
                    mNote.setContent(edtContent.getText().toString());
                    mNote.setTimestamp(calendar.getTimeInMillis());
                    mNote.setColor(mColor);
                    Intent intent = new Intent();
                    intent.putExtra("returnedNote", mNote);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void initialPickerDialogs() {
        // for set date item
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
            }
        };
        mDatePickerDialog = new DatePickerDialog(this
                , mOnDateSetListener
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        // for set time item
        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR, i);
                calendar.set(Calendar.MINUTE, i1);
            }
        };
        mTimePickerDialog = new TimePickerDialog(this
                , mOnTimeSetListener
                , calendar.get(Calendar.HOUR)
                , calendar.get(Calendar.MINUTE)
                , true);
        // for set color item
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#0000FF");
        colors.add("#FF0000");
        colors.add("#FFFF00");
        colors.add("#FF6600");
        colors.add("#00FF00");
        colors.add("#6600FF");
        colors.add("#3399FF");
        colors.add("#FFFF66");
        mColorPicker = new ColorPicker(this);
        mColorPicker.setColors(colors)
                .setColumns(colors.size()/2)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        mColor = color;
                    }

                    @Override
                    public void onCancel() {
                    }
        });
        // for delete item
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm")
                .setMessage("Delete this note?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnedIntent = new Intent();
                        mNote = null;
                        returnedIntent.putExtra("returnedNote", mNote);
                        setResult(RESULT_OK, returnedIntent);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mDeleteDialog = builder.create();
    }

    private void initialComponents() {
        bottomNavigationView = findViewById(R.id.bnv_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.pick_date_item:
                        mDatePickerDialog.show();
                        return true;
                    case R.id.pick_time_item:
                        mTimePickerDialog.show();
                        return true;
                    case R.id.pick_color_item:
                        mColorPicker.show();
                        return true;
                    case R.id.delete_item:
                        if(requestCodeFromParent == DiaryActivity.ADD_NEW_NOTE_CODE){
                            Toast.makeText(NoteDetailActivity.this, "Cannot delete!", Toast.LENGTH_SHORT).show();
                        } else {
                            mDeleteDialog.show();
                        }
                        return true;
                }
                return false;
            }
        });

        tvTitleActivity = findViewById(R.id.tv_title_activity);
        ivBack = findViewById(R.id.iv_back);
        ivApply = findViewById(R.id.iv_apply);
        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
    }
}