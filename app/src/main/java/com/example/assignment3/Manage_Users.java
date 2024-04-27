package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Manage_Users extends AppCompatActivity implements UserRecordsAdapter.EditContact{

    UserRecordsAdapter Adapter;
    RecyclerView rvUserRecords;

    UserRecordsDB UserDatabase;
    Button ToRecycleBin;

    ArrayList<UserRecord> Records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        ToRecycleBin = findViewById(R.id.btnToBin);
        UserDatabase = new UserRecordsDB(this);
        UserDatabase.open();
        Records = UserDatabase.readAllContacts();
        UserDatabase.close();
        rvUserRecords = findViewById(R.id.rvUserRecords);
        rvUserRecords.hasFixedSize();
        rvUserRecords.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(this, Records.get(0).getUsername(),Toast.LENGTH_LONG).show();
        Adapter = new UserRecordsAdapter(this, Records);
        rvUserRecords.setAdapter(Adapter);
        ToRecycleBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manage_Users.this, RecycleBinActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onDeleteUser(int index) {
        Records.remove(index);
        Adapter.notifyDataSetChanged();

    }
    @Override
    public void onUpdateUser(int index, String[] values) {
        Records.get(index).setUsername(values[0]);
        Records.get(index).setPassword(values[1]);
        Adapter.notifyDataSetChanged();
    }


}