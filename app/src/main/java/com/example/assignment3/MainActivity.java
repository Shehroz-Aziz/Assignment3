package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    UserRecordsDB RecordsDB;
    EditText etPasswordRegister;
    EditText etUsernameRegister;
    EditText etConfirmPasswordRegister;
    Pattern passwordpattern;
    TextView tvPasswordError;
    TextView tvConfirmPasswordError;
    TextView tvUsernameError;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;


    Button btnSubmit;
    Button btnToLogin;
    Button btnToManagerLogin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecordsDB = new UserRecordsDB(this);
        btnToLogin = findViewById(R.id.btnToLogin);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnToManagerLogin = findViewById(R.id.btnToManager);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etConfirmPasswordRegister=findViewById(R.id.etConfirmPasswordRegister);
        etUsernameRegister=findViewById(R.id.etUsernameRegister);
        // Regular expression for password validation
        passwordpattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{3,}$");
        tvPasswordError = findViewById(R.id.tvPasswordWrongError);
        tvConfirmPasswordError = findViewById(R.id.tvConfirmPasswordWrongError);
        tvUsernameError = findViewById(R.id.tvUsernameExistsWrongError);

        // Hide the error text initially
        tvPasswordError.setVisibility(View.GONE);
        tvConfirmPasswordError.setVisibility(View.GONE);
        tvUsernameError.setVisibility(View.GONE);
        // Add a TextWatcher to monitor changes in the EditText
        etPasswordRegister.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(s.toString());
            }

        });
        etConfirmPasswordRegister.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateConfirmPassword(s.toString());
            }

        });

        etPasswordRegister.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Check if the click was on the drawable (icon) at the end of the EditText
                if (event.getRawX() >= (etPasswordRegister.getRight() - etPasswordRegister.getCompoundDrawables()[2].getBounds().width())) {
                    isPasswordVisible = !isPasswordVisible;
                    if (isPasswordVisible) {
                        etPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        etPasswordRegister.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibile, 0);  // Icon for visible password
                    } else {
                        etPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        etPasswordRegister.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);  // Icon for hidden password
                    }
                    // Move cursor to the end after toggling
                    etPasswordRegister.setSelection(etPasswordRegister.getText().length());
                    return true;
                }
            }
            return false;
        });
        etConfirmPasswordRegister.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Check if the click was on the drawable (icon) at the end of the EditText
                if (event.getRawX() >= (etConfirmPasswordRegister.getRight() - etConfirmPasswordRegister.getCompoundDrawables()[2].getBounds().width())) {
                    isConfirmPasswordVisible = !isConfirmPasswordVisible;
                    if (isConfirmPasswordVisible) {
                        etConfirmPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        etConfirmPasswordRegister.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibile, 0);  // Icon for visible password
                    } else {
                        etConfirmPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        etConfirmPasswordRegister.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);  // Icon for hidden password
                    }
                    // Move cursor to the end after toggling
                    etConfirmPasswordRegister.setSelection(etConfirmPasswordRegister.getText().length());
                    return true;
                }
            }
            return false;
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPasswordRegister.getText().toString().equals("") || etUsernameRegister.getText().toString().equals("") ||tvConfirmPasswordError.getVisibility() == View.VISIBLE)
                {
                    Toast.makeText(MainActivity.this, getString(R.string.enter_data_correctly), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RecordsDB.open();
                    RecordsDB.insert(etUsernameRegister.getText().toString(),etPasswordRegister.getText().toString());
                    RecordsDB.close();
                    etPasswordRegister.setText("");
                    etUsernameRegister.setText("");
                    etConfirmPasswordRegister.setText("");
                    tvPasswordError.setVisibility(View.GONE);
                    tvConfirmPasswordError.setVisibility(View.GONE);
                    tvUsernameError.setVisibility(View.GONE);



                }
            }
        });
        etUsernameRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkUsername(s.toString());
            }
        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginModule.class);
                startActivity(intent);
                finish();

            }
        });

        btnToManagerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Manager_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void checkUsername(String username)
    {
        if(!username.equals(""))
        {
            RecordsDB.open();
            if(RecordsDB.checkUsernameExists(username))
            {
                tvUsernameError.setVisibility(View.VISIBLE);
            }
            else
            {
                tvUsernameError.setVisibility(View.GONE);
            }
            RecordsDB.close();
        }

    }
    private void validatePassword(String password) {
        Matcher matcher = passwordpattern.matcher(password);

        // If password does not meet the requirements, show red underline
        if (!matcher.matches()) {
            tvPasswordError.setVisibility(View.VISIBLE);
        } else {
            // If password meets the requirements, remove the red underline
            tvPasswordError.setVisibility(View.GONE);
        }
    }
    private void validateConfirmPassword(String password) {

        // If password does not meet the requirements, show red underline
        if (!password.equals(etPasswordRegister.getText().toString())) {
            tvConfirmPasswordError.setVisibility(View.VISIBLE);
        } else {
            // If password meets the requirements, remove the red underline
            tvConfirmPasswordError.setVisibility(View.GONE);
        }
    }
}