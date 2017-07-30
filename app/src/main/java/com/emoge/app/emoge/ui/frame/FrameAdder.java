package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.encoder.GifDecoder;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 7. 26.
 */


public class FrameAdder implements OnBMClickListener {
    private static final String LOG_TAG = FrameAdder.class.getSimpleName();

    // Menu Position
    public static final int INTENT_GET_IMAGE        = 0;
    public static final int INTENT_GET_GIF          = 1;
    public static final int INTENT_GET_VIDEO        = 2;

    public static final int INTENT_CAPTURE_VIDEO    = 100;

    public static final int MAX_WIDTH   = 400;
    public static final int MAX_HEIGHT  = 400;

    private Activity activity;
    private int selectedIndex;

    public FrameAdder(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onBoomButtonClick(int index) {
        Intent intent;

        switch (index) {
            case INTENT_GET_IMAGE :
                intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/jpeg", "image/png"});
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_image)), INTENT_GET_IMAGE);
                break;
            case INTENT_GET_GIF :
                intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/gif"});
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_image)), INTENT_GET_GIF);
                break;
            case INTENT_GET_VIDEO :
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_video)), INTENT_GET_VIDEO);
                break;
        }

    }

    private String getRealPathFromUri(Context context, Uri selectedUri) {
        String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE };

        Cursor cursor = context.getContentResolver().query(selectedUri, columns, null, null, null);
        cursor.moveToFirst();

        int pathColumnIndex     = cursor.getColumnIndex( columns[0] );
        int mimeTypeColumnIndex = cursor.getColumnIndex( columns[1] );

        String contentPath = cursor.getString(pathColumnIndex);
        String mimeType    = cursor.getString(mimeTypeColumnIndex);
        cursor.close();

        if(mimeType.startsWith("image")) {
            // image
        }
        else if(mimeType.startsWith("video")) {
            // video
        }

        return contentPath;
    }


    @NonNull
    public Bitmap loadBitmapSampleSize(int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(activity.getResources(), resourceId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight);
        return BitmapFactory.decodeResource(activity.getResources(), resourceId, options);
    }


    @NonNull
    public Bitmap loadBitmapSampleSize(@NonNull Uri imageUri) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri), null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight);
        return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri), null, options);
    }

    @NonNull
    public List<Bitmap> loadBitmapsFromGif(@NonNull Uri imageUri) throws FileNotFoundException {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        GifDecoder decoder = new GifDecoder();
        decoder.read(activity.getContentResolver().openInputStream(imageUri));
        for(int i=0; i<decoder.getFrameCount(); i++) {
            bitmaps.add(decoder.getFrame(i));
        }
        return bitmaps;
    }


    private int getSampleSize(int width, int height) {
        int inSampleSize = 1;
        if (width > MAX_WIDTH || height > MAX_HEIGHT) {
            inSampleSize = width > height ? width / MAX_WIDTH : height / MAX_HEIGHT;
        }
        return inSampleSize;
    }



    @NonNull
    public List<Bitmap> captureVideo(Uri videoUri, int startSec, int count, int fps) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        List<Bitmap> bitmapArrayList = new ArrayList<>();

        retriever.setDataSource(activity, videoUri);
        for(int i = 0; i < count; i++){
            bitmapArrayList.add(retriever.getFrameAtTime((startSec + i*fps)*1000, MediaMetadataRetriever.OPTION_CLOSEST));
        }
        retriever.release();

        return bitmapArrayList;
    }

}
