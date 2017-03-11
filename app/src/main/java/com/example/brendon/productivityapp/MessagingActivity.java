package com.example.brendon.productivityapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessagingActivity extends AppCompatActivity {
    public static final String TAG = "MessagingActivity";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;

    // For SMS Messaging
    Button sendButton;
    EditText phoneNumberField;
    EditText textMessageField;
    String phoneNumber;
    String textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        // For SMS Messaging
        sendButton = (Button) findViewById(R.id.buttonSendSMS);
        textMessageField = (EditText) findViewById(R.id.textFieldMessage);
        phoneNumberField = (EditText) findViewById(R.id.textFieldPhoneNumber);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMS();
            }
        });
    }

    private void sendSMS() {
        phoneNumber = phoneNumberField.getText().toString();
        textMessage = textMessageField.getText().toString();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, textMessage, null, null);
                    Toast.makeText(this, "SMS Sent to " + phoneNumber, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
