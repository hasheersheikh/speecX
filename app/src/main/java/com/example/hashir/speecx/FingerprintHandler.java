package com.example.hashir.speecx;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHandler  extends FingerprintManager.AuthenticationCallback{
    private Context context;

    public FingerprintHandler(Context context){
        this.context=context;
    }
    public void startAuth(FingerprintManager fingerprintManager,FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal=new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("there was an Auth error"+errString,false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("authentication failed",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("error",false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("you can now access the app ",true);
    }
    private void update(String s,boolean b){
        TextView paralabel=(TextView)((Activity)context).findViewById(R.id.value);
        ImageView imageView=(ImageView)((Activity)context).findViewById(R.id.fingerprintImage);
        paralabel.setText(s);
        if(b==false){
            paralabel.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

        }
        else {
            Intent intent=new Intent(context,Main2Activity.class);
            context.startActivity(intent);
            paralabel.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            imageView.setImageResource(R.drawable.abc);

        }
    }
}

