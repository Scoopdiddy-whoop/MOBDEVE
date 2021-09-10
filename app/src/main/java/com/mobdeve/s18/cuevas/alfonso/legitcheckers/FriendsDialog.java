package com.mobdeve.s18.cuevas.alfonso.legitcheckers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.adapter.FriendAdapter;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.Database;
import com.mobdeve.s18.cuevas.alfonso.legitcheckers.model.User;

import java.util.ArrayList;
import java.util.Map;


public class FriendsDialog extends AppCompatDialogFragment {

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
        db.getFriendList(user.getUid(), new Database.FirebaseMapCallback() {
            @Override
            public void onCallBack(Map<String, String> map) {
                ArrayList<String> ids = new ArrayList<>(map.values());
                FriendAdapter friendAdapter = new FriendAdapter(ids, getContext());
                rv_friends.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_friends.setAdapter(friendAdapter);
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
                            ArrayList<String> friends = new ArrayList<>();
                            friends.add(et_ID.getText().toString());
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

