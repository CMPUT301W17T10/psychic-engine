package com.psychic_engine.cmput301w17t10.feelsappman.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by hnkhan on 2/27/17. Edited by pslin 03/10/17
 * Commented by adong on 3/29/2017
 */

/**
 * The photograph model contain the bitmap used to display the images that the participant would
 * like to add. The photograph will need to restricted to the uncompressed size of 65536 bytes.
 * This enables the photograph to be stored in the elastic server and retrieved when needed.
 */
public class Photograph implements Serializable {

    //TODO: getByteSizeCount not accurate. Decoded memory vs compressed size
    private static Integer BYTE;
    private Boolean limitSize;
    private transient Bitmap map;
    private String encodedPhoto;

    public Photograph(Bitmap image) {
        this.map = image;
        //getAllocationByteCount exclusive to API>18
        if (image != null) {
            if (Build.VERSION.SDK_INT > 18) {
                this.BYTE = image.getAllocationByteCount();
            } else {
                this.BYTE = image.getByteCount();
            }

        }
        byte tempArray[];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int compression = 100;
        do {
            image.compress(Bitmap.CompressFormat.JPEG, compression, stream);
            tempArray = stream.toByteArray();
            this.encodedPhoto = Base64.encodeToString(tempArray, Base64.DEFAULT);
            compression -= 5;
        } while (encodedPhoto.length() > 65536);
        Log.i("PhotoSize", Integer.toString(encodedPhoto.length()));
        this.limitSize = encodedPhoto.length() < 65536;

    }

    /**
     * Setter method to set the bitmap
     * @param map Bitmap to be set in
     */
    public void setMap(Bitmap map) {
        this.map = map;
    }

    /**
     * Getter method to return the size of 65536 bytes that is necessary for proper elastic server storage
     * @return limitSize which is 65536
     */
    public Boolean getLimitSize() {
        return limitSize;
    }

    /**
     * Getter method to return the bitmap of the photograph.
     * @return Bitmap of the photograph
     */
    public Bitmap getImage() {
        byte imageArray[];
        imageArray = Base64.decode(encodedPhoto, Base64.DEFAULT);
        int size = imageArray.length;
        Bitmap map;
        map = BitmapFactory.decodeByteArray(imageArray, 0, size);
        return map;
    }

}
