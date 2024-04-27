package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Manager_Activity extends AppCompatActivity {

    ManagerRecordsDB RecordsDB;
    EditText etUsernameLoginManager;
    EditText etPasswordLoginManager;
    TextView tvUsernameNotFoundManager;
    TextView tvWrongPasswordManager;
    Button btnLoginManager;

    private boolean isPasswordVisible = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


        etPasswordLoginManager=findViewById(R.id.etPasswordLoginManager);
        etUsernameLoginManager = findViewById(R.id.etUsernameLoginManager);
        tvWrongPasswordManager=findViewById(R.id.tvPasswordWrongErrorLoginManager);
        tvUsernameNotFoundManager=findViewById(R.id.tvUsernameNotFoundManager);
        RecordsDB = new ManagerRecordsDB(Manager_Activity.this);
        tvWrongPasswordManager.setVisibility(View.GONE);
        tvUsernameNotFoundManager.setVisibility(View.GONE);
        etPasswordLoginManager.setVisibility(View.GONE);
        btnLoginManager = findViewById(R.id.btnLoginManager);


        etUsernameLoginManager.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkUsername(s.toString());
            }
        });
        etPasswordLoginManager.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Check if the click was on the drawable (icon) at the end of the EditText
                if (event.getRawX() >= (etPasswordLoginManager.getRight() - etPasswordLoginManager.getCompoundDrawables()[2].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;
                    if (isPasswordVisible) {
                        etPasswordLoginManager.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        etPasswordLoginManager.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibile, 0);  // Icon for visible password
                    } else {
                        etPasswordLoginManager.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        etPasswordLoginManager.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);  // Icon for hidden password
                    }
                    // Move cursor to the end after toggling
                    etPasswordLoginManager.setSelection(etPasswordLoginManager.getText().length());
                    return true;
                }
            }
            return false;
        });
        btnLoginManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });

    }
    private void checkUsername(String Username)
    {
        if(!Username.equals(""))
        {
            RecordsDB.open();
            if(RecordsDB.checkUsernameExists(Username))
            {
                tvUsernameNotFoundManager.setVisibility(View.GONE);
                etPasswordLoginManager.setVisibility(View.VISIBLE);

            }
            else
            {
                tvUsernameNotFoundManager.setVisibility(View.VISIBLE);
                etPasswordLoginManager.setVisibility(View.GONE);
                etPasswordLoginManager.setText("");
                tvWrongPasswordManager.setVisibility(View.GONE);
            }
            RecordsDB.close();
        }
    }
    public void Validate()
    {
        RecordsDB.open();
        if(!etUsernameLoginManager.getText().toString().equals("") || !etPasswordLoginManager.getText().toString().equals(""))
        {
            if(RecordsDB.checkUsernameAndPassword(etUsernameLoginManager.getText().toString() , etPasswordLoginManager.getText().toString()))
            {
                Intent intent = new Intent(Manager_Activity.this,Manage_Users.class);
                startActivity(intent);
                finish();
            }
            else
            {
                tvWrongPasswordManager.setVisibility(View.VISIBLE);
            }

            RecordsDB.close();

        }


    }
    private void Insertion()
    {
        RecordsDB.open();
        RecordsDB.insert("Shehroz","123");
        RecordsDB.close();
    }



}