package com.vchatcloud.androidsample.resourse;

import android.util.Log;
import android.widget.Toast;

import com.vchatcloud.androidsample.R;

public class ProfileRescource {
    private static int index = 0;
    public static int[] profiles = new int[] {
            R.drawable.profile_img_1
            ,R.drawable.profile_img_2
            ,R.drawable.profile_img_3
            ,R.drawable.profile_img_4
            ,R.drawable.profile_img_5
            ,R.drawable.profile_img_6
            ,R.drawable.profile_img_7
            ,R.drawable.profile_img_8
            ,R.drawable.profile_img_9
            ,R.drawable.profile_img_10
            ,R.drawable.profile_img_11
            ,R.drawable.profile_img_12
            ,R.drawable.profile_img_13
            ,R.drawable.profile_img_14
            ,R.drawable.profile_img_15
            ,R.drawable.profile_img_16
            ,R.drawable.profile_img_17
            ,R.drawable.profile_img_18
            ,R.drawable.profile_img_19
            ,R.drawable.profile_img_20
            ,R.drawable.profile_img_21
            ,R.drawable.profile_img_22
            ,R.drawable.profile_img_23
            ,R.drawable.profile_img_24
            ,R.drawable.profile_img_25
            ,R.drawable.profile_img_26
            ,R.drawable.profile_img_27
            ,R.drawable.profile_img_28
            ,R.drawable.profile_img_29
            ,R.drawable.profile_img_30
            ,R.drawable.profile_img_31
            ,R.drawable.profile_img_32
            ,R.drawable.profile_img_33
            ,R.drawable.profile_img_34
            ,R.drawable.profile_img_35
            ,R.drawable.profile_img_36
            ,R.drawable.profile_img_37
            ,R.drawable.profile_img_38
            ,R.drawable.profile_img_39
            ,R.drawable.profile_img_40
            ,R.drawable.profile_img_41
            ,R.drawable.profile_img_42
            ,R.drawable.profile_img_43
            ,R.drawable.profile_img_44
            ,R.drawable.profile_img_45
            ,R.drawable.profile_img_46
            ,R.drawable.profile_img_47
            ,R.drawable.profile_img_48
    };
    public static void setIndex(int _index) {
        if (_index < 0) {
            index = 0;
        } else if (_index > indexSize()) {
            index = indexSize();
        } else {
            index = _index;
        }
    }
    public static int getIndex() {
        return index + 1;
    }
    public static int getProfile() {
        return profiles[index];
    }
    public static int getSize() {
        return profiles.length;
    }
    public static int indexSize() {
        return getSize() -1;
    }

    public static int setLeft() {
        index = index - 1;
        if (index < 0) {
            index = indexSize();
        }

        return profiles[index];
    }
    public static int setRight() {
        index = index + 1;
        if (index > indexSize() ) {
            index = 0;
        }
        return profiles[index];
    }
}
