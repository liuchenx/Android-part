package me.liuyichen.androidpart.photo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

/**
 * Created by liu on 2015/11/10.
 */
public class PhotoDialog extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "PhotoDialog";

    private static final int PICTURE_FROM_CAMERA = 1;
    private static final int PICTURE_FROM_GALLERY = 2;
    public static int REQUEST_CODE = 0;

    private static final String TMP_NAME = "camera_tmp";
    private static final String[] arrays = { "拍照", "选择照片" };

    public static void show(Activity activity) {
        activity.startActivityForResult(new Intent(activity, PhotoDialog.class), REQUEST_CODE);
    }

    public static void show(Activity activity, int paramInt) {
        REQUEST_CODE = paramInt;
        activity.startActivityForResult(new Intent(activity, PhotoDialog.class), REQUEST_CODE);
    }

    private void openGalley() {
        try {
            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICTURE_FROM_GALLERY);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        File file;
        if (Environment.getExternalStorageState().equals("mounted")) {
            file = new File(getExCacheDir(), "camera_tmp");
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if (file.exists())
                    file.delete();
                intent.putExtra("output", Uri.fromFile(file));
                startActivityForResult(intent, PICTURE_FROM_CAMERA);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                android.util.Log.e(TAG, "no found camera");
            }
        } else {
            android.util.Log.e(TAG, "sdcard not mounted");
        }
    }

    @Nullable
    private File getExCacheDir() {
        File file = getExternalCacheDir();
        if (file == null)
            return null;
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photo);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        for (int i = 0; i < arrays.length; i++) {
            adapter.add(arrays[i]);
        }
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        switch (i) {
            case 0:
                openCamera();
                return;
            case 1:
                openGalley();
                break;
        }
    }

    private void getCameraPicture() {
        File file = new File(getExCacheDir(), "camera_tmp");
        android.util.Log.e(TAG, "[getCameraPicture]  image path: " + file.getAbsolutePath());
        if (file.exists()) {
            Uri localUri = Uri.fromFile(file);
            Intent localIntent = new Intent();
            localIntent.setData(localUri);
            setResult(RESULT_OK, localIntent);
            finish();
            REQUEST_CODE = 1;
        }
    }

    private void getGalleyPicture(Intent intent) {
        if (intent != null) {
            Uri localUri = intent.getData();
            Intent localIntent = new Intent();
            localIntent.setData(localUri);
            setResult(RESULT_OK, localIntent);
            finish();
            REQUEST_CODE = 1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    getCameraPicture();
                    return;
                case 2:
                    getGalleyPicture(data);
            }
        }
    }
}
