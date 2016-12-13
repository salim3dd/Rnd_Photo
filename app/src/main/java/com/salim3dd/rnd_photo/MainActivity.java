package com.salim3dd.rnd_photo;

/**
 * Created by Salim3dd on 13/12/2016.
 */

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper DB_NAME_Photos = new DatabaseHelper(this);
    private ImageView imageView_1, imageView_2;
    private TextView Text_QU, Text_Result;
    private Button btn_Next, btn_ReNew;
    private int ANS, Result = 0, Remainder_NUM = 0;

    public static Bundle mBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView_1 = (ImageView) findViewById(R.id.imageView_1);
        imageView_2 = (ImageView) findViewById(R.id.imageView_2);
        Text_QU = (TextView) findViewById(R.id.Text_QU);
        Text_Result = (TextView) findViewById(R.id.Text_Result);
        btn_Next = (Button) findViewById(R.id.btn_Next);
        btn_ReNew = (Button) findViewById(R.id.btn_ReNew);


        ////////////كود نسخ قاعدة البيانات اول مرة
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == database.exists()) {
            DB_NAME_Photos.getReadableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(MainActivity.this, "تم نسخ قاعدة البيانات بنجاح", Toast.LENGTH_SHORT).show();
                GetNextQU();
            } else {
                Toast.makeText(MainActivity.this, "خطأ لم يتم نسخ قاعدة البيانات", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //////////////////////////


        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetNextQU();
            }
        });

        ////////////تجديد الاسئلة
        btn_ReNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB_NAME_Photos.update_isShow_ReNew();
                Text_Result.setText("");
                Result = 0;
                imageView_2.setEnabled(true);
                imageView_1.setEnabled(true);
                GetNextQU();
            }
        });

        imageView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Remainder_NUM > 0) {
                    if (ANS == 1) {
                        Toast.makeText(MainActivity.this, "الاجابة صحيحة", Toast.LENGTH_SHORT).show();
                        Result++;
                    } else {
                        Toast.makeText(MainActivity.this, "الاجابة خاطئة", Toast.LENGTH_SHORT).show();
                    }
                    Text_Result.setText("الاجابات الصحيحة : " + Result);
                    GetNextQU();
                }
            }
        });

        imageView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Remainder_NUM > 0) {
                    if (ANS == 2) {
                        Toast.makeText(MainActivity.this, "الاجابة صحيحة", Toast.LENGTH_SHORT).show();
                        Result++;
                    } else {
                        Toast.makeText(MainActivity.this, "الاجابة خاطئة", Toast.LENGTH_SHORT).show();
                    }
                    Text_Result.setText("الاجابات الصحيحة : " + Result);
                    GetNextQU();
                }
            }
        });

    }

    public void GetNextQU() {


        List<Integer> List_ID;
        List_ID = DB_NAME_Photos.get_Remainder_List();
        Remainder_NUM = List_ID.size();

        if (List_ID.size() > 0) {

            Random n = new Random();
            int Rnd_id = n.nextInt(List_ID.size());
            int Getid = List_ID.get(Rnd_id);

            DB_NAME_Photos.get_Next_Search(Getid);

            String Photo1 = mBundle.getString("Photo_1");
            String Photo2 = mBundle.getString("Photo_2");
            String Qu = mBundle.getString("QU");
            ANS = mBundle.getInt("ANS");

            imageView_1.setImageResource(getResources().getIdentifier((Photo1), "drawable", getPackageName()));
            imageView_2.setImageResource(getResources().getIdentifier((Photo2), "drawable", getPackageName()));
            Text_QU.setText(Qu);

            /////عمل تحديث لعدم عرضه مرة أخرى أو تكراره
            DB_NAME_Photos.update_isShow(String.valueOf(Getid));

        } else {
            Toast.makeText(this, "انتهت الاسئلة", Toast.LENGTH_SHORT).show();
            Text_Result.append("\n" + "انتهت الاسئلة");
            imageView_2.setEnabled(false);
            imageView_1.setEnabled(false);
        }
    }

    ////////////////كود نسخ قاعدة البيانات أول مرة فقط
    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
