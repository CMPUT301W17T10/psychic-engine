package com.psychic_engine.cmput301w17t10.feelsappman.Models;

import android.graphics.Bitmap;
import android.os.Build;

/**
 * Created by hnkhan on 2/27/17. Edited by pslin 03/10/17
 * Commented by adong on 3/29/2017
 */

/**
 * The photograph model contain the bitmap used to display the images that the participant would
 * like to add. The photograph will need to restricted to the uncompressed size of 65536 bytes.
 * This enables the photograph to be stored in the elastic server and retrieved when needed.
 */
public class Photograph {

    //TODO: getByteSizeCount not accurate. Decoded memory vs compressed size
    private static Integer BYTE;
    private Boolean limitSize;
    private Bitmap map;
    public Photograph(Bitmap image) {
        this.map = image;
        //getAllocationByteCount exclusive to API>18
        if (image != null) {
            if (Build.VERSION.SDK_INT > 18) {
                this.BYTE = image.getAllocationByteCount();
            } else {
                this.BYTE = image.getByteCount();
            }
            this.limitSize = this.BYTE < 65536;
        }
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
        return this.map;
    }

}
