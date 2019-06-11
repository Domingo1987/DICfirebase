package dic.firebase;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Localización
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private TextView mensajeTextView;
    private EditText mensajeEditText;

    //Localización
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView longitud;
    private TextView latitud;


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mensajeRef = ref.child("mensaje");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mensajeTextView = findViewById(R.id.mensajeTextView);
        mensajeEditText = findViewById(R.id.mensajeEditText);

        //Localización
        googleApiClient = new GoogleApiClient.Builder(  this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        latitud = (TextView) findViewById(R.id.latitudTextView);
        longitud = (TextView) findViewById(R.id.longitudTextView);
        //Localización
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Localización
        googleApiClient.connect();

        mensajeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                mensajeTextView.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void modificar(View view) {
        String mensaje = mensajeEditText.getText().toString();
        mensajeRef.setValue(mensaje);
        mensajeEditText.setText("");
    }


    @Override
    protected void onStop() {
        //Localización
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
        Log.e("MainActivity", "Error al conectar:" + connectionResult.getErrorCode() );
    }

    @Override
    public void onConnectionSuspended(int i){
        Log.e("MainActivity", "Conección suspendida" );
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        int permiso = PermissionChecker.checkSelfPermission(this, ACCESS_FINE_LOCATION);



        if (permiso != PermissionChecker.PERMISSION_GRANTED) {
            Toast.makeText(this,"La aplicación requiere acceso a su localización mediante GPS.",Toast.LENGTH_LONG).show();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitud.setText(String.valueOf(location.getLatitude()));
                                longitud.setText(String.valueOf(location.getLongitude()));

                            }
                        }
                    });
        }
    }
}
