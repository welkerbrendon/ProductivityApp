package com.example.brendon.productivityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessagingActivity extends AppCompatActivity {
    public static final String TAG = "MessagingActivity";

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
        
        SmsManager messageSender = SmsManager.getDefault();
        messageSender.sendTextMessage(phoneNumber, null, textMessage, null, null);

        Toast.makeText(this, "SMS Sent to " + phoneNumber, Toast.LENGTH_LONG).show();
    }
}
