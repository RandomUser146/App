package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.app.databinding.ActivitySystemSetupBinding;
import com.example.app.util.CropData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SystemSetup extends AppCompatActivity {
    ActivitySystemSetupBinding binding;
    ArrayAdapter<String> systems;
    ArrayAdapter<String> Wifi;
    ArrayAdapter<String> crop;
    ArrayAdapter<String> area;
    WifiManager mWifiManager;

    private String wifiSSID;
    private String wifiPassword;
    private float ph;
    private float rainfall;

    private String espSSID;

    final String TAG = "SystemSetup";

    private final String list[] = {"ESP32"};
    private final String wifi[] = {"desktop"};
    private final String crops[] = {"rice", "maize"};
    private final String areas[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    private List<String> wifilist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySystemSetupBinding.inflate(getLayoutInflater());
        systems = new ArrayAdapter<>(this, R.layout.item, list);
        crop = new ArrayAdapter<>(this, R.layout.item, CropData.croplist);
        area = new ArrayAdapter<>(this, R.layout.item, areas);
        wifilist = new ArrayList<>();

        binding.system.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
        binding.cropField.setAdapter(crop);
        binding.areaField.setAdapter(area);

        binding.add.setOnClickListener(v -> {
            wifiPassword = binding.wifiPassField.getText().toString();
            binding.loading.setVisibility(View.VISIBLE);
            String url = "http://192.168.4.1/wifi_cred/?ssid="+wifiSSID+"&pass="+wifiPassword;
            Log.e("URL:", url);
            Request getRequest = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(getRequest).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        if(responseData.equals("ok")){
                            runOnUiThread(()->Toast.makeText(getApplicationContext(),"WiFi Authentication done",Toast.LENGTH_SHORT).show());
                        }else{
                            runOnUiThread(()->Toast.makeText(getApplicationContext(),"WiFi Authentication failed",Toast.LENGTH_SHORT).show());
                        }
                    }
                    runOnUiThread(()->binding.loading.setVisibility(View.GONE));
                }
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(()->{
                        Toast.makeText(getApplicationContext(),"Error in connection",Toast.LENGTH_SHORT).show();
                        binding.loading.setVisibility(View.GONE);
                    });
                    Log.e("WiFi Authentication","call :" + call);
                    Log.e("WiFi Authentication","e :" + e);
                }
            });

            String url2 = "http://192.168.4.1/thresholds/?ph="+String.valueOf(ph)+"&wcnt="+String.valueOf(rainfall);
            Log.e("URL:", url2);
            Request getRequest2 = new Request.Builder()
                    .url(url2)
                    .build();
            client.newCall(getRequest2).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        if(responseData.equals("ok")){
                            runOnUiThread(()->Toast.makeText(getApplicationContext(),"Threshold setting done",Toast.LENGTH_SHORT).show());
                        }else{
                            runOnUiThread(()->Toast.makeText(getApplicationContext(),"Threshold setting failed",Toast.LENGTH_SHORT).show());
                        }
                    }
                    runOnUiThread(()->{
                        binding.loading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Connect to the network", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        FirebaseDatabase fbdo = FirebaseDatabase.getInstance();
                        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                        String mail = sh.getString("mail", "");
                        mail = mail.replace("@", "at");
                        mail = mail.replace(".","");
                        String location = (binding.nameField.getText()).toString();
                        fbdo.getReference("/"+espSSID+"_loc").setValue(location);
                        String finalMail = mail;
                        String finalMail1 = mail;
                        Executors.newScheduledThreadPool(1).execute(()->{
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            fbdo.getReference("/"+espSSID+"_systems").addValueEventListener(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        fbdo.getReference("/"+ finalMail +"_systems").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()){
                                                            ArrayList<String> systems = new ArrayList<>();
                                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                systems.add(ds.getValue(String.class));
                                                            }
                                                            systems.add(espSSID);
                                                            fbdo.getReference("/"+ finalMail +"_systems/").setValue(systems);
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                }
                                        );
                                        fbdo.getReference("/"+ finalMail +"_systems/").setValue(espSSID);
                                    }else{
                                        ArrayList<String> systems = new ArrayList<>();
                                        systems.add(espSSID);
                                        fbdo.getReference("/"+finalMail+"_systems/").setValue(espSSID);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    runOnUiThread(()->Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                                }
                            });
                        });
                        finish();
                    });
                }
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(()->{
                        Toast.makeText(getApplicationContext(),"Error in connection",Toast.LENGTH_SHORT).show();
                        binding.loading.setVisibility(View.GONE);
                    });

                }
            });
        });
        binding.cancel.setOnClickListener(v -> {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            finish();
        });


        binding.system.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });

        binding.cropField.setOnItemClickListener(((parent, view, position, id) -> {
            double phValue = CropData.ph[position];
            double rainfallValue = CropData.wa[position];
            ph = (float) phValue;
            rainfall = (float) rainfallValue;
            String p = "pH Estimated Threshold: " + String.valueOf(phValue);
            binding.ph.setText(p);
            String r = "Rainfall Estimated Threshold: " + String.valueOf(rainfallValue) + "mm";
            binding.rainfall.setText(r);
        }));

        binding.wifiField.setOnItemClickListener(((parent, view, position, id) -> {
            wifiSSID = (String) parent.getAdapter().getItem(position);
        }));

        setContentView(binding.getRoot());
    }

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (Objects.equals(intent.getAction(), WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SystemSetup.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 87);
                    return;
                }
                List<ScanResult> mScanResults = mWifiManager.getScanResults();
                wifilist.clear();
                for (ScanResult mScanResult : mScanResults) {
                    wifilist.add(mScanResult.SSID);
                    Log.e(TAG, "Scan Result: " + mScanResult);
                    if(mScanResult.SSID.contains("ESP")){
                        espSSID = mScanResult.SSID;
                    }
                }
                runOnUiThread(() -> {
                    binding.wifiField.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item, wifilist));
                });
            }
        }
    };

    private void pushNewSystem(String esp, String wifi, String wifiPassword, float ph, float rain) {
        if (Objects.equals(connectWifi(esp), "connection successful")) {
            Executors.newScheduledThreadPool(1).execute(() -> {
                Request.Builder builder = new Request.Builder();
                String url = "https://";
                Request request = builder.build();
            });
        } else {
            runOnUiThread(() -> {
                Toast.makeText(SystemSetup.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            });
        }
    }

    String connectWifi(String ssid) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", "123456789");
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        //remember id
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);

        boolean isConnectionSuccessful = wifiManager.reconnect();

        if (isConnectionSuccessful) {
            return "connection successful";
        } else {
            return "invalid credential";
        }
    }
}