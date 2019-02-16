package com.example.kahermans.phonebookdemo;

// Introducing Google Firebase: A Free Database for your Mobile Apps
// CSTA 2018 Kimberly Hermans
// Add Activity - Allows users to add contacts to the phone book

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    // Google Firebase Database References
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    // Event Listener that listens to each child in the database
    private ChildEventListener childEventListener;

    // ArrayAdapter allows the results to be displayed in a list on the app
    private SentenceAdapter2 listAdapter;

    private ArrayList<Sentence> sentenceList;
    private ArrayList<Sentence> holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Initializes the references to the database and contacts
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("contacts");

        // Set up an array that will have the contents that you want to display
        sentenceList = new ArrayList<Sentence>();
        holder = new ArrayList<Sentence>();

        childEventListener = new ChildEventListener(){
            @Override
            // Method is run when any new node is added to the database, and once
            // for every existing node when the activity is loaded
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                holder.add( dataSnapshot.getValue(Sentence.class));

                if(!holder.isEmpty())
                {
                    listAdapter.clear();
                    listAdapter.add(holder.get(holder.size()-1));
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        // Need to add the event listener to the database
        myRef.addChildEventListener(childEventListener);

        // Sets up the list adapter to read from the results array
        listAdapter = new SentenceAdapter2(this, sentenceList );
        ListView results = findViewById(R.id.latestSentence);
        results.setAdapter(listAdapter);
    }

    public void addSentence(View view)
    {
        EditText editSentence = findViewById(R.id.editTextSentence);
        String sentence = editSentence.getText().toString();
        EditText editAuthor = findViewById(R.id.editTextAuthor);
        String author = editAuthor.getText().toString();

        if( sentence.length() > 0 )
        {
            String key = myRef.push().getKey(); // Generates unique random key
            Sentence s = new Sentence(sentence, author, key);
            myRef.child(key).setValue(s);   // Adds new Contact to the Database
            Toast.makeText(this, s.getSentence() + " successfully added.", Toast.LENGTH_LONG).show();
        }

        // Resets fields
        editSentence.setText("");
        editAuthor.setText("");
    }

    public void goHome( View view )
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity( intent);
    }
}
