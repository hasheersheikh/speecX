package com.example.hashir.speecx;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    Button speak,send,viewcontacts,share;
    EditText resultText,tel;
    TextView personname;
    int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    String message;
    String SENT="SMS_SENT";
    String DELIVERED="SMS_DELIVERED";
    PendingIntent sendPT,deliveredPT;
    BroadcastReceiver smsSendRecevier,smsDeliveredReceiver;
    public static final int  REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tel=findViewById(R.id.tel);
        send=findViewById(R.id.send);
        speak=findViewById(R.id.speak);
        resultText =(EditText) findViewById(R.id.result);
        personname=findViewById(R.id.personname);
        viewcontacts=findViewById(R.id.viewcontacts);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.speak){
                     promptSpeechInput();

                }
            }
        });
        share=findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, resultText.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        viewcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Main2Activity.this,Main3Activity.class);
                i.putExtra("message",message);
                startActivityForResult(i,REQUEST_CODE);

            }
        });
        sendPT=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPT=PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

    }

    public void btn_SendSMS_OnClick(View v){
        String message=resultText.getText().toString();
        String phone=tel.getText().toString();
        if(ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);

        }
        else{
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(phone,null,message,sendPT,deliveredPT);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSendRecevier=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(Main2Activity.this,"SMS send",Toast.LENGTH_SHORT);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(Main2Activity.this,"Geric failaure",Toast.LENGTH_SHORT);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(Main2Activity.this,"no service",Toast.LENGTH_SHORT);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(Main2Activity.this,"null PDU",Toast.LENGTH_SHORT);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(Main2Activity.this,"radio off",Toast.LENGTH_SHORT);
                        break;
                    }
            }
        };
        smsDeliveredReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(Main2Activity.this,"SMS send",Toast.LENGTH_SHORT);
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(Main2Activity.this,"SMS not delivered",Toast.LENGTH_SHORT);
                        break;
                }
            }
        };

        registerReceiver(smsSendRecevier,new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver,new IntentFilter(DELIVERED));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSendRecevier);
    }

    public void promptSpeechInput(){
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something");
        try {
            startActivityForResult(i, 100);
        }catch (ActivityNotFoundException a){
            a.printStackTrace();
            Toast.makeText(Main2Activity.this,"sorry your device doesn't support speech language",Toast.LENGTH_LONG).show();

        }

    }
    public void onActivityResult(int request_code,int result_code,Intent i){
        super.onActivityResult(request_code,result_code,i);
        switch(request_code){
            case 100:if(result_code== RESULT_OK && i!= null){
                ArrayList<String> result=i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultText.setText(result.get(0));
                break;
            }
        }

        try {
            super.onActivityResult(request_code, result_code, i);
            if (request_code == REQUEST_CODE  && result_code  == RESULT_OK) {
                message=i.getStringExtra("message");
                splitString(message);
                //tel.setText(message);
            }
        } catch (Exception ex) {
            Toast.makeText(Main2Activity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void splitString(String str)
    {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i=0; i<str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)) || str.charAt(i)=='+')
                num.append(str.charAt(i));
            else if(Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }

        personname.setText(alpha);
        System.out.println(alpha);
        tel.setText(num);
        System.out.println(special);
    }
}
