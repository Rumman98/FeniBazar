package www.fenibazar.com;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.github.ybq.android.spinkit.style.DoubleBounce;


import java.io.File;
import java.io.IOException;


public class firstpage extends AppCompatActivity {
    RelativeLayout relativeLayout;
    Button button;
    WebView wv;
    String url = "https://fenibazar.com";
    ProgressBar progressBarWeb;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swiperefreshLayout;
    private static final int REQUEST_CODE = 1;





   public static final String TAG = firstpage.class.getSimpleName();

   public static final int FILECHOOSER_RESULTCODE = 1;
    public ValueCallback<Uri> mUploadMessage;
    public Uri mCapturedImageURI = null;

    // the same for Android 5.0 methods only
    public ValueCallback<Uri[]> mFilePathCallback;
    public String mCameraPhotoPath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        verifyPermissions();
        wv = (WebView) findViewById(R.id.webview);
        button=(Button)findViewById(R.id.btn);

        relativeLayout=(RelativeLayout)findViewById(R.id.Relative);
        swiperefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        if (!DetectConnection.checkInternetConnection(this)) {
            Intent intent=new Intent(firstpage.this,nointernet.class);
            startActivity(intent);
        }
        else {
            relativeLayout.setVisibility(View.GONE);
            CustomWebViewClient c =  new CustomWebViewClient();
            wv.setWebViewClient(c);
            WebSettings webSettings=wv.getSettings();
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            wv.loadUrl(url);
            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setDisplayZoomControls(false);
            wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            wv.getSettings().setAppCacheEnabled(true);
            wv.getSettings().setMediaPlaybackRequiresUserGesture(true);
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setSavePassword(true);
            webSettings.setSaveFormData(true);
            webSettings.setEnableSmoothTransition(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // chromium, enable hardware acceleration
                wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                // older android version, disable hardware acceleration
                wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }







        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wv.reload();
               relativeLayout.setVisibility(View.GONE);


            }
        });




        swiperefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                wv.reload();

            }

        });


        progressBarWeb=(ProgressBar)findViewById(R.id.progress);
        DoubleBounce doubleBounce=new DoubleBounce();
        progressBarWeb.setIndeterminateDrawable(doubleBounce);







        wv.setWebViewClient(new CustomWebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                progressBarWeb.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBarWeb.setVisibility(View.GONE);
                swiperefreshLayout.setRefreshing(false);
                super.onPageFinished(view, url);
            }



        });


        wv.setWebChromeClient(new WebChromeClient(){

            // for Lollipop, all in one
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    // create the file where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // continue only if the file was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                return true;
            }

            // creating image files (Lollipop only)
            private File createImageFile() throws IOException {

                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs();
                }

                // create an image file name
                imageStorageDir = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                return imageStorageDir;
            }

            // openFileChooser for Android 3.0+
          private void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;

                try {
                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }

                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    mCapturedImageURI = Uri.fromFile(file); // save to the private variable

                    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser));
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Camera Exception:" + e, Toast.LENGTH_LONG).show();
                }

            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            // openFileChooser for other Android versions
            /* may not work on KitKat due to lack of implementation of openFileChooser() or onShowFileChooser()
               https://code.google.com/p/android/issues/detail?id=62220
               however newer versions of KitKat fixed it on some devices */
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }



        });

    }
    }

    public class CustomWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!DetectConnection.checkInternetConnection(getApplicationContext())) {
               Intent intent=new Intent(firstpage.this,nointernet.class);
               startActivity(intent);
            } else {
                wv.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
           relativeLayout.setVisibility(View.VISIBLE);
        }
    }


    // return here when file selected from camera or from SD Card
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e, Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }

        } // end of code for all versions except of Lollipop

        // start of code for Lollipop only
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != FILECHOOSER_RESULTCODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null || data.getData() == null) {
                    // if there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } // end of code for Lollipop only
    }

    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(firstpage.this,
                    permissions,
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }


    public static class DetectConnection {
        public static boolean checkInternetConnection(Context context) {

            ConnectivityManager con_manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return (con_manager.getActiveNetworkInfo() != null
                    && con_manager.getActiveNetworkInfo().isAvailable()
                    && con_manager.getActiveNetworkInfo().isConnected());
        }
    }



    @Override
    public void onBackPressed() {
        if (wv.canGoBack()){
            wv.goBack();
        }
        else{
            super.onBackPressed();
        }

    }
}
