package test;

import Jama.Matrix;

public class Test {
    public static void main(String[] args) {
        double[][] var0 = new double[5][1];
        double[][] var1 = new double[5][1];
        for (int i = 0; i < 5; i++) {
            var0[i][0] = i+1;
            var1[i][0] = i+2;
        }
        Matrix var2 = new Matrix(var0);
        Matrix var3 = new Matrix(var1);
        var2.arrayLeftDivide(var3).print(1,1);
    }
}
