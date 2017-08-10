package com.example.nikhil.fireapp;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.category;


public class MainActivity extends AppCompatActivity {

    private TextView mtext;
    private Button mSendData;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mDatabase = FirebaseDatabase.getInstance().getReference();

        mtext=(TextView)findViewById(R.id.text1);
        mSendData=(Button)findViewById(R.id.button_addString);

        mSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });


    write_data();

    }

    private void submitPost() {


        ArrayList<User> sampleJournalEntries = getSampleJournalEntries();
        for(int i=0;i<sampleJournalEntries.size();i++) {

            User user=sampleJournalEntries.get(i);
            mDatabase.child(user.getUserId()).setValue(user);
        }
    }
    public ArrayList<User> getSampleJournalEntries(){

        ArrayList<User> journalEnrties = new ArrayList<>();
        //create the dummy journal
        User User1 = new User();
        User1.setUsername("DisneyLand Trip");
        User1.setEmail("We went to Disneyland today and the kids had lots of fun!");
        String key = mDatabase.push().getKey();
        User1.setUserId(key);

        User User2 = new User();
        User2.setUsername("DisneyLand Trip");
        User2.setEmail("We went to Disneyland today and the kids had lots of fun!");
        String key2 = mDatabase.push().getKey();
        User2.setUserId(key2);
        journalEnrties.add(User1);
        journalEnrties.add(User2);

        return journalEnrties;
    }

    public void write_data(){
        final ArrayList<User> mUser = new ArrayList<User>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    User note=new User();
                    note.setUsername(noteSnapshot.getValue(User.class).getUsername());
                    note.setEmail(noteSnapshot.getValue(User.class).getEmail());
                    note.setUserId(noteSnapshot.getValue(User.class).userId);
                    mUser.add(note);
                }
                mtext.setText(mUser.get(0).getEmail());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for(int i=0;i<mUser.size();i++) {
            mtext.setText(mUser.get(i).getUsername());
        }

    }


}
