package com.example.ard2and;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends ActionBarActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private final String mac = "20:13:09:13:24:28";
    private final UUID uuid = UUID.randomUUID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enableBluetooth();
        Button button = ((Button)findViewById(R.id.discover_button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.snare);
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mPlayer.release();
                    }
                });
            }
        });

        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableBluetooth();
                fillDevicesList(getApplicationContext());
            }
        });

        customizeTabs();
        enableBluetooth();
        fillDevicesList(getApplicationContext());
    }

    private void fillDevicesList(Context context) {
        ListView listView = (ListView)findViewById(R.id.listView);
        List<String> items = getBtDevices();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.menu_item, items);
        discoverDevices(adapter, context);
        listView.setAdapter(adapter);

    }

    private void discoverDevices(final ArrayAdapter adapter, Context context) {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        List<String> devicesNames = new ArrayList<>();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            System.out.println("REGISTER BROADCAST RECIEVER: ");
            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        System.out.println("DISCOVER IS OVER: " + device.getName());
                        adapter.add(device.getName() + "\n" + device.getAddress());
                        adapter.notifyDataSetChanged();
                    }
                }
            };
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mReceiver, filter);
            mBluetoothAdapter.startDiscovery();
        }
    }

    private List<String> getBtDevices() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        List<String> devicesNames = new ArrayList<>();
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    devicesNames.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
        return devicesNames;
    }

    private void customizeTabs() {
        TabHost tabs = (TabHost)findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab_1);
        spec.setIndicator("BT-устройства");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab_2);
        spec.setIndicator("Звук");
        tabs.addTab(spec);
        tabs.setCurrentTab(0);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        shutDownBluetooth();
        super.onDestroy();
    }

    private void shutDownBluetooth() {
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if(bluetoothAdapter != null){
//            bluetoothAdapter.disable();
//        }
    }

    @Override
    public void onPause(){
        shutDownBluetooth();
        super.onPause();
    }

    @Override
    public void onResume(){
        enableBluetooth();
        super.onResume();
    }

    private void enableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            new AlertDialog.Builder(this)
                    .setTitle("Ошибка")
                    .setMessage("Ваше устройство не поддерживает Bluetooth")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

}
