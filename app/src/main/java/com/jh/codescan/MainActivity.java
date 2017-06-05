package com.jh.codescan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;
import com.utils.CommonUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG= "MainActivity";

    Button openQrCodeScan;
    EditText text;
    Button CreateQrCode;
    ImageView QrCode;
    TextView qrCodeText;

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();

    }

    private void viewInit(){
        openQrCodeScan=(Button)findViewById(R.id.openQrCodeScan);
        text=(EditText)findViewById(R.id.text);
        CreateQrCode=(Button)findViewById(R.id.CreateQrCode);
        QrCode=(ImageView)findViewById(R.id.QrCode);
        qrCodeText=(TextView)findViewById(R.id.qrCodeText);

        openQrCodeScan.setOnClickListener(this);

        CreateQrCode.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openQrCodeScan:
                if (CommonUtils.isCameraCanUse()){
                    Intent intent =new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent,REQUEST_CODE);
                }else {
                    Toast.makeText(this, "请打开此应用的摄像头权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.CreateQrCode:
                try {
                    String str =text.getText().toString().trim();
                    if(str !=null && !str.equals("")){
                        Bitmap bitmap = EncodingHandler.createQRCode(str,500);
                        if (bitmap !=null){
                            Toast.makeText(this,"二维码生成成功！",Toast.LENGTH_SHORT).show();
                            QrCode.setImageBitmap(bitmap);
                        }
                    }else {
                        Toast.makeText(this,"文本信息不能为空！",Toast.LENGTH_SHORT).show();
                    }
                }catch (WriterException e){
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bundle bundle=data.getExtras();
            String scanResult=bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);

            qrCodeText.setText(scanResult);
        }
    }
}
