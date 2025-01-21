/*
 * Copyright (c) 2017-2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.misc;

class NewtonApproximation {
    public static void main(String[] args) {
        // Newton's Approximation to solve cos(x) = x
        double x = 1.0;
        int left = 200;
        for (; left > 0; left--) {
            final double y= x * x - 6205;
            if (Math.abs(y) < 1E-7) {
                System.out.printf("the solution to y= x * x - 6205 is: %.4f%n" , x);
                System.exit(0);
            }
            x = x - y / (2 * x);
        }
    }
}