package com.lecaoviethuy.mydiaryapp.supporters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lecaoviethuy.mydiaryapp.DiaryActivity;
import com.lecaoviethuy.mydiaryapp.MainActivity;
import com.lecaoviethuy.mydiaryapp.NoteDetailActivity;
import com.lecaoviethuy.mydiaryapp.R;
import com.lecaoviethuy.mydiaryapp.entities.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.MyViewHolder> {
    private List<Note> notes;
    private Context mContext;

    public DiaryAdapter(Context context, List<Note> notes) {
        this.mContext = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_item, parent, false);
        return new MyViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Note note = notes.get(position);
        Calendar cal = Calendar.getInstance();
        //
        long differentDate = ((System.currentTimeMillis() - note.getTimestamp()) / (1000 * 60 * 60 * 24));
        holder.tvDay.setText(differentDate + " days ago");

        cal.setTimeInMillis(note.getTimestamp());
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        formatter.setTimeZone(cal.getTimeZone());
        String formattedTime = formatter.format(cal.getTime())
                + "\n"
                + (cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");
        holder.tvTime.setText(formattedTime);

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.cvItem.setCardBackgroundColor(note.getColor());

        // set event when click on cardview item -> start NoteDetailActivity to edit this note
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoteDetailActivity.class);
                intent.putExtra("note", note);
                intent.putExtra("requestCode", DiaryActivity.EDIT_NOTE_CODE);
                if(mContext instanceof DiaryActivity){
                    ((DiaryActivity) mContext).startActivityForResult(intent, DiaryActivity.EDIT_NOTE_CODE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDay;
        public TextView tvTime;
        public TextView tvTitle;
        public TextView tvContent;
        public CardView cvItem;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            cvItem = itemView.findViewById(R.id.cv_item);
        }
    }

}
