package bhalotia.sunny.cameratrial;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    Button clickBtn;
    String filePath, filename;
    Bitmap yourSelectedImage;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickBtn = (Button) findViewById(R.id.button1);
        image = (ImageView) findViewById(R.id.image);

        clickBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    // file path of captured image
                    filePath = cursor.getString(columnIndex);
                    // file path of captured image
                    File f = new File(filePath);
                    filename = f.getName();

                    Toast.makeText(getApplicationContext(),
                            "Your Path:" + filePath, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),
                            "Your Filename:" + filename, Toast.LENGTH_LONG).show();
                    cursor.close();

                    // Convert file path into bitmap image using below line.
                    // yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    // Toast.makeText(getApplicationContext(),
                    // "Your image"+yourSelectedImage, 2000).show();
                    Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    // put bitmapimage in your imageviewyourSelectedImage
                    image.setImageBitmap(myBitmap);
                    image.setVisibility(View.VISIBLE);

                    // To save the file in sdcard
                    Savefile(filename, filePath);
                }
        }
    }

    public void Savefile(String name, String path) {

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/MyAppFolder/MyApp/");

        File file = new File(Environment.getExternalStorageDirectory()
                + "/MyAppFolder/MyApp/" + name);
        if (!direct.exists()) {
            direct.mkdir();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                FileChannel src = new FileInputStream(path).getChannel();
                FileChannel dst = new FileOutputStream(file).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }
