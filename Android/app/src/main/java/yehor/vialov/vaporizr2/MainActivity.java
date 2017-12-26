package yehor.vialov.vaporizr2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    TextView txtStatus;
    OutputStream btOutStream;
    Boolean btConnected = false;
    BluetoothSocket btSocket = null;
    SeekBar motorA;
    SeekBar motorB;
    TextView txtA;
    TextView txtB;
    private final static int MOTOR_FULL_FORWARD_COMMAND = 10;
    private final static int MOTOR_FULL_BACKWARD_COMMAND = 0;
    private final static int MOTOR_STOP_COMMAND = 5;
    ImageView imEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);

        motorA = (SeekBar)findViewById(R.id.motorA);
        motorA.setProgress(MOTOR_STOP_COMMAND);
        motorA.setMax(MOTOR_FULL_FORWARD_COMMAND);
        motorB = (SeekBar)findViewById(R.id.motorB);
        motorB.setProgress(MOTOR_STOP_COMMAND);
        motorB.setMax(MOTOR_FULL_FORWARD_COMMAND);
        motorA.setEnabled(false);
        motorB.setEnabled(false);
        imEngine = (ImageView)findViewById(R.id.imCE);

        imEngine.setVisibility(View.VISIBLE);
        txtA = (TextView)findViewById(R.id.txtA);
        txtB = (TextView)findViewById(R.id.txtB);
        txtA.setText(Integer.toString(MOTOR_STOP_COMMAND));
        txtB.setText(Integer.toString(MOTOR_STOP_COMMAND));

        motorA.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    String progress = "";
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progresValue, boolean fromUser) {
                        sendCommand();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Do something here,
                        //if you want to do anything at the start of
                        // touching the seekbar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress(MOTOR_STOP_COMMAND);

                    }
        });

        motorB.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    String progress = "";
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progresValue, boolean fromUser) {
                        sendCommand();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Do something here,
                        //if you want to do anything at the start of
                        // touching the seekbar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress(MOTOR_STOP_COMMAND);

                    }
                });

        connectToCar();


    }

    @Override
    protected void onDestroy() {
        disconnectFromCar();
        super.onDestroy();
    }

    private void disconnectFromCar() {
        try {
            btOutStream.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            btSocket.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        btConnected = false;
        motorA.setEnabled(false);
        motorB.setEnabled(false);
        imEngine.setVisibility(View.GONE);
    }

    private void connectToCar() {
        new Thread() {
            public void run() {
                boolean enableBTRequested = false;
                while (btSocket==null || !btSocket.isConnected()) {
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    if (adapter == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtStatus.setText("Bluetooth is not supported");
                                imEngine.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        if (!adapter.isEnabled() && !enableBTRequested) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtStatus.setText("Bluetooth is not enabled. Will retry in 5 sec.");
                                    imEngine.setVisibility(View.VISIBLE);
                                }
                            });
                            //make sure the device's bluetooth is enabled
                            enableBTRequested = true;
                            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
                        } else if (adapter.isEnabled()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtStatus.setText("Connecting...");
                                    imEngine.setVisibility(View.VISIBLE);
                                }
                            });
                            enableBTRequested = false;
                            final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID for serial connection
                            String mac = "98:D3:31:F5:2D:2F"; //my laptop's mac adress
                            BluetoothDevice device = adapter.getRemoteDevice(mac); //get remote device by mac, we assume these two devices are already paired
                            // Get a BluetoothSocket to connect with the given BluetoothDevice
                            btSocket = null;
                            btOutStream = null;
                            try {
                                btSocket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtStatus.setText("Error creating socket.  Will retry in 5 sec.");
                                        imEngine.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                            try {
                                btSocket.connect();
                                btOutStream = btSocket.getOutputStream();

                                btConnected = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtStatus.setText("Connected");
                                        imEngine.setVisibility(View.GONE);
                                        motorA.setEnabled(true);
                                        motorB.setEnabled(true);
                                    }
                                });
                            } catch (Exception e) {
                                final Exception er = e;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtStatus.setText("Connection error. Will retry in 5 sec.");
                                        imEngine.setVisibility(View.VISIBLE);
                                    }

                                });
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void sendCommand() {
        int motorASpeed, motorBSpeed;
        String m1, m2;
        int mAProgress = motorA.getProgress();
        int mBProgress = motorB.getProgress();
        if (mAProgress < 5) {
            m1 = "b";
            motorASpeed = 5 - mAProgress;
        } else {
            m1 = "f";
            motorASpeed = mAProgress - 5;
        }
        if (mBProgress < 5) {
            m2 = "b";
            motorBSpeed = 5 - mBProgress;
        } else {
            m2 = "f";
            motorBSpeed = mBProgress - 5;
        }
        m1 += Integer.toString(motorASpeed);
        m2 += Integer.toString(motorBSpeed);
        txtA.setText(m1);
        txtB.setText(m2);
        try {
            btOutStream.write((m1+m2).getBytes());
        } catch (Exception e) {
            txtStatus.setText("Disconnected");
            disconnectFromCar();
            connectToCar();
        }
    }
}
