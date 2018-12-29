package neilj.implicitintents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int CAMERA_PIC_REQUEST = 1;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText mWebsiteText;
    private EditText mLocationEditText;
    private EditText mShareTextEditText;
    private int mImageWidth;
    private ImageView mImage;
    private Uri mImageUri;
    private String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define elements in page
        mWebsiteText = findViewById(R.id.website_edittext);
        mLocationEditText = findViewById(R.id.location_edittext);
        mShareTextEditText = findViewById(R.id.share_edittext);
        mImage = findViewById(R.id.camera_image);
        mImageWidth = mImage.getWidth();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //case image
        //check if needs to be saved
        if (mCurrentPhotoPath != null) {
            if (mImage.getVisibility() == View.VISIBLE)
                outState.putParcelable("image_uri", mImageUri);
            outState.putString("image_path", mCurrentPhotoPath);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);

        if (saveInstanceState != null) {
            mCurrentPhotoPath = saveInstanceState.getString("image_path");
            mImageUri = saveInstanceState.getParcelable("image_uri");
            if (mImageUri != null) {
                mImage.setImageURI(mImageUri);
                mImage.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void openWebsite(View view) {
        //get url text
        String url = mWebsiteText.getText().toString();

        //parse URI and create intent
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        //find an activity that can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }

    protected void openLocation(View view) {
        //get location text
        String loc = mLocationEditText.getText().toString();

        //parse into URI object with geo search query
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);

        //find an activity that can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }

    protected void shareText(View view) {
        //get share text
        String txt = mShareTextEditText.getText().toString();

        //define mime type of text to share and call ShareCompat.IntentBuilder
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(R.string.chooser_title)
                .setText(txt)
                .startChooser();
    }

    //creates collision-resistant names for images captured
    private File createImageFile() throws IOException {
        //creates an image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /*prefix*/
                ".jpg",  /*suffix*/
                storageDir     /*directory*/
        );

        //save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    protected void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //ensure there's aa camera activity that can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            //create file where photo goes
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                //Error occurred while creating file
            }
            //Continue only if file is created successfully
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "neilj.implicitintents.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),
                        "Image Captured!", Toast.LENGTH_SHORT).show();
                //locate image using filepath
                //setImage();
                File imageFile = new File(mCurrentPhotoPath);
                mImageUri = Uri.fromFile(imageFile);
                mImage.setImageURI(mImageUri);

                mImage.setVisibility(View.VISIBLE);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setImage() {
        //since height is not defined currently, only going to use width
        int targetW = mImageWidth;
        Log.d(LOG_TAG, "targetW = " + targetW);
        //get dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int imageW = bmOptions.outWidth;

        //determine scalefactor
        int scaleFactor = imageW / targetW;

        //decode into bitmap sized to fit view
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImage.setImageBitmap(bitmap);

        //lastly compress bitmap into uri to save
        mImageUri = getImageUri(getBaseContext(), bitmap);
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}
