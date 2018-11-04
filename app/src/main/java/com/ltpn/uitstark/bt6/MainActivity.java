package com.ltpn.uitstark.bt6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.solver.Cache;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
public class MainActivity extends AppCompatActivity {
    private Button btnChange;
    private  Button btnSave;
    private  Button btnCancel;
    private EditText txtName;
    private  EditText txtPhone;
    private  EditText txtEmail;
    private RadioButton radiMale;
    private  RadioButton radiFemale;
    private ImageView imgProfle;
    private int request_code = 1;
    final String interPath = Environment.getDataDirectory().getPath()+"/data/com.ltpn.uitstark.bt6";
    final  String nameFileProfile = "profile.txt";
    private File photoFile;
    private String currentFilePhoto;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        ReadProfile();
        Save();
        ChangeProfilePhoto();
        ExitApp();
    }
    private  void AnhXa()
    {
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnChange = (Button)findViewById(R.id.btnChange);
        btnSave = (Button)findViewById(R.id.btnSave);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtName = (EditText)findViewById(R.id.txtName);
        txtPhone = (EditText)findViewById(R.id.txtPhone);
        radiFemale = (RadioButton)findViewById(R.id.radioFemale);
        radiMale = (RadioButton)findViewById(R.id.radioMale);
        imgProfle = (ImageView)findViewById(R.id.imgProfile);
        radiFemale.setChecked(true);
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = "ProPhoto";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (it.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                it.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(it, request_code);
            }
        }
    }
    private  void ChangeProfilePhoto()
    {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bm;
        if(requestCode==request_code&& resultCode==RESULT_OK&&data!=null)
        {
            imgProfle.setImageURI(Uri.fromFile(photoFile));
            currentFilePhoto = Uri.fromFile(photoFile).toString();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private  void Save()

    {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(getFilesDir(), nameFileProfile);

                    String ten = txtName.getText().toString().trim() ;
                    String email = txtEmail.getText().toString().trim() ;
                    String phone = txtPhone.getText().toString().trim() ;
                    String gengar = radiMale.isChecked() ? "Male\n" : "Female\n";
                    if (ten.length()>0 && email.length()>0 && phone.length()>0 ) {
                        try {
                            ten+="\n";
                            email+="\n";
                            phone+="\n";
                            String path = interPath + nameFileProfile;
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            //  FileOutputStream outputStream = new FileOutputStream(file,true);
                            FileOutputStream outputStream = openFileOutput(nameFileProfile, Context.MODE_PRIVATE);
                            outputStream.write(ten.getBytes(), 0, ten.length());
                            outputStream.write(email.getBytes(), 0, email.length());
                            outputStream.write(phone.getBytes(), 0, phone.length());
                            outputStream.write(gengar.getBytes(), 0, gengar.length());
                            outputStream.write(currentFilePhoto.getBytes(), 0, currentFilePhoto.length());
                            outputStream.close();
                            Toast.makeText(MainActivity.this, "Save Success" , Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Save Fail" , Toast.LENGTH_SHORT).show();
                        }


                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Info is not complete" , Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception t)
                {
                    Toast.makeText(MainActivity.this, "Error" , Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private  void ReadProfile()
    {
        try
        {
            FileInputStream fileInputStream = openFileInput(nameFileProfile);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int i=0;
            while (i<5)
            {
                if(i==0)
                    txtName.setText(bufferedReader.readLine());
                if(i==1)
                    txtEmail.setText(bufferedReader.readLine());
                if(i==2)
                    txtPhone.setText(bufferedReader.readLine());
                if(i==3)
                {
                    if(bufferedReader.readLine().trim().equals("Male"))
                        radiMale.setChecked(true);
                    else
                        radiFemale.setChecked(true);
                }
                if (i==4)
                {
                    String path = bufferedReader.readLine();
                    if(path!=null) {
                        Uri uri = Uri.parse(path);
                        imgProfle.setImageURI(uri);
                    }

                }
                i++;
            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void ExitApp()
    {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
