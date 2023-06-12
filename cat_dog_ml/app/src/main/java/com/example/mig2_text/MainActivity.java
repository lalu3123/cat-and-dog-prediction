package com.example.mig2_text;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Element;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mig2_text.ml.DogCat;
import com.example.mig2_text.ml.MobilenetV110224Quant;
import com.example.mig2_text.ml.Model;
import com.example.mig2_text.ml.Modells;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button select,predict;
    private TextView tv;
    private Bitmap img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        select=findViewById(R.id.select);
        predict=findViewById(R.id.predict);
        tv=findViewById(R.id.textView);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,100);
            }
        });
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = Bitmap.createScaledBitmap(img, 299, 299, true);
                try {
//                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(getApplicationContext());
//                    DogCat model = DogCat.newInstance(getApplicationContext());
//                    Model model = Model.newInstance(getApplicationContext());
                    Modells model = Modells.newInstance(getApplicationContext());

                    // Creates inputs for reference.
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 299, 299, 3}, DataType.FLOAT32);
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 299, 299, 3}, DataType.FLOAT32);
                    TensorImage tensorImage=new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer= tensorImage.getBuffer();
                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
//                    MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
//                    DogCat.Outputs outputs = model.process(inputFeature0);
//                    Model.Outputs outputs = model.process(inputFeature0);
                    Modells.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    tv.setText( "dog  :"+outputFeature0.getFloatArray()[0] + " \n" +"cat  :" + outputFeature0.getFloatArray()[1]);

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100);
        {
            imageView.setImageURI(data.getData());

            Uri uri=data.getData();
            try {
                img= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}