package com.soundcloud.android.crop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.sn.lib.R;

import java.io.File;

/**
 * Builder for crop Intents and utils for handling result
 */
public class Crop {


    private final static String FILE_LOCATION = "sdcard/b600db3ffd88acf2d038723ab3ab960f";
    private final static String IMAGE_FILE_LOCATION = "file:///" + FILE_LOCATION;
    private final static Uri IMAGE_URI = Uri.parse(IMAGE_FILE_LOCATION);


    public static final int REQUEST_CROP = 6709;
    public static final int REQUEST_PICK = 9162;
    public static final int RESULT_ERROR = 404;

    interface Extra {
        String ASPECT_X = "aspect_x";
        String ASPECT_Y = "aspect_y";
        String MAX_X = "max_x";
        String MAX_Y = "max_y";
        String AS_PNG = "as_png";
        String ERROR = "error";
    }

    private Intent cropIntent;


    /**
     * Create a crop Intent builder with source and destination image Uris
     *
     * @param source      Uri for image to crop
     * @param destination Uri for saving the cropped image
     */
    public static Crop of(Uri source, Uri destination) {
        if (source == null) source = IMAGE_URI;
        return new Crop(source, destination);
    }

    private Crop(Uri source, Uri destination) {
        cropIntent = new Intent();
        cropIntent.setData(source);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
    }

    /**
     * Set fixed aspect ratio for crop area
     *
     * @param x Aspect X
     * @param y Aspect Y
     */
    public Crop withAspect(int x, int y) {
        cropIntent.putExtra(Extra.ASPECT_X, x);
        cropIntent.putExtra(Extra.ASPECT_Y, y);
        return this;
    }

    /**
     * Crop area with fixed 1:1 aspect ratio
     */
    public Crop asSquare() {
        cropIntent.putExtra(Extra.ASPECT_X, 1);
        cropIntent.putExtra(Extra.ASPECT_Y, 1);
        return this;
    }

    /**
     * Set maximum crop size
     *
     * @param width  Max width
     * @param height Max height
     */
    public Crop withMaxSize(int width, int height) {
        cropIntent.putExtra(Extra.MAX_X, width);
        cropIntent.putExtra(Extra.MAX_Y, height);
        return this;
    }

    /**
     * Set whether to save the result as a PNG or not. Helpful to preserve alpha.
     *
     * @param asPng whether to save the result as a PNG or not
     */
    public Crop asPng(boolean asPng) {
        cropIntent.putExtra(Extra.AS_PNG, asPng);
        return this;
    }

    /**
     * Send the crop Intent from an Activity
     *
     * @param activity Activity to receive result
     */
    public void start(Activity activity) {
        start(activity, REQUEST_CROP);
    }

    /**
     * Send the crop Intent from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public void start(Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    /**
     * Send the crop Intent from a Fragment
     *
     * @param context  Context
     * @param fragment Fragment to receive result
     */
    public void start(Context context, Fragment fragment) {
        start(context, fragment, REQUEST_CROP);
    }

    /**
     * Send the crop Intent from a support library Fragment
     *
     * @param context  Context
     * @param fragment Fragment to receive result
     */
    public void start(Context context, android.support.v4.app.Fragment fragment) {
        start(context, fragment, REQUEST_CROP);
    }

    /**
     * Send the crop Intent with a custom request code
     *
     * @param context     Context
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start(Context context, Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    /**
     * Send the crop Intent with a custom request code
     *
     * @param context     Context
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    public void start(Context context, android.support.v4.app.Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    /**
     * Get Intent to start crop Activity
     *
     * @param context Context
     * @return Intent for CropImageActivity
     */
    public Intent getIntent(Context context) {
        cropIntent.setClass(context, CropImageActivity.class);
        return cropIntent;
    }

    /**
     * Retrieve URI for cropped image, as set in the Intent builder
     *
     * @param result Output Image URI
     */
    public static Uri getOutput(Intent result) {
        return result.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
    }

    /**
     * Retrieve error that caused crop to fail
     *
     * @param result Result Intent
     * @return Throwable handled in CropImageActivity
     */
    public static Throwable getError(Intent result) {
        return (Throwable) result.getSerializableExtra(Extra.ERROR);
    }

    /**
     * Pick image from an Activity
     *
     * @param activity Activity to receive result
     */
    public static void pickImage(Activity activity) {
        pickImage(activity, REQUEST_PICK);
    }


    /**
     * Pick image from a Fragment
     *
     * @param context  Context
     * @param fragment Fragment to receive result
     */
    public static void pickImage(Context context, Fragment fragment) {
        pickImage(context, fragment, REQUEST_PICK);
    }

    /**
     * Pick image from a support library Fragment
     *
     * @param context  Context
     * @param fragment Fragment to receive result
     */
    public static void pickImage(Context context, android.support.v4.app.Fragment fragment) {
        pickImage(context, fragment, REQUEST_PICK);
    }

    /**
     * Pick image from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public static void pickImage(Activity activity, int requestCode) {
        try {
            activity.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(activity);
        }
    }

    /**
     * Pick image from a Fragment with a custom request code
     *
     * @param context     Context
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void pickImage(Context context, Fragment fragment, int requestCode) {
        try {
            fragment.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(context);
        }
    }

    /**
     * Pick image from a support library Fragment with a custom request code
     *
     * @param context     Context
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    public static void pickImage(Context context, android.support.v4.app.Fragment fragment, int requestCode) {
        try {
            fragment.startActivityForResult(getImagePicker(), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(context);
        }
    }

    private static Intent getImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        return intent;
    }

    /**
     * Pick image from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public static void takeImage(Activity activity) {
        try {
            activity.startActivityForResult(getCameraPicker(activity), REQUEST_PICK);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(activity);
        }
    }

    /**
     * Pick image from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public static void takeImage(Activity activity, int requestCode) {
        try {
            activity.startActivityForResult(getCameraPicker(activity), requestCode);
        } catch (ActivityNotFoundException e) {
            showImagePickerError(activity);
        }
    }


    private static Intent getCameraPicker(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        return intent;
    }

    private static void showImagePickerError(Context context) {
        Toast.makeText(context.getApplicationContext(), R.string.crop__pick_error, Toast.LENGTH_SHORT).show();
    }


    public static Uri getImageUri() {
        return IMAGE_URI;
    }
}
