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

import java.io.DataInputStream;
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

import ir.mahdi.mzip.zip.ZipArchive;

public class FolderEncrypt extends AppCompatActivity {

    Button btn_ok,btn_pick_file,btn_location;
    TextView txt_file,txt_location;
    EditText txt_file_name,txt_password,txt_passwordConfirm;
    CheckBox delete_box;
    Dialog enc_dialog,dec_dialog;

    //encryption/ decryption attributes
    Button btn_enc,btn_dec;
    File fileZip;
    ZipArchive zipArchive = new ZipArchive();
    InputStream inputStream,encInputStream;
    File encDir,decDir;
    String encDirectory,decDirectory;
    private String FILE_NAME_DEC ="DecryptedFile.zip";
    private String FILE_NAME_ENC="Enc";
    String my_key="jdwztahttruvphdm";
    String my_spec_key="risxjdoxqfhatuph";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_encrypt);

        btn_enc=(Button)findViewById(R.id.btn_encrypt);
        btn_dec=(Button)findViewById(R.id.btn_decrypt);
        //dialog box initiate
        enc_dialog=new Dialog(this);
        dec_dialog=new Dialog(this);
        //permission to access external storage
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
                        Toast.makeText(FolderEncrypt.this,"You must enable permission",Toast.LENGTH_SHORT).show();
                    }
                }).check();

        //encryption
        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream is= inputStream;
                if(inputStream!=null&&encDir!=null){
                    try {
                        File outputFileEnc=new File(encDir,FILE_NAME_ENC);
                        Encryptor.encryptToFile(my_key,my_spec_key,is,new FileOutputStream(outputFileEnc));
                        Toast.makeText(FolderEncrypt.this,"Ecrypted!!",Toast.LENGTH_SHORT).show();
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

                if(encInputStream!=null&&decDir!=null){
                    try{
                        File outputFileDec = new File(decDir,FILE_NAME_DEC);
                        Encryptor.decryptToFile(my_key,my_spec_key,encInputStream,new FileOutputStream(outputFileDec));
                        zipArchive.unzip(decDir+"/"+FILE_NAME_DEC,decDirectory,"");
                        outputFileDec.delete();
                        Toast.makeText(FolderEncrypt.this, "Decrypted", Toast.LENGTH_SHORT).show();
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
    //get Directory of the saving place
    public void ShowDirectoryPicker(final String type){
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(FolderEncrypt.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                if(type=="enc"){
                    encDir=new File(path);
                    txt_location.setText(path);
                    Toast.makeText(FolderEncrypt.this, "The selected path is : " + path, Toast.LENGTH_SHORT).show();}
                else if(type=="dec"){
                    decDir=new File(path);
                    decDirectory=path;
                    txt_location.setText(path);
                    Toast.makeText(FolderEncrypt.this, "The selected path is : " + path, Toast.LENGTH_SHORT).show();
                }else{
                    encDirectory=path;
                    Toast.makeText(FolderEncrypt.this, "Selecting Folder is : " + path, Toast.LENGTH_SHORT).show();
                    txt_file.setText(encDirectory);
                }
            }
        });
        chooser.show();
    }

    //encryption form popup
    private void showEncPopup(){

        enc_dialog.setContentView(R.layout.enc_layout);
        btn_ok=(Button)enc_dialog.findViewById(R.id.btn_ok);
        btn_pick_file=(Button)enc_dialog.findViewById(R.id.btn_pick_file);
        btn_location=(Button)enc_dialog.findViewById(R.id.btn_location);
        txt_file=(TextView)enc_dialog.findViewById(R.id.txt_file);
        txt_file.setText("Select Folder to Encrypt");
        txt_location=(TextView)enc_dialog.findViewById(R.id.txt_location);
        txt_location.setText("Select Location to Save");
        txt_file_name=(EditText)enc_dialog.findViewById(R.id.txt_file_name);
        txt_password=(EditText)enc_dialog.findViewById(R.id.txt_password);
        txt_passwordConfirm=(EditText)enc_dialog.findViewById(R.id.txt_passwordConfirm);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encDirectory!=""){
                    if(txt_password.getText().toString().equals(txt_passwordConfirm.getText().toString())){
                        FILE_NAME_ENC=txt_file_name.getText().toString()+"Enc";
                        zipArchive.zip(encDirectory,encDirectory+"/file.zip","");
                        fileZip = new File(encDirectory+"/file.zip");
                        try {
                            inputStream=new DataInputStream(new FileInputStream(fileZip));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(FolderEncrypt.this, encDirectory+" Converted to Zip and Select", Toast.LENGTH_SHORT).show();
                        enc_dialog.dismiss();
                    }else{
                        Toast.makeText(FolderEncrypt.this, encDirectory+" Confirmation Password Didnt Match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(FolderEncrypt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDirectoryPicker("encDirectory");
            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FolderEncrypt.this,"Select Location to Save Encrypted File!!",Toast.LENGTH_SHORT).show();
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
        txt_file_name.setVisibility(View.INVISIBLE);
        txt_password=(EditText)dec_dialog.findViewById(R.id.txt_password);
        delete_box=(CheckBox)dec_dialog.findViewById(R.id.delete);
        delete_box.setVisibility(View.INVISIBLE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encInputStream!=null && decDir!=null){
                    FILE_NAME_DEC=txt_file_name.getText().toString()+"Folder.zip";
                    dec_dialog.dismiss();
                }
                else{
                    Toast.makeText(FolderEncrypt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(Intent.createChooser(intent,"Pick an Encrypted Folder"),2);
                Toast.makeText(FolderEncrypt.this,"Select a Encrypted Folder",Toast.LENGTH_SHORT).show();

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FolderEncrypt.this,"Select Location to Save Decrypted File!!",Toast.LENGTH_SHORT).show();
                ShowDirectoryPicker("dec");
            }
        });
        dec_dialog.show();
    }
}
