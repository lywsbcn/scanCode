package com.utils;

import android.hardware.Camera;

/**
 * Created by admin on 2017/6/4.
 */

public class CommonUtils {

    public static boolean isCameraCanUse(){
        boolean canUse=true;
        Camera camera =null;

        try {
            camera =Camera .open();
        }catch (Exception e){
            canUse=false;
        }

        if (canUse){
            if (camera !=null){
                camera.release();
            }
            camera=null;
        }

        return canUse;
    }
}
