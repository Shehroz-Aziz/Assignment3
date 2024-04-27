package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class RecycleBinActivity extends AppCompatActivity implements RecycleBinAdapter.EditContact {

    RecycleBinAdapter Adapter;
    RecyclerView rvUserRecordsRecycle;

    RecycleBin UserDatabase;

    ArrayList<UserRecord> Records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);
        UserDatabase = new RecycleBin(this);
        rvUserRecordsRecycle = findViewById(R.id.rvUserRecordsinBin);
        UserDatabase.open();
        Records = UserDatabase.readAllContacts();
        UserDatabase.close();
        if (Records.size()>0)
        {
            rvUserRecordsRecycle.hasFixedSize();
            rvUserRecordsRecycle.setLayoutManager(new LinearLayoutManager(this));
            Adapter = new RecycleBinAdapter(this, Records);
            rvUserRecordsRecycle.setAdapter(Adapter);
        }
        else
        {
            Toast.makeText(this,"Empty RecycleBin",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,Manage_Users.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onRestoreUser(int index) {
        Records.remove(index);
        Adapter.notifyDataSetChanged();
        Intent intent = new Intent(this,Manage_Users.class);
        startActivity(intent);
        finish();
    }
}