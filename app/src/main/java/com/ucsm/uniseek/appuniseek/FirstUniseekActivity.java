package com.ucsm.uniseek.appuniseek;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.media.ThumbnailUtils;

import com.ucsm.uniseek.R;
import com.ucsm.uniseek.ml.ModelColor;
import com.ucsm.uniseek.ml.ModelColoresv2;
import com.ucsm.uniseek.ml.ModelObjetosv2;
import com.ucsm.uniseek.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FirstUniseekActivity extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_uniseek);
        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            ModelObjetosv2 model = ModelObjetosv2.newInstance(getApplicationContext());
            ModelColoresv2 model2 = ModelColoresv2.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues,0, image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel = 0;
            for(int i =0; i <imageSize; i++){
                for(int j=0; j<imageSize; j++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val>>16)& 0xFF)*(1f/255.f));
                    byteBuffer.putFloat(((val>>8)& 0xFF)*(1f/255.f));
                    byteBuffer.putFloat((val & 0xFF)*(1f/255.f));
                }

            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.

            ModelObjetosv2.Outputs outputs = model.process(inputFeature0);
            ModelColoresv2.Outputs outputs2 = model2.process(inputFeature0);


            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature02 = outputs2.getOutputFeature0AsTensorBuffer();


            //Objetos
            float[] confidences = outputFeature0.getFloatArray();
            
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++ ){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            float[] confidences2 = outputFeature02.getFloatArray();

            //Colores
            int maxPos2 = 0;
            float maxConfidence2 = 0;
            for (int i = 0; i < confidences2.length; i++ ){
                if(confidences2[i] > maxConfidence2){
                    maxConfidence2 = confidences2[i];
                    maxPos2 = i;
                }
            }

            String[] classes = {"Billetera","Calculadora","Cargador","Cartuchera","Smartphone","DNI","Laptop","Libro","Mochila","Reloj","Tomatodo"};
            String[] classes2 = {"Amarillo","Azul","Blanco","Gris","Marron","Morado","Naranja","Negro","Rojo","Verde"};

            result.setText(classes[maxPos]+" color "+classes2[maxPos2]);

            //Objetos
            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }

            //Colores
            String s2 = "";
            for(int j = 0; j < classes2.length; j++){
                s2 += String.format("%s: %.1f%%\n", classes2[j], confidences2[j] * 100);
            }
            confidence.setText(s+s2);

            // Releases model resources if no longer used.
            model.close();
            model2.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize,imageSize, false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
