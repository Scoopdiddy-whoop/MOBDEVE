package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.adapter.FriendAdapter;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Friend;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.User;

import java.util.ArrayList;
import java.util.HashMap;


public class FriendsDialog extends AppCompatDialogFragment {

    FirebaseDatabase fdb;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.friends, null);
        builder.setView(view);

        Database db = new Database();
        RecyclerView rv_friends = view.findViewById(R.id.rv_friends);
        ImageButton btn_search = view.findViewById(R.id.btn_search);
        EditText et_ID = view.findViewById(R.id.et_ID);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        fdb = FirebaseDatabase.getInstance("https://legitcheckers-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference userRef = fdb.getReference("users/"+user.getUid()+"/friends");
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Friend> friendList = new ArrayList<Friend>();
                    DataSnapshot dataSnapshot = task.getResult();
                    Iterable<DataSnapshot> friends = dataSnapshot.getChildren();
                    for(DataSnapshot snapshot : friends) {
                        String username = ((HashMap)snapshot.getValue()).get("username").toString();
                        String id = snapshot.getKey();
                        Log.i("FR","us: "+ username);
                        Log.i("FR","id: "+ id);
                        friendList.add(new Friend(username, true, id));
                    }
                    FriendAdapter friendAdapter = new FriendAdapter(friendList, getContext());
                    rv_friends.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_friends.setAdapter(friendAdapter);
                }
            }
        });

        btn_search.setOnClickListener(v -> {
            if(et_ID.getText().toString().trim().equals("")){
                et_ID.setError("Please input valid id");
            }
            else{
                db.getUsername(et_ID.getText().toString(), new Database.FirebaseStringCallback() {
                    @Override
                    public void onCallBack(String string) {
                        if(string==null){
                            et_ID.setError("User ID could not be found");
                        }
                        else{
                            ArrayList<Friend> friends = new ArrayList<>();
                            friends.add(new Friend(string, false, et_ID.getText().toString()));
                            FriendAdapter friendAdapter = new FriendAdapter(friends, getContext());
                            rv_friends.setLayoutManager(new LinearLayoutManager(getContext()));
                            rv_friends.setAdapter(friendAdapter);

                        }
                    }
                });
            }
        });
        return builder.create();
    }
    public void makeUser(User user){

    }
}

