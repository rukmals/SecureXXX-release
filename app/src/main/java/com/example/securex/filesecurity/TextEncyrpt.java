package com.example.securex.filesecurity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.codekidlabs.storagechooser.StorageChooser;
import com.example.securex.R;
import com.example.securex.filesecurity.Utils.Encryptor;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class TextEncyrpt extends AppCompatActivity {

    //textPad
    Button btn_ok_text,btn_write;
    ConstraintLayout textPad;
    private String FILE_NAME_DEC ="DecryptedText.txt";
    TextInputLayout text;


    //dialog box
    Button btn_ok,btn_pick_file,btn_location;
    TextView txt_file,txt_location;
    EditText txt_file_name,txt_password,txt_passwordConfirm;
    CheckBox delete_box,deleteSource;
    Dialog enc_dialog,dec_dialog;


    //encrypt/decrypt
    Button btn_enc,btn_dec;
    InputStream inputStream,encInputStream;
    File outputFileDec;
    File encDir,decDir;
    private String FILE_NAME_ENC="Enc";
    String my_key1,my_key2;
    File textFile;
    String defaultKey="encryptK";
    String defaultKey2="encryptS";
    String my_spec_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_encyrpt);

        //Text Pad
        textPad=(ConstraintLayout)findViewById(R.id.textPad);

        //dialog box
        enc_dialog=new Dialog(this);
        dec_dialog=new Dialog(this);
        text=(TextInputLayout)findViewById(R.id.textInput);
        btn_ok_text=(Button)findViewById(R.id.btn_ok_text);
        btn_write=(Button)findViewById(R.id.btn_write);

        //encrypt
        btn_enc=(Button)findViewById(R.id.btn_encrypt);
        btn_dec=(Button)findViewById(R.id.btn_decrypt);


        btn_ok_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setEnabled(false);
                textFile=new File(Environment.getExternalStorageDirectory(),"Text.txt");
                try {
                    FileOutputStream textOut=new FileOutputStream(textFile);
                    textOut.write(text.getEditText().getText().toString().getBytes());
                    inputStream = new FileInputStream(Environment.getExternalStorageDirectory()+File.separator+"Text.txt");
                    textOut.close();
                    Toast.makeText(TextEncyrpt.this,"Click Encrpyt to Encrypt Text",Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setEnabled(true);
                Toast.makeText(TextEncyrpt.this,"Edit Text and Click Ok",Toast.LENGTH_SHORT).show();
            }
        });
        Dexter.withActivity(this)
                .withPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                })
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        btn_dec.setEnabled(true);
                        btn_enc.setEnabled(true);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(TextEncyrpt.this,"You must enable permission",Toast.LENGTH_SHORT).show();
                    }
                }).check();

        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputStream is= inputStream;
                if(inputStream!=null&& encDir!=null) {

                    try {
                        File outputFileEnc=new File(encDir,FILE_NAME_ENC);
                        Encryptor.encryptToFile(my_key1, my_spec_key, is, new FileOutputStream(outputFileEnc));
                        textFile.delete();
                        Toast.makeText(TextEncyrpt.this, "Ecrypted!!", Toast.LENGTH_SHORT).show();
                        btn_dec.setEnabled(true);
                        inputStream=null;
                        encDir=null;
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }}else{
                    showEncPopup();
                }


            }
        });

        //decryption
        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //File encFile=new File(myDir,FILE_NAME_ENC);
                if(encInputStream!=null && decDir!=null){
                    try{
                        outputFileDec = new File(decDir,FILE_NAME_DEC);
                        Encryptor.decryptToFile(my_key2,my_spec_key,encInputStream,new FileOutputStream(outputFileDec));
                        Toast.makeText(TextEncyrpt.this, "Decrypted with Given Password!", Toast.LENGTH_SHORT).show();
                        btn_enc.setEnabled(true);
                        encInputStream=null;
                        decDir=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    }}else{
                    showDecPopup();
                }

            }
        });

    }

    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                inputStream = getContentResolver().openInputStream(data.getData());
                txt_file.setText(data.getData().getPath());
                btn_dec.setEnabled(false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            try {
                encInputStream = getContentResolver().openInputStream(data.getData());
                btn_enc.setEnabled(false);
                txt_file.setText(data.getData().getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void ShowDirectoryPicker(final String type){
        // Initialize dialog
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(TextEncyrpt.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();
        // Retrieve the selected path by the user and show in a toast !
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                if(type=="enc"){
                    encDir=new File(path);}
                else{
                    decDir=new File(path);
                }
                txt_location.setText(path);
                Toast.makeText(TextEncyrpt.this, "The selected path is : " + path, Toast.LENGTH_SHORT).show();
            }
        });
        // Display File Picker !
        chooser.show();
    }

    //enc popup
    private void showEncPopup(){

        enc_dialog.setContentView(R.layout.enc_layout);
        btn_ok=(Button)enc_dialog.findViewById(R.id.btn_ok);
        btn_pick_file=(Button)enc_dialog.findViewById(R.id.btn_pick_file);
        btn_location=(Button)enc_dialog.findViewById(R.id.btn_location);
        txt_file=(TextView)enc_dialog.findViewById(R.id.txt_file);
        txt_location=(TextView)enc_dialog.findViewById(R.id.txt_location);
        txt_file_name=(EditText)enc_dialog.findViewById(R.id.txt_file_name);
        txt_password=(EditText)enc_dialog.findViewById(R.id.txt_password);
        txt_passwordConfirm=(EditText)enc_dialog.findViewById(R.id.txt_passwordConfirm);
        btn_pick_file.setVisibility(View.INVISIBLE);
        txt_file.setVisibility(View.INVISIBLE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encDir!=null && inputStream!=null){
                    FILE_NAME_ENC=txt_file_name.getText().toString()+"Encrypted";
                    if(txt_password.getText().toString().length()==8){
                        if(txt_password.getText().toString().equals(txt_passwordConfirm.getText().toString())){
                            my_key1=txt_password.getText().toString()+defaultKey;
                            my_spec_key=txt_password.getText().toString()+defaultKey2;
                            enc_dialog.dismiss();}
                        else{
                            Toast.makeText(TextEncyrpt.this,"Confirmation Password Didn't Match",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(TextEncyrpt.this,"Password should have 8 characters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(TextEncyrpt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TextEncyrpt.this,"Select Location to Save Encrypted File!!",Toast.LENGTH_SHORT).show();
                ShowDirectoryPicker("enc");
            }
        });
        enc_dialog.show();
    }

    //decryption form popup
    private void showDecPopup(){
        dec_dialog.setContentView(R.layout.dec_layout);
        btn_ok=(Button)dec_dialog.findViewById(R.id.btn_ok);
        btn_pick_file=(Button)dec_dialog.findViewById(R.id.btn_pick_file);
        btn_location=(Button)dec_dialog.findViewById(R.id.btn_location);
        txt_file=(TextView)dec_dialog.findViewById(R.id.txt_file);
        txt_location=(TextView)dec_dialog.findViewById(R.id.txt_location);
        txt_file_name=(EditText)dec_dialog.findViewById(R.id.txt_file_name);
        txt_password=(EditText)dec_dialog.findViewById(R.id.txt_password);
        delete_box=(CheckBox)dec_dialog.findViewById(R.id.delete);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encInputStream!=null && decDir!=null){
                    FILE_NAME_DEC=txt_file_name.getText().toString()+".txt";
                    if(txt_password.getText().toString().length()==8){
                        my_key2=txt_password.getText().toString()+defaultKey;
                        my_spec_key=txt_password.getText().toString()+defaultKey2;
                        dec_dialog.dismiss();
                    }else{
                        Toast.makeText(TextEncyrpt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(TextEncyrpt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(Intent.createChooser(intent,"Pick an Encrypted Text"),2);
                Toast.makeText(TextEncyrpt.this,"Select a Encrypted Text",Toast.LENGTH_SHORT).show();

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TextEncyrpt.this,"Select Location to Save Decrypted File!!",Toast.LENGTH_SHORT).show();
                ShowDirectoryPicker("dec");
            }
        });
        dec_dialog.show();
    }
}
