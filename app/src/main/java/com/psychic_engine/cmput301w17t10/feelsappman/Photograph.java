package com.psychic_engine.cmput301w17t10.feelsappman;

import android.graphics.Bitmap;
import android.os.Build;

/**
 * Created by hnkhan on 2/27/17. Edited by pslin 03/10/17
 */

public class Photograph {

    private static Integer BYTE;
    private Boolean limitSize;
    private Bitmap map;
    public Photograph(Bitmap image) {
        this.map = image;
        //getAllocationByteCount exclusive to API>18
        if (Build.VERSION.SDK_INT > 18) {
            this.BYTE = image.getAllocationByteCount();
        } else {
            this.BYTE = image.getByteCount();
        }
        this.limitSize = this.BYTE < 65536;
    }

    public void setMap(Bitmap map) {
        this.map = map;
    }

    public Boolean getLimitSize() {
        return limitSize;
    }

    public Bitmap getImage() {
        return this.map;
    }

    public void deleteImage() {

    }
}
