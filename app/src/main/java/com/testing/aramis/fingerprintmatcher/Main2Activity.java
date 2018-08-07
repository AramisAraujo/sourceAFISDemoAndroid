package com.testing.aramis.fingerprintmatcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.testing.aramis.sourceafis.FingerprintMatcher;
import com.testing.aramis.sourceafis.FingerprintTemplate;

import java.io.ByteArrayOutputStream;

public class Main2Activity extends AppCompatActivity {

    public static int PICK_IMAGE_PROBE = 1;
    public static int PICK_IMAGE_CANDIDATE = 2;


    private byte[] probeImage;
    private byte[] candidateImage;

    private FingerprintTemplate probeTemplate;
    private FingerprintTemplate candidateTemplate;

    private FingerprintMatcher fm = new FingerprintMatcher();
    private TextView tview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button mClickButtonProbe = (Button) findViewById(R.id.buttonProbe);
        Button mClickButtonCandidate = (Button) findViewById(R.id.buttonCandidate);
        Button mClickButtonCompare = (Button) findViewById(R.id.buttonCompare);
        this.tview = (TextView) findViewById(R.id.resultsView);

        mClickButtonProbe.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pickImage(PICK_IMAGE_PROBE);
            }
        });

        mClickButtonCandidate.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pickImage(PICK_IMAGE_CANDIDATE);
            }
        });

        mClickButtonCompare.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                compareTemplates();
            }
        });

    }

    public void compareTemplates(){

        long beginTime = System.currentTimeMillis();

        double score = fm.index(this.candidateTemplate).match(this.candidateTemplate);

        long endTime = System.currentTimeMillis();

        long elapsed = (endTime - beginTime);

        TextView tview = (TextView) findViewById(R.id.resultsView);

        tview.append("\nMatch Fingerprints Score: " + Double.toString(score)+
                " in "+ elapsed+" ms.");

    }


    public void pickImage(int destination){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image File");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, destination);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE_PROBE){
            if(resultCode == RESULT_OK) {
                Uri uri = data.getData();

                try {
                    TextView tview = (TextView) findViewById(R.id.resultsView);

                    tview.append("\nProbe Image Selected");

                    long selectedTime = System.currentTimeMillis();

                    Bitmap probe = MediaStore.Images.Media
                            .getBitmap(this.getContentResolver(), uri);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    probe.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    this.probeImage = stream.toByteArray();
                    stream.reset();


                    this.probeTemplate = new FingerprintTemplate()
                            .dpi(500).create(this.probeImage);

                    long templateCreatedTime = System.currentTimeMillis();

                    long elapsed = (templateCreatedTime - selectedTime);

                    tview.append("\nProbe Template Created in: "+elapsed+ " ms.");

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        else if(requestCode == PICK_IMAGE_CANDIDATE){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();

                try {
                    TextView tview = (TextView) findViewById(R.id.resultsView);

                    tview.append("\nCandidate Image Selected");

                    long selectedTime = System.currentTimeMillis();

                    Bitmap candidate = MediaStore.Images.Media
                            .getBitmap(this.getContentResolver(), uri);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    candidate.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    this.candidateImage = stream.toByteArray();
                    stream.reset();


                    this.candidateTemplate = new FingerprintTemplate()
                            .dpi(500).create(this.candidateImage);

                    long templateCreatedTime = System.currentTimeMillis();

                    long elapsed = (templateCreatedTime - selectedTime);

                    tview.append("\nCandidate Template Created in: "+elapsed+ " ms.");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
