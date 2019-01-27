package com.example.hashir.speecx;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button b;
    private TextView headingLabel;
    private ImageView fingerPrintImage;
    private TextView ParaLabel;

    private FingerprintManager fingerprintManager;
    KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        headingLabel=findViewById(R.id.header);
        fingerPrintImage=findViewById(R.id.fingerprintImage);
        ParaLabel=findViewById(R.id.value);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            fingerprintManager=(FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
            keyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
            if(!fingerprintManager.isHardwareDetected()){
                ParaLabel.setText("finger print scanner not detected in your device");
            }
            else if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
                ParaLabel.setText("Permission not granted");
            }
            else if(!keyguardManager.isKeyguardSecure()){
                ParaLabel.setText("your device is not secure");
            }
            else if(!fingerprintManager.hasEnrolledFingerprints()){
                ParaLabel.setText("you should register your finger print ");
            }
            else {
                ParaLabel.setText("place your finger on your sensor for get access to app");
                FingerprintHandler fingerprintHandler=new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager,null);
            }

        }
    }
}
