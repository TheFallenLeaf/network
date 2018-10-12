package cn.edu.cup.tanyao.network;

/**
 * 计算BWRS方程中的11个参数
 */
public class Parameter {
    //混合物的11个参数
    public double A;
    public double B;
    public double C;
    public double D;
    public double E;
    public double a;
    public double b;
    public double c;
    public double d;
    public double rfa;
    public double gama;

    public void setData(double[] component) {
        double R = 8.3143;// 气体常数，kJ/(kmol.K);
        // 通用常数A，B值
        double A1 = 0.443690, B1 = 0.115449;
        double A2 = 1.28438, B2 = -0.920731;
        double A3 = 0.356306, B3 = 1.70871;
        double A4 = 0.544979, B4 = -0.270896;
        double A5 = 0.528629, B5 = 0.349261;
        double A6 = 0.484011, B6 = 0.754130;
        double A7 = 0.0705233, B7 = -0.044448;
        double A8 = 0.5040870, B8 = 1.32245;
        double A9 = 0.0307452, B9 = 0.179433;
        double A10 = 0.0732828, B10 = 0.463492;
        double A11 = 0.0064500, B11 = -0.022143;

        double Tc[] = { 190.69, 305.38, 369.89, 408.13, 425.18, 460.37, 469.49,
                507.28, 126.15, 304.09 };// 临界温度，K
        double rou_c[] = { 10.05, 6.7566, 4.9994, 3.8012, 3.9213, 3.2469,
                3.2149, 2.7167, 11.099, 10.638 };// 临界密度，kmol/m3
        double w[] = { 0.013, 0.1018, 0.157, 0.183, 0.197, 0.226, 0.252, 0.302,
                0.035, 0.21 };// 偏心因子
        double M0[] = { 16.042, 30.068, 44.094, 58.12, 58.12, 72.146, 72.146,
                86.172, 28.016, 44.01 };// 相对分子质量

        // 纯组分的11个系数
        double A0[] = new double[10];
        double B0[] = new double[10];
        double C0[] = new double[10];
        double D0[] = new double[10];
        double E0[] = new double[10];
        double a0[] = new double[10];
        double b0[] = new double[10];
        double c0[] = new double[10];
        double d0[] = new double[10];
        double rfa0[] = new double[10];
        double gama0[] = new double[10];

        for (int i = 0; i < 10; i++) {
            B0[i] = (A1 + B1 * w[i]) / rou_c[i];
            A0[i] = (A2 + B2 * w[i]) * R * Tc[i] / rou_c[i];
            C0[i] = (A3 + B3 * w[i]) * R * Math.pow(Tc[i], 3) / rou_c[i];
            gama0[i] = (A4 + B4 * w[i]) / Math.pow(rou_c[i], 2);
            b0[i] = (A5 + B5 * w[i]) / Math.pow(rou_c[i], 2);
            a0[i] = (A6 + B6 * w[i]) * R * Tc[i] / Math.pow(rou_c[i], 2);
            rfa0[i] = (A7 + B7 * w[i]) / Math.pow(rou_c[i], 3);
            c0[i] = (A8 + B8 * w[i]) * R * Math.pow(Tc[i], 3)
                    / Math.pow(rou_c[i], 2);
            D0[i] = (A9 + B9 * w[i]) * R * Math.pow(Tc[i], 4) / rou_c[i];
            d0[i] = (A10 + B10 * w[i]) * R * Math.pow(Tc[i], 2)
                    / Math.pow(rou_c[i], 2);
            E0[i] = (A11 + B11 * w[i] * Math.exp(-3.8 * w[i])) * R
                    * Math.pow(Tc[i], 5) / rou_c[i];
        }

        // 二元交互作用系数
        double k[][] = {
                { 0, 0.01, 0.023, 0.0275, 0.031, 0.036, 0.041, 0.05, 0.025, 0.05 },
                { 0, 0, 0.0031, 0.004, 0.0045, 0.005, 0.006, 0.007, 0.07, 0.048 },
                { 0, 0, 0, 0.003, 0.0035, 0.004, 0.0045, 0.005, 0.1, 0.045 },
                { 0, 0, 0, 0, 0, 0.008, 0.001, 0.0015, 0.11, 0.05 },
                { 0, 0, 0, 0, 0, 0.008, 0.001, 0.0015, 0.12, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0.134, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0.148, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0.172, 0.05 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };// 0.008有待考证
        for (int i = 1; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                k[i][j] = k[j][i];
            }
        }

        // 求混合物的11个系数
        double A = 0;
        double B = 0;
        double C = 0;
        double D = 0;
        double E = 0;
        double a = 0;
        double b = 0;
        double c = 0;
        double d = 0;
        double rfa = 0;
        double gama = 0;

        for (int i = 0; i < 10; i++) {
            B += component[i] * B0[i];
            gama += component[i] * Math.cbrt(gama0[i]);
            b += component[i] * Math.cbrt(b0[i]);
            a += component[i] * Math.cbrt(a0[i]);
            rfa += component[i] * Math.cbrt(rfa0[i]);
            c += component[i] * Math.cbrt(c0[i]);
            d += component[i] * Math.cbrt(d0[i]);
            for (int j = 0; j < 10; j++) {
                A += component[i] * component[j] * Math.sqrt(A0[i])
                        * Math.sqrt(A0[j]) * (1 - k[i][j]);
                C += component[i] * component[j] * Math.sqrt(C0[i])
                        * Math.sqrt(C0[j]) * Math.pow((1 - k[i][j]), 3);
                D += component[i] * component[j] * Math.sqrt(D0[i])
                        * Math.sqrt(D0[j]) * Math.pow((1 - k[i][j]), 4);
                E += component[i] * component[j] * Math.sqrt(E0[i])
                        * Math.sqrt(E0[j]) * Math.pow((1 - k[i][j]), 5);
            }
        }

        gama = Math.pow(gama, 3);
        b = Math.pow(b, 3);
        a = Math.pow(a, 3);
        rfa = Math.pow(rfa, 3);
        c = Math.pow(c, 3);
        d = Math.pow(d, 3);

        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.rfa = rfa;
        this.gama = gama;
    }

}
