package com.example.firebasesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {


    private static final String TAG="MainActivity";
    EditText thoughts,title,stitle,sthoughts;
    Button button,rcButton,update,delete;
    TextView rcTitle,rcThoghts,secondData;


    private String t1,t2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //for, data retrive
    private DocumentReference retriveData = db.collection("Journa").document("First Thoughts");
    private DocumentReference secondRetrive = db.collection("Second").document("Second doc");

    public static final String KEY_TITLE = "Title data";
    public static final String KEY_THOUGHTS = "Thoughts data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        thoughts = findViewById(R.id.thoughts);
        secondData = findViewById(R.id.seconde_data);

        stitle = findViewById(R.id.second);
        sthoughts = findViewById(R.id.sthoughts);

        rcButton = findViewById(R.id.retrive);
        rcTitle = findViewById(R.id.rc_title);
        rcThoghts = findViewById(R.id.rc_thooughts);

        button = findViewById(R.id.button);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t1 = title.getText().toString().trim();
                t2 = thoughts.getText().toString().trim();

                String s1 = sthoughts.getText().toString().trim();
                String s2 = stitle.getText().toString().trim();

                HashMap<String,Object> data = new HashMap<>();

                data.put(KEY_TITLE,t1);
                data.put(KEY_THOUGHTS,t2);

                HashMap<String,Object> data2 = new HashMap<>();
                data2.put("SECOND_TITLE",s2);
                data2.put("SECOND_THOUGHTS",s1);

                db.collection("Journa")
                        .document("First Thoughts")
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(MainActivity.this, "Successful...", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG,e.toString());

                            }
                        });

                db.collection("Second")
                        .document("Second doc")
                        .set(data2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SECOND",e.toString());
                            }
                        });

            }
        });


        rcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retriveData.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {
                            String titleRetrive = documentSnapshot.getString(KEY_TITLE);
                            String thoughtsRetrive = documentSnapshot.getString(KEY_THOUGHTS);

                            rcTitle.setText(titleRetrive);
                            rcThoghts.setText(thoughtsRetrive);

                            rcTitle.setTextSize(25);
                            rcThoghts.setTextSize(30);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Data not found!", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG,e.toString());

                            }
                        });

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String t1 = title.getText().toString().trim();
                String t2 = thoughts.getText().toString().trim();

                Map<String,Object> dataUpdate = new HashMap<>();
                dataUpdate.put(KEY_TITLE,t1);
                dataUpdate.put(KEY_THOUGHTS,t2);

                retriveData.update(dataUpdate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "updated!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "failed to update!", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                Map<String,Object> data = new HashMap<>();
//                data.put(KEY_THOUGHTS,FieldValue.delete());
//                retriveData.update(data);


//                shortcut
             retriveData.update(KEY_THOUGHTS, FieldValue.delete());

//              secondRetrive.update(KEY_THOUGHTS,FieldValue.delete());


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        retriveData.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null)
                {
                    Toast.makeText(MainActivity.this, "Something went to wrong!", Toast.LENGTH_SHORT).show();
                }

                if ( documentSnapshot != null && documentSnapshot.exists())
                {
                    String getTitle = documentSnapshot.getString(KEY_TITLE);
                    String getThought = documentSnapshot.getString(KEY_THOUGHTS);

                    rcTitle.setText(getTitle);
                    rcThoghts.setText(getThought);

                    rcTitle.setTextSize(26);
                    rcThoghts.setTextSize(28);
                }


            }
        });


    }
}
