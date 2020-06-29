package com.example.securex.filesecurity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codekidlabs.storagechooser.StorageChooser;
import com.example.securex.R;
import com.example.securex.filesecurity.Utils.Encryptor;
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

public class FormatEncryption extends AppCompatActivity {
    private String FILE_NAME_DEC;
    //diaolog box attributes
    Button btn_ok,btn_pick_file,btn_location;
    TextView txt_file,txt_location,txt_passwordConfirm;
    EditText txt_file_name,txt_password,txt_extension;
    CheckBox delete_box;

    //formatencryptor attribute
    Button btn_enc,btn_dec;
    String encSource;
    InputStream encInputStream,inputStream;
    File encDir,decDir;
    Dialog enc_dialog,dec_dialog;
    private String FILE_NAME_ENC="Enc";
    String my_key1,my_key2;
    String defaultKey="encryptK";
    String defaultKey2="encryptS";
    String my_spec_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format_encryption);
        btn_enc=(Button)findViewById(R.id.btn_encrypt);
        btn_dec=(Button)findViewById(R.id.btn_decrypt);
        //dialog box initiate
        enc_dialog=new Dialog(this);
        dec_dialog=new Dialog(this);

        //runtime permission check
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
                        btn_enc.setEnabled(true);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(FormatEncryption.this,"You must enable permission",Toast.LENGTH_SHORT).show();

                    }
                }).check();


        //format encryption
        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputStream!=null && encDir!=null){
                    InputStream is= inputStream;
                    File outputFileEnc=new File(encDir,FILE_NAME_ENC);

                    try {
                        Encryptor.encryptToFile(my_key1,my_spec_key,is,new FileOutputStream(outputFileEnc));
                        Toast.makeText(FormatEncryption.this,"Ecrypted!!",Toast.LENGTH_SHORT).show();
                        encDir=null;
                        btn_dec.setEnabled(true);
                        inputStream=null;
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



        //format decryption
        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encInputStream!=null && decDir!=null){
                    try{
                        File outputFileDec = new File(decDir,FILE_NAME_DEC);
                        Encryptor.decryptToFile(my_key2,my_spec_key,encInputStream,new FileOutputStream(outputFileDec));
                        Toast.makeText(FormatEncryption.this, "Decrypted with Given Password!", Toast.LENGTH_SHORT).show();
                        //deletion of the file
                        if(delete_box.isChecked()){
                            outputFileDec.delete();
                        }
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
                    }

                }else{
                    showDecPopup();
                }
            }
        });


    }
    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    //select any file to encrypt
    public void showPickFile(){
        //Initialize dialog
        StorageChooser chooser = new StorageChooser.Builder()
                // Specify context of the dialog
                .withActivity(FormatEncryption.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .build();

        //Handle what should happend when the user selects the directory !
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                File pickFile=new File(path);
                try {
                    inputStream=new FileInputStream(pickFile);
                    txt_file.setText(path);
                    Toast.makeText(FormatEncryption.this, "Your file is : " + path, Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        // Display File Picker whenever you need to !
        chooser.show();
    }


    //get Directory of the saving place
    public void ShowDirectoryPicker(final String type){
        // Initialize dialog
        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(FormatEncryption.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();
        //Retrieve the selected path by the user and show in a toast !
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                if(type=="enc"){
                    encDir=new File(path);}
                else{
                    decDir=new File(path);
                }
                txt_location.setText(path);
                Toast.makeText(FormatEncryption.this, "The selected path is : " + path, Toast.LENGTH_SHORT).show();
            }
        });
        //Display File Picker !
        chooser.show();
    }

    //encryption form popup
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
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encDir!=null && inputStream!=null){
                    FILE_NAME_ENC=txt_file_name.getText().toString()+"Encrypted";
                    if(txt_password.getText().toString().length()==8){
                        if(txt_password.getText().toString().equals(txt_passwordConfirm.getText().toString())) {
                            my_key1 = txt_password.getText().toString()+defaultKey;
                            my_spec_key=txt_password.getText().toString()+defaultKey2;
                            enc_dialog.dismiss();
                        }else {
                            Toast.makeText(FormatEncryption.this,"Confirmation Password Didn't Match",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(FormatEncryption.this,"Password Should Have 8 Characters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(FormatEncryption.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FormatEncryption.this,"Select an Image to Encrypt!",Toast.LENGTH_SHORT).show();
                showPickFile();

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FormatEncryption.this,"Select Location to Save Encrypted File!!",Toast.LENGTH_SHORT).show();
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
        txt_extension=(EditText)dec_dialog.findViewById(R.id.txt_extension);
        txt_extension.setVisibility(View.VISIBLE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encInputStream!=null && decDir!=null && txt_extension.getText().toString()!="" ){
                    FILE_NAME_DEC=txt_file_name.getText().toString()+"."+txt_extension.getText().toString();
                    if(txt_password.getText().toString().length()==8){
                        my_key2=txt_password.getText().toString()+defaultKey;
                        my_spec_key=txt_password.getText().toString()+defaultKey2;
                        dec_dialog.dismiss();
                    }else{
                        Toast.makeText(FormatEncryption.this,"Password Should Have 8 Characters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(FormatEncryption.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(Intent.createChooser(intent,"Pick an Encrypted Image"),2);
                Toast.makeText(FormatEncryption.this,"Select a Encrypted Any File",Toast.LENGTH_SHORT).show();

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FormatEncryption.this,"Select Location to Save Decrypted File!!",Toast.LENGTH_SHORT).show();
                ShowDirectoryPicker("dec");
            }
        });
        dec_dialog.show();
    }

}
