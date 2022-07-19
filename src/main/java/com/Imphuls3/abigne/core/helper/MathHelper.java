package com.Imphuls3.abigne.core.helper;

import net.minecraft.util.Mth;

import java.math.*;
import java.util.Arrays;

public class MathHelper {
    public static float pythag(float a, float b){
        float a2 = a*a;
        float b2 = b*b;
        return Mth.sqrt(a2+b2);
    }

    public static float pythag3D(float a, float b, float c){
        float diag1 = pythag(a, b);
        
        return pythag(diag1, c);
    }

    public static float distSqr(float... a) {
        float d = 0.0F;
        for (float f : a) {
            d += f * f;
        }
        return d;
    }

    public static float distance(float... a) {
        return Mth.sqrt(distSqr(a));
    }

    public static int largestNum(int nums[]){
        int size = nums.length;
        Arrays.sort(nums);
        int res = nums[size-1];
        return res;
    }
}
