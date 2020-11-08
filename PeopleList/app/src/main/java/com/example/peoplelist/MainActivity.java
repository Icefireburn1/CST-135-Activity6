package com.example.peoplelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    Button btn_add, btn_sortABC, btn_sortAGE;

    ListView lv_friendsList;

    PersonAdapter adapter;

    MyFriends myFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        btn_sortABC = findViewById(R.id.btn_sortABC);
        btn_sortAGE = findViewById(R.id.btn_sortAge);
        lv_friendsList = findViewById(R.id.lv_listofnames);

        myFriends = ((MyApplication)this.getApplication()).getMyFriends();

        adapter = new PersonAdapter(MainActivity.this, myFriends);

        lv_friendsList.setAdapter(adapter);

        // listen for messages
        Bundle incomingMessages = getIntent().getExtras();

        if (incomingMessages != null) {

            // capture message
            String name = incomingMessages.getString("name");
            int age = Integer.parseInt(incomingMessages.getString("age"));
            int pictureNumber = Integer.parseInt(incomingMessages.getString("pictureNumber"));
            int positionEdited  = incomingMessages.getInt("edit");

            Person p = new Person(name, age, pictureNumber);

            if (positionEdited > -1) {
                myFriends.getMyFriendsList().remove(positionEdited);
            }

            myFriends.getMyFriendsList().add(p);
            adapter.notifyDataSetChanged();
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NewPersonForm.class);
                startActivity(i);
            }
        });

        btn_sortAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myFriends.getMyFriendsList(), new Comparator<Person>() {
                    @Override
                    public int compare(Person p1, Person p2) {
                        return p1.getAge() - p2.getAge();
                    }
                });


                adapter.notifyDataSetChanged();
            }
        });

        btn_sortABC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myFriends.getMyFriendsList());
                adapter.notifyDataSetChanged();
            }
        });

        lv_friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editPerson(position);
            }
        });
    }

    private void editPerson(int position) {
        Intent i = new Intent(getApplicationContext(), NewPersonForm.class);

        Person p = myFriends.getMyFriendsList().get(position);

        i.putExtra("edit", position);
        i.putExtra("name", p.getName());
        i.putExtra("age", p.getAge());
        i.putExtra("pictureNumber", p.getPictureNumber());

        startActivity(i);
    }
}