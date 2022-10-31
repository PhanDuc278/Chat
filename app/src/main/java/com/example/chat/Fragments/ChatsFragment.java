package com.example.chat.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chat.Adapter.UserAdapter;
import com.example.chat.Model.Chatlist;
import com.example.chat.Model.User;
import com.example.chat.R;
import com.example.chat.SearchUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsFragment extends Fragment {

    FirebaseAuth auth ;
    FirebaseUser user ;

    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<Chatlist> usersList;

    CircleImageView img_ChatProfile ;
    DatabaseReference reference ;
    TextView txt_Search ;
    RecyclerView rec_ChatList ;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chats, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        img_ChatProfile = view.findViewById(R.id.img_ChatProfile);
        txt_Search = view.findViewById(R.id.txt_Search);

        rec_ChatList = view.findViewById(R.id.rec_ChatList);
        rec_ChatList.setHasFixedSize(true);
        rec_ChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loadImgProfile(img_ChatProfile);

        txt_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity() , SearchUserActivity.class));
            }
        });


        return view ;
    }

    //Read chat list
    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList){
                        if (user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                rec_ChatList.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Load profile user

    private void loadImgProfile(CircleImageView img_chatProfile) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getImageURL()).into(img_chatProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}