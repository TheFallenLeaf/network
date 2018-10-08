package cn.edu.cup.tanyao.network;

/**
 * 管段数据类
 * @author tanyao
 * @version 2018.09.12
 */
public class PipeData {
    /**
     * 管段摩阻
     */
    public double[] frictionFactor;

    /**
     * 管段长度,m
     */
    public double[] pipeLength;

    /**
     * 管径,m
     */
    public double[] diameter;

    public void setData() {
        int count = PutIn.getRowCount(1) - 1;

        double[] frictionFactor = new double[count];
        double[] pipeLength = new double[count];
        double[] diameter = new double[count];

        FrictionFactor F = new FrictionFactor();

        for (int i = 0; i < count; i++) {
            diameter[i] = PutIn.getElement(i + 2, 5, 1);
            pipeLength[i] = PutIn.getElement(i + 2, 4, 1);

            F.setDiameter(diameter[i]);
            frictionFactor[i] = F.calculateFrictionFactor();

            this.diameter = diameter;
            this.frictionFactor = frictionFactor;
            this.pipeLength = pipeLength;
        }
    }

    public void setDiameter(double[] diameter_) {
        this.diameter = diameter_;
    }

    public void setFrictionFactor(double[] frictionFactor_) {
        this.frictionFactor = frictionFactor_;
    }

    public void setPipeLength(double[] pipeLength_) {
        this.pipeLength = pipeLength_;
    }

    public static void main(String[] args) {
        PipeData P = new PipeData();
        P.setData();
        for(int i = 0; i < P.diameter.length; i++) {
            System.out.println(P.frictionFactor[i]);
        }
    }
}
