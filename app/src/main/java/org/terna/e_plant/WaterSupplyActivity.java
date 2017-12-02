package org.terna.e_plant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.itangqi.waveloadingview.WaveLoadingView;

public class WaterSupplyActivity extends AppCompatActivity {


    WaveLoadingView waveLoadingView2;
    TextView text1,text3;
    DatabaseReference databaseReference;
    int s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_supply);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        waveLoadingView2 = (WaveLoadingView)(findViewById(R.id.wave_loading_view2));
        text1 = (TextView)(findViewById(R.id.text1));
        text3 = (TextView)(findViewById(R.id.text3));



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                s = Integer.parseInt(dataSnapshot.child("Machine").child("bWaterLevel").getValue().toString());
                waveLoadingView2.setProgressValue(s);

                text1.setText("Current Water Supply : "+s);

                if(s>50)
                {
                    text3.setText("Sufficient Water Supply");
                }
                else if(s>=30)
                {
                    text3.setText("less Water Supply");
                }
                else if(s>=20)
                {
                    text3.setText("very low Water Supply");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

}
