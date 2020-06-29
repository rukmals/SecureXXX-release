package com.example.securex.filesecurity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.codekidlabs.storagechooser.StorageChooser;
import com.example.securex.R;
import com.example.securex.filesecurity.Utils.Encryptor;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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

public class AudioEncrypt extends AppCompatActivity {

    //Music Player attributes
    Button playBtn,btn_finish;
    SeekBar positionBar,volumeBar;
    TextView elapsedTimeLabel,remainingTimeLabel,song_name;
    MediaPlayer player;
    String audioPath;
    CheckBox delete_audio;
    int totalTime;

    //diaolog box attributes
    Button btn_ok,btn_pick_file,btn_location;
    TextView txt_file,txt_location;
    EditText txt_file_name,txt_password,txt_passwordConfirm;
    CheckBox delete_box;
    Dialog enc_dialog,dec_dialog;
    ConstraintLayout audio_player;

    //encryption/decryption attributes
    Button btn_enc,btn_dec;
    ImageView audio1;
    InputStream inputStream,encInputStream;
    File outputFileDec;
    File encDir,decDir;
    private String FILE_NAME_ENC="Encrypted";
    private String FILE_NAME_DEC ="DecryptedAudio.mp3";
    String my_key1,my_key2;
    String defaultKey="encryptK";
    String defaultKey2="encryptS";
    String my_spec_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_encrypt);

        //enc/dec initiate
        btn_enc=(Button)findViewById(R.id.btn_encrypt);
        btn_dec=(Button)findViewById(R.id.btn_decrypt);
        audio1=(ImageView) findViewById(R.id.audio1);
        //dialog box initiate
        enc_dialog=new Dialog(this);
        dec_dialog=new Dialog(this);
        //music player initiate
        playBtn=(Button)findViewById(R.id.playBtn);
        elapsedTimeLabel=(TextView)findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel=(TextView)findViewById(R.id.remainingTimeLabel);
        btn_finish=(Button)findViewById(R.id.btn_finish);
        delete_audio=(CheckBox)findViewById(R.id.delete_audio);
        player = new MediaPlayer();
        audio_player=(ConstraintLayout)findViewById(R.id.audio_player);
        song_name=(TextView)findViewById(R.id.song_name);
        player.setVolume(0.5f,0.5f);
        player.seekTo(0);
        player.setLooping(true);
        volumeBar=(SeekBar)findViewById(R.id.volumeBar);
        //volume bar music player
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum=progress/100f;
                        player.setVolume(volumeNum,volumeNum);
                        if(volumeNum==0){
                            Toast.makeText(AudioEncrypt.this, "Mute", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        //position bar music player
        positionBar=(SeekBar)findViewById(R.id.positionBar);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    player.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //permission to access storage
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
                        Toast.makeText(AudioEncrypt.this,"You must enable permission",Toast.LENGTH_SHORT).show();
                    }
                }).check();

        //encryption
        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputStream is= inputStream;
                if(inputStream!=null&& encDir!=null) {

                    try {
                        audio_player.setVisibility(View.INVISIBLE);
                        audio1.setVisibility(View.VISIBLE);
                        player.reset();
                        File outputFileEnc=new File(encDir,FILE_NAME_ENC);
                        Encryptor.encryptToFile(my_key1, my_spec_key, is, new FileOutputStream(outputFileEnc));
                        Toast.makeText(AudioEncrypt.this, "Ecrypted!!", Toast.LENGTH_SHORT).show();
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

        //deletion of decrypted file
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delete_audio.isChecked() && outputFileDec!=null){
                    outputFileDec.delete();
                    Toast.makeText(AudioEncrypt.this, "OutPut File is Deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AudioEncrypt.this, "Not Selected Decrypted File", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //decryption
        btn_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encInputStream!=null && decDir!=null){
                    try{
                        audio1.setVisibility(View.INVISIBLE);
                        audio_player.setVisibility(View.VISIBLE);
                        player.reset();
                        playBtn.setBackgroundResource(R.drawable.play);

                        totalTime=player.getDuration();
                        outputFileDec = new File(decDir,FILE_NAME_DEC);
                        Encryptor.decryptToFile(my_key2,my_spec_key,encInputStream,new FileOutputStream(outputFileDec));
                        Toast.makeText(AudioEncrypt.this, "Decrypted with Given Password!", Toast.LENGTH_SHORT).show();

                        if(audioPath!=null){
                            try {
                                song_name.setText(FILE_NAME_DEC);
                                player.setDataSource(audioPath + File.separator + FILE_NAME_DEC);
                                player.prepare();
                                positionBar.setMax(player.getDuration());
                                totalTime=player.getDuration();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }}
                        else{
                            Toast.makeText(AudioEncrypt.this, "No Audio File to Play", Toast.LENGTH_SHORT).show();
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
                    }}else{
                    showDecPopup();
                }

            }
        });

        //Thread update postion
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(player!=null){
                    try{
                        Message message=new Message();
                        message.what=player.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }
            }
        }).start();




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
    //get Directory of the saving place
    public void ShowDirectoryPicker(final String type){
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(AudioEncrypt.this)
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
                    audioPath=path;
                }
                txt_location.setText(path);
                Toast.makeText(AudioEncrypt.this, "The selected path is : " + path, Toast.LENGTH_SHORT).show();
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
                if(encDir!=null && inputStream!=null){
                    FILE_NAME_ENC=txt_file_name.getText().toString()+"Encrypted";
                    if(txt_password.getText().toString().length()==8){
                        if(txt_password.getText().toString().equals(txt_passwordConfirm.getText().toString())){
                            my_key1=txt_password.getText().toString()+defaultKey;
                            my_spec_key=txt_password.getText().toString()+defaultKey2;
                            enc_dialog.dismiss();}
                        else{
                            Toast.makeText(AudioEncrypt.this,"Confirmation Password Didn't Match",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(AudioEncrypt.this,"Password should have 8 characters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AudioEncrypt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AudioEncrypt.this,"Select an Audio to Encrypt!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(Intent.createChooser(intent,"Pick an Audio"),1);

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AudioEncrypt.this,"Select Location to Save Encrypted File!!",Toast.LENGTH_SHORT).show();
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
        delete_box.setVisibility(View.INVISIBLE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encInputStream!=null && decDir!=null){
                    FILE_NAME_DEC=txt_file_name.getText().toString()+".mp3";
                    if(txt_password.getText().toString().length()==8){
                        my_key2=txt_password.getText().toString()+defaultKey;
                        my_spec_key=txt_password.getText().toString()+defaultKey2;
                        dec_dialog.dismiss();
                    }else{
                        Toast.makeText(AudioEncrypt.this,"Password Have 8 Characters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AudioEncrypt.this,"Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_pick_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(Intent.createChooser(intent,"Pick an Encrypted Audio"),2);
                Toast.makeText(AudioEncrypt.this,"Select a Encrypted Audio",Toast.LENGTH_SHORT).show();

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AudioEncrypt.this,"Select Location to Save Decrypted File!!",Toast.LENGTH_SHORT).show();
                ShowDirectoryPicker("dec");
            }
        });
        dec_dialog.show();
    }
    public void playBtnClick(View view){
        if(!player.isPlaying()){
            player.start();
            playBtn.setBackgroundResource(R.drawable.pause);
        }else{
            player.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
    private Handler handler=new Handler(){
        public void handleMessage(Message message){
            int currentPosition=message.what;
            positionBar.setProgress(currentPosition);

            String elapsedTime=createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime=createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- "+remainingTime);
        }
    };
    public String createTimeLabel(int time){
        String timeLabel="";
        int min=time/1000/60;
        int sec=time/1000%60;
        timeLabel=min+":";
        if(sec<10)timeLabel+="0";
        timeLabel+=sec;
        return timeLabel;
    }
}
