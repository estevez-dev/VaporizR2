package yehor.vialov.vaporizr2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    Button btnConnect;
    OutputStream btOutStream;
    Boolean btConnected = false;
    BluetoothSocket btSocket = null;
    SeekBar motorA;
    SeekBar motorB;
    TextView txtA;
    TextView txtB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = (Button)findViewById(R.id.btnConnect);

        motorA = (SeekBar)findViewById(R.id.motorA);
        motorA.setProgress(3);
        motorA.setMax(6);
        motorB = (SeekBar)findViewById(R.id.motorB);
        motorB.setProgress(3);
        motorB.setMax(6);

        txtA = (TextView)findViewById(R.id.txtA);
        txtB = (TextView)findViewById(R.id.txtB);

        motorA.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    String progress = "";
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progresValue, boolean fromUser) {
                        progress = Integer.toString(progresValue*85+1000);
                        txtA.setText(progress);
                        try {
                            if (btOutStream != null)
                                btOutStream.write(progress.getBytes());
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Do something here,
                        //if you want to do anything at the start of
                        // touching the seekbar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress(3);

                    }
        });

        motorB.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    String progress = "";
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progresValue, boolean fromUser) {
                        progress = Integer.toString(progresValue*85);
                        txtB.setText(progress);
                        try {
                            if (btOutStream != null)
                                btOutStream.write(progress.getBytes());
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Do something here,
                        //if you want to do anything at the start of
                        // touching the seekbar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress(3);

                    }
                });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!btConnected) {
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    if (adapter == null) {
                        // Device does not support Bluetooth
                        finish(); //exit
                    }

                    if (!adapter.isEnabled()) {
                        //make sure the device's bluetooth is enabled
                        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
                    }

                    final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID for serial connection
                    String mac = "98:D3:31:F5:2D:2F"; //my laptop's mac adress
                    BluetoothDevice device = adapter.getRemoteDevice(mac); //get remote device by mac, we assume these two devices are already paired


                    // Get a BluetoothSocket to connect with the given BluetoothDevice
                    btSocket = null;
                    btOutStream = null;
                    try {
                        btSocket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        btSocket.connect();
                        btOutStream = btSocket.getOutputStream();
                        btConnected = true;
                        btnConnect.setText("Disconnect");
                        //now you can use out to send output via out.write
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        btOutStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        btSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btConnected = false;
                    btnConnect.setText("Connect");
                }
            }
        });
    }
}
