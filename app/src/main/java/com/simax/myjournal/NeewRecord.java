package com.simax.myjournal;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeewRecord extends AppCompatActivity {

    private static final String TAG = GoogleSignInActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText addTitle, addJournal;

    private List<Task> allTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neew_record);

        allTask = new ArrayList<Task>();

        addTitle = (EditText) findViewById(R.id.title);
        addJournal = (EditText) findViewById(R.id.journal);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save_journal);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });
    }
        void saveTodo() {
            // first section
            // get the data to save in our firebase db
            EditText nameEdtText = (EditText)findViewById(R.id.title);
            EditText messageEditText = (EditText)findViewById(R.id.journal);
            DatePicker datePicker = (DatePicker) findViewById(R.id.date);
            Date date = new Date();
            date.setMonth(datePicker.getMonth());
            date.setYear(datePicker.getYear());
            date.setDate(datePicker.getDayOfMonth());

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String dateString = format.format(date);
            //make the modal object and convert it into hasmap


            //second section
            //save it to the firebase db
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            database.child(user.getUid()).setValue(user_class);
            String key = database.getReference("todoList").push().getKey();

            Todo todo = new Todo();
            todo.setName(nameEdtText.getText().toString());
            todo.setMessage(messageEditText.getText().toString());
            todo.setDate(dateString);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put( key, todo.toFirebaseObject());
            database.getReference("todoList").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        finish();
                    }
                }
            });
        }
    }

