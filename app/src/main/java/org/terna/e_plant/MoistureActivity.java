package org.terna.e_plant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.kyleduo.switchbutton.SwitchButton;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MoistureActivity extends AppCompatActivity {

    WaveLoadingView waveLoadingView;
    VerticalSeekBar seekBar;
    TextView moistureLevel,setMoisture,motorStatus;
    SwitchButton toggel;
    DatabaseReference databaseReference;
    int p,i=0,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moisture);



        databaseReference = FirebaseDatabase.getInstance().getReference();
        waveLoadingView = (WaveLoadingView)findViewById(R.id.wave_loading_view);
        seekBar = (VerticalSeekBar)findViewById(R.id.custom_seekbar);
        moistureLevel =(TextView)findViewById(R.id.moistureLevel);
        setMoisture =(TextView)findViewById(R.id.setMoisture);
        motorStatus =(TextView)findViewById(R.id.motorStatus);
        toggel =(SwitchButton) findViewById(R.id.toggel);



        seekBar.setProgress(60);
        waveLoadingView.setProgressValue(p);
        moistureLevel.setText("60%");
        setMoisture.setText("Set Moisture Level : "+p+"%");



        toggel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(toggel.isChecked())
                {
                    databaseReference.child("Machine").child("MotorStatus").setValue("1");
                    motorStatus.setText("Running");
                }
                else{
                    databaseReference.child("Machine").child("MotorStatus").setValue("0");
                    motorStatus.setText("Stopped");
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String checked = dataSnapshot.child("Machine").child("MotorStatus").getValue().toString();

                s = Integer.parseInt(dataSnapshot.child("Machine").child("MoistureLevel").getValue().toString());
                i = Integer.parseInt(dataSnapshot.child("Machine").child("WaterLevel").getValue().toString());
                moistureLevel.setText(""+i+"%");

                waveLoadingView.setProgressValue(i);

                if(i==s)
                {
                    databaseReference.child("SetMoistureLevel").setValue(0);
                    seekBar.setProgress(0);
                }

                if(checked.equals("1"))
                {
                    toggel.setChecked(true);
                    motorStatus.setText("Running");
                }
                else{
                    toggel.setChecked(false);
                    motorStatus.setText("Stopped");
                }

                seekBar.setProgress(s);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                p = seekBar.getProgress();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


                databaseReference.child("Machine").child("MoistureLevel").setValue(p);

                setMoisture.setText("Set Moisture Level : "+p+"%");



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {





                seekBar.setProgress(p);

            }
        });
    }

}
