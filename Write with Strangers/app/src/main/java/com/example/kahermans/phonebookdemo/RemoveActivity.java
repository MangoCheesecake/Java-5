package com.example.kahermans.phonebookdemo;
// Introducing Google Firebase: A Free Database for your Mobile Apps
// CSTA 2018 Kimberly Hermans
// Remove Activity: User searches for contact and selects one to remove from database
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RemoveActivity extends AppCompatActivity {

    // Google Firebase Database Reference
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    // Event Listener that listens to each child in the database
    private ChildEventListener childEventListener;

    // Local data structure that will store all the values from the database
    private ArrayList<Sentence> sentenceList;

    // Local data structure that will store all the search results from the database
    // These results will be used to populate the ListView
    private ArrayList<Sentence> searchResults;

    // ContactAdapter allows the results to be displayed in a list on the app
    private SentenceAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        // Initializes the references to the database and contacts
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("contacts");

        // Initializes the local data structures
        sentenceList = new ArrayList<Sentence>();
        searchResults = new ArrayList<Sentence>();

        // Sets up the event listener that will specify what happens when access of a node
        // occurs in the database
        childEventListener = new ChildEventListener() {
            // Method is run when any new node is added to the database, and once
            // for every existing node when the activity is loaded
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Adds the Contact to the local data structure
               sentenceList.add(dataSnapshot.getValue(Sentence.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // Need to add the event listener to the database
        myRef.addChildEventListener(childEventListener);

        // Sets up the list view/list adapter to read from the search results array
        listAdapter = new SentenceAdapter(this, searchResults);

        // Associates the ListView to the adapter
        ListView results = findViewById(R.id.listViewResults);
        results.setAdapter(listAdapter);

        // Defines what happens when an item of the listView is clicked
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Sentence selectedItem = (Sentence) parent.getItemAtPosition(position);

                // Removes that Contact from firebase
                myRef.child(selectedItem.getUid()).removeValue();

                // Removes the Contact from the local data structure
                sentenceList.remove(selectedItem);

                // Refreshes the results on the ListView to reflect removal of selected Contact
                refresh(selectedItem.getSentence());

            }
        });
    }

    // Method refreshes the ListView
    public void refresh(String update) {
        listAdapter.clear();    // clears any content
        for (Sentence s : sentenceList) {
            if (s.getSentence().equalsIgnoreCase(update)) {
                // If the contact name is a match, add the result to the listAdapter for display
                listAdapter.add(s);
            }
        }
    }

    // Upon button click of "Remove"
    public void removeRecord(View view) {
        listAdapter.clear();    // clears any content
        boolean found = false;
        EditText text = (EditText) findViewById(R.id.editTextSentence);
        String search = text.getText().toString();  // Extracts name from EditText
        for (Sentence s : sentenceList) {
            if (s.getAuthor().equalsIgnoreCase(search)) {
                // If the contact name is a match, add the result to the listAdapter for display
                listAdapter.add(s);
                found = true;
            }
        }
        if (!found) {
            Toast.makeText(this, text.getText().toString() + " not found.", Toast.LENGTH_LONG).show();
        }
        text.setText("");
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
