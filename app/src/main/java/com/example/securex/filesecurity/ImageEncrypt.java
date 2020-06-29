package com.example.securex.filesecurity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class ImageEncrypt extends AppCompatActivity {

    private String FILE_NAME_DEC ="decryptedImage.png" ;
    //diaolog box attributes
    Button btn_ok,btn_pick_file,btn_location;
    TextView txt_file,txt_location;
    EditText txt_file_name,txt_password,txt_passwordConfirm;
    CheckBox delete_box;

    //imageencryptor attribute
    Button btn_enc,btn_dec;
    String encSource;
    ImageView imageView,image1;
    Bitmap bitmap;
    InputStream encInputStream;
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
        setContentView(R.layout.activity_image_encrypt);
        btn_enc=(Button)findViewById(R.id.btn_encrypt);
        btn_dec=(Button)findViewById(R.id.btn_decrypt);
        imageView = (ImageView)findViewById(R.id.imageView);
        image1 = (ImageView)findViewById(R.id.image1);

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
                        Toast.makeText(ImageEncrypt.this,"You must enable permission",Toast.LENGTH_SHORT).show();

                    }
                }).check();


        //image encryption
        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null && encDir!=null){
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    InputStream is= new ByteArrayInputStream(stream.toByteArray());
                    File outputFileEnc=new File(encDir,FILE_NAME_ENC);

                    try {
                        image1.setVisibility(View.VISIBLE);
                        Encryptor.encryptToFile(my_key1,my_spec_key,is,new FileOutputStream(outputFileEnc));
                        Toast.makeText(ImageEncrypt.this,"Ecrypted!!",Toast.LENGTH_SHORT).show();
                        encDir=null;
                        btn_dec.setEnabled(true);
                        bitmap=null;
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


        //image decryption
        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encInputStream!=null && decDir!=null){
                    try{
                        File outputFileDec = new File(decDir,FILE_NAME_DEC);
                        Encryptor.decryptToFile(my_key2,my_spec_key,encInputStream,new FileOutputStream(outputFileDec));
                        image1.setVisibility(View.INVISIBLE);
                        imageView.setImageURI(Uri.fromFile(outputFileDec));
                        Toast.makeText(ImageEncrypt.this, "Decrypted with Given Password!", Toast.LENGTH_SHORT).show();
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

    //select image to encrypt
    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                encSource=data.getData().getPath();
                btn_dec.setEnabled(false);
                txt_file.setText(data.getData().getPath());
                Toast.makeText(ImageEncrypt.this, "The Selected Image is : " + data.getData().getPath(), Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            try {
                encInputStream = getContentResolver().openInputStream(data.getData());
                txt_file.setText(data.getData().getPath());
                btn_enc.setEnabled(false);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    //get Directory of the saving place
    public void ShowDirectoryPicker(final String type){
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(ImageEncrypt.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .build();
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                if(type=="enc"){
                    encDir=new File(path);}
                else{
                    decDir=new File(path);
                }
                txt_location.setText(path);
                Toast.makeText(ImageEncrypt.this, "The selected path is : " + path, Toast.LENGTH_SHORT).show();
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
        txt_location=(TextView)enc_dialog.findViewById(R.id.txt_location);
        txt_file_name=(EditText)enc_dialog.findViewById(R.id.txt_file_name);
        txt_password=(EditText)enc_dialog.findViewById(R.id.txt_password);
        txt_passwordConfirm=(EditText)enc_dialog.findViewById(R.id.txt_passwordConfirm);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encDir!=null && bitmap!=null){
                    FILE_NAME_ENC=txt_file_name.getText().toString()+"Encrypted";
                    if(txt_password.getText().toString().length()==8){
                        if(txt_password.getText().toString().equals(txt_passwordConfirm.getText().toString())){
                            my_key1=txt_password.getText().toString()+defaultKey;
                            my_spec_key=txt_password.getText().toString()+defaultKey2;
                            enc_dialog.dismiss();}else{
                            Toast.makeText(ImageEncrypt.this,"Confirmation Password Didn't Match",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(ImageEncrypt.this,"Password Should Have 8 Characters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ImageEncrypt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageEncrypt.this,"Select an Image to Encrypt!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Pick an Image"),1);

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageEncrypt.this,"Select Location to Save Encrypted File!!",Toast.LENGTH_SHORT).show();
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
                    FILE_NAME_DEC=txt_file_name.getText().toString()+".png";
                    if(txt_password.getText().toString().length()==8){
                        my_key2=txt_password.getText().toString()+defaultKey;
                        my_spec_key=txt_password.getText().toString()+defaultKey2;
                        dec_dialog.dismiss();
                    }else{
                        Toast.makeText(ImageEncrypt.this,"Password Have 8 Characters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ImageEncrypt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(Intent.createChooser(intent,"Pick an Encrypted Image"),2);
                Toast.makeText(ImageEncrypt.this,"Select a Encrypted Image",Toast.LENGTH_SHORT).show();

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageEncrypt.this,"Select Location to Save Decrypted File!!",Toast.LENGTH_SHORT).show();
                ShowDirectoryPicker("dec");
            }
        });
        dec_dialog.show();
    }
}
