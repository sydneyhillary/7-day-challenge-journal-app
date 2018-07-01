package com.simax.myjournal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserJournall extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    UserJournall.RecycleAdapter adapter;
    ArrayList<Todo> todoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_journall);


        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Todo todo = (Todo)extras.get("todo");
            if (todo != null) {
                EditText nameEdtText = (EditText)findViewById(R.id.title);
                EditText messageEditText = (EditText)findViewById(R.id.journal);
                DatePicker datePicker = (DatePicker) findViewById(R.id.date);

                nameEdtText.setText(todo.getName());
                messageEditText.setText(todo.getMessage());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    Date date = format.parse(todo.getDate());
                    datePicker.updateDate(date.getYear(), date.getMonth(), date.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(UserJournall.this,NeewRecord.class);
                UserJournall.this.startActivity(newIntent);
            }
        });

        todoList = new ArrayList<>();

        /*RecyclerView recyclerView = (RecyclerView)findViewById(R.id.mRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new UserJournall.RecycleAdapter();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.mRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new UserJournall.RecycleAdapter();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }


   /* @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("todoList").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        todoList.clear();

                        Log.w("TodoApp", "getUser:onCancelled " + dataSnapshot.toString());
                        Log.w("TodoApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Todo todo = data.getValue(Todo.class);
                            todoList.add(todo);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }*/

    private class RecycleAdapter extends RecyclerView.Adapter {


        @Override
        public int getItemCount() {
            return todoList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_journal, parent, false);
            UserJournall.RecycleAdapter.SimpleItemViewHolder pvh = new UserJournall.RecycleAdapter.SimpleItemViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            UserJournall.RecycleAdapter.SimpleItemViewHolder viewHolder = (UserJournall.RecycleAdapter.SimpleItemViewHolder) holder;
            viewHolder.position = position;
            Todo todo = todoList.get(position);
            ((UserJournall.RecycleAdapter.SimpleItemViewHolder) holder).title.setText(todo.getName());
        }

        public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title;
            public int position;
            public SimpleItemViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                title = (TextView) itemView.findViewById(R.id.journal_title);
            }

            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(UserJournall.this,NeewRecord.class);
                newIntent.putExtra("todo", todoList.get(position));
                UserJournall.this.startActivity(newIntent);
            }
        }
    }
}
