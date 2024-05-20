package com.skunpham.colorpicker;

public interface ColorObserver {
    void onColor(int color, boolean fromUser, boolean shouldPropagate);
}
