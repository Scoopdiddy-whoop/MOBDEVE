package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.databinding.ActivityRoomBinding;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;

import java.util.ArrayList;

public class  PlayOnline extends AppCompatActivity {

    private ActivityRoomBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> roomList;
    private String roomName;
    private FirebaseAuth mAuth;
    private DatabaseReference roomRef;
    private DatabaseReference roomsRef;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance("https://legitcheckers-default-rtdb.asia-southeast1.firebasedatabase.app");
        mAuth = FirebaseAuth.getInstance();
        roomList = new ArrayList<>();

        Database db = new Database();

        db.getUsername(mAuth.getCurrentUser().getUid(), new Database.FirebaseStringCallback() {
            @Override
            public void onCallBack(String username) {
                roomAction(username);
            }
        });
    }
    public void roomAction(String username){
        binding.btnCreateRoom.setOnClickListener(v -> {
                roomName = username;
            binding.btnCreateRoom.setText("CREATING ROOM");
            binding.btnCreateRoom.setEnabled(false);
            roomRef = firebaseDatabase.getReference("rooms/"+roomName+"/player1");
            Log.i("ROOM ACTION", "ROOM CREATED");
            addRoomEventListener();
            roomRef.setValue(username);
        });
        binding.lvRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomName = roomList.get(position);
                roomRef = firebaseDatabase.getReference("rooms/"+roomName+"/player2");
                addRoomEventListener();
                roomRef.setValue(username);
                Log.i("ROOM ACTION", "ROOM JOINED");
            }
        });
        //show new rooms
        addRoomsEventListener();
    }
    public void addRoomEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //join the room
                binding.btnCreateRoom.setText("CREATE ROOM");
                binding.btnCreateRoom.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error
                binding.btnCreateRoom.setText("CREATE ROOM");
                binding.btnCreateRoom.setEnabled(true);
                Toast.makeText(getApplicationContext(), "ERROR JOINING!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addRoomsEventListener(){
        roomsRef = firebaseDatabase.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                Log.i("ADD ROOMS", "ROOMS ADDED");
                for(DataSnapshot snapshot : rooms) {
                    roomList.add(snapshot.getKey());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(PlayOnline.this,
                            android.R.layout.simple_list_item_1, roomList);
                    Log.i("ADD ROOMS", "ROOM:" + roomList.get(0));
                    binding.lvRoom.setAdapter(adapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
