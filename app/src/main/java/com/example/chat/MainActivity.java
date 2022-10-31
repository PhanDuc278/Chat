package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.chat.Fragments.ChatsFragment;
import com.example.chat.Fragments.ProfileFragment;
import com.example.chat.Fragments.UsersFragment;
import com.example.chat.Model.Chat;
import com.example.chat.Model.User;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_Nav ;
    DatabaseReference reference ;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        bottom_Nav = findViewById(R.id.bottom_Nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.container , new ChatsFragment()).commit();


        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }
                if(unread != 0){
                    bottom_Nav.getOrCreateBadge(R.id.chats).setNumber(unread);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottom_Nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chats:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container , new ChatsFragment()).commit();
                        return true ;
                    case R.id.peoples:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container , new UsersFragment()).commit();
                        return true ;
                    case R.id.profiles:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container , new ProfileFragment()).commit();
                        return true ;
                }
                return false;
            }
        });
    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}