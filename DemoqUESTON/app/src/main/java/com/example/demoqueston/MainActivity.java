package com.example.demoqueston;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.example.demoqueston.databinding.ActivityMainBinding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    ActivityResultLauncher<Intent> resultLauncher;

    byte[] bytes ;

    Bitmap bitmap;

    DBHelper dbHelper = new DBHelper(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // registerResult metodunu çağırın
        registerResult();

        binding.button.setOnClickListener(view -> pickImage()


        );

    }

    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try {
                            Uri imageUri = o.getData().getData();
                            binding.imageView.setImageURI(imageUri);
                        } catch (Exception e) {
                            // Hata durumunda işlemler
                        }
                    }
                }
        );
    }

        public static InputStream getInputStreamFromImageView(ImageView view) {
            // ImageView'dan Drawable nesnesini al
            BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
            if (drawable != null) {
                // Drawable nesnesini bir Bitmap'e dönüştür
                Bitmap bitmap = drawable.getBitmap();
                // Bitmap'ten ByteArrayOutputStream oluştur
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // Bitmap'i JPEG formatında ByteArrayOutputStream'e yaz
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                // ByteArrayOutputStream'den byte dizisi elde et
                byte[] bytes = byteArrayOutputStream.toByteArray();
                // Byte dizisini InputStream'e dönüştür
                return new ByteArrayInputStream(bytes);
            }
            return null;

    }

    public void save(View v){

        InputStream inputStream = MainActivity.getInputStreamFromImageView(binding.imageView);
        Utils utils = new Utils();
        try {
            bytes =  utils.getBytes(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       bitmap =  utils.getImage(bytes);

        try {

            dbHelper.open();
            dbHelper.insertImage(bytes);
            dbHelper.close();

        }

        catch (Exception e ){


        }

    }




}
