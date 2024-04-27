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

public class LoginModule extends AppCompatActivity {

    UserRecordsDB RecordsDB;
    EditText etUsernameLogin;
    EditText etPasswordLogin;
    TextView tvUsernameNotFound;
    TextView tvWrongPassword;
    Button btnLogin;

    private boolean isPasswordVisible = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_module);

        etPasswordLogin=findViewById(R.id.etPasswordLogin);
        etUsernameLogin = findViewById(R.id.etUsernameLogin);
        tvWrongPassword=findViewById(R.id.tvPasswordWrongErrorLogin);
        tvUsernameNotFound=findViewById(R.id.tvUsernameNotFound);
        RecordsDB = new UserRecordsDB(this);
        tvWrongPassword.setVisibility(View.GONE);
        tvUsernameNotFound.setVisibility(View.GONE);
        etPasswordLogin.setVisibility(View.GONE);
        btnLogin = findViewById(R.id.btnLogin);

        etUsernameLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkUsername(s.toString());
            }
        });
        etPasswordLogin.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Check if the click was on the drawable (icon) at the end of the EditText
                if (event.getRawX() >= (etPasswordLogin.getRight() - etPasswordLogin.getCompoundDrawables()[2].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;
                    if (isPasswordVisible) {
                        etPasswordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibile, 0);  // Icon for visible password
                    } else {
                        etPasswordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        etPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);  // Icon for hidden password
                    }
                    // Move cursor to the end after toggling
                    etPasswordLogin.setSelection(etPasswordLogin.getText().length());
                    return true;
                }
            }
            return false;
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
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
                tvUsernameNotFound.setVisibility(View.GONE);
                etPasswordLogin.setVisibility(View.VISIBLE);

            }
            else
            {
                tvUsernameNotFound.setVisibility(View.VISIBLE);
                etPasswordLogin.setVisibility(View.GONE);
                etPasswordLogin.setText("");
                tvWrongPassword.setVisibility(View.GONE);
            }
            RecordsDB.close();
        }
    }
    public void Validate()
    {
        RecordsDB.open();
        if(!etUsernameLogin.getText().toString().equals("") || !etPasswordLogin.getText().toString().equals(""))
        {
           if(RecordsDB.checkUsernameAndPassword(etUsernameLogin.getText().toString() , etPasswordLogin.getText().toString()))
           {
               Intent intent = new Intent(LoginModule.this,MainActivity2.class);
               startActivity(intent);
               finish();
           }
           else
           {
               tvWrongPassword.setVisibility(View.VISIBLE);
           }

        RecordsDB.close();

        }


    }

}