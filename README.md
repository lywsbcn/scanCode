# Android二维码扫描的简单实现

把压缩包解压到项目中 java文件夹

简介
1.	下载开源项目SimpleZXing,它是在Zxing库的作者为Android写的条码扫描App Barcode Scanner的基础上精简和修改而成的.
项目地址https://github.com/zxing/zxing
2.这里	已经集成好了一个Demo,只需按照以下步骤将此项目中的内容复制导入即可.

集成

1.	项目的build.gradle中添加依赖库 

compile 'com.google.zxing:core:3.3.0'
 

2.	将整个包 com.google.zxing 复制到自己的项目下.
3.	复制res下的资源文件,注意不要覆盖了自己项目里的内容,可以将内容赋值到对应的文件夹里.
复制完的目录结构

 
4.	添加权限

<uses-permission android:name="android.permission.INTERNET" /> <!-- 网络权限 -->
<uses-permission android:name="android.permission.VIBRATE" /> <!-- 震动权限 -->
<uses-permission android:name="android.permission.CAMERA" /> <!-- 摄像头权限 -->
<uses-feature android:name="android.hardware.camera.autofocus" /> <!-- 自动聚焦权限 -->


使用

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG= "MainActivity";
    Button openQrCodeScan;	//打开摄像头按钮
    EditText text;		//扫描成功后,显示文本

    Button CreateQrCode;	    	//生成二维码的按钮

    ImageView QrCode;		//二维码显示的图片

    TextView qrCodeText;		//要生成二维码的文字
    
    private int REQUEST_CODE = 0x01; //打开扫描界面请求码
    private int RESULT_OK = 0xA1; //扫描成功返回码
    
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
            String scanResult=bundle.getString("qr_scan_result");
            qrCodeText.setText(scanResult);
        }
    }
}

说明
1.	调用扫描

Intent intent =new Intent(MainActivity.this, CaptureActivity.class);
startActivityForResult(intent,REQUEST_CODE);
实际上就是打开一个新的活动页,进行扫描获取条码数据,获取成功的时候,就退回来
并且会把获取的数据传回来
扫描结果的回调,重写方法 onActivityResult

2.	生成二维码

Bitmap bitmap = EncodingHandler.createQRCode(str,500);  //500表示宽高

部分源码类介绍


CaptureActivity Zxing暴露出来进行调用的界面，在handleDecode方法中对扫码成功后的动作进行处理。

CameraManager getFramingRect()方法,定义了扫描的区域，可以自己修改。

ViewfinderView ZXing扫码窗口的绘制。

private void drawTextInfo(Canvas canvas, Rect frame)

修改文本绘制的位置

private void drawLaserScanner(Canvas canvas, Rect frame)

修改扫描线的样式。注意若使用paint.setShader(Shader shader) 方法，一定要在绘制完成后调用paint.setShader(null)。以免绘制信息出错。

CameraConfigurationManager 修改横竖屏、处理变形效果的核心类。

DecodeHandler.decode ZXing解码的核心类

CaptureActivityHandler

当DecodeHandler.decode完成解码后，系统会向CaptureActivityHandler发消息。如果编码成功则调用CaptureActivity.handleDecode方法对扫描到的结果进行分类处理。

