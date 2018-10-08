package cn.edu.cup.tanyao.network;

/**
 * 节点载荷、管段流量
 *
 * @author tanyao
 * @version 2018.09.13
 */
public class NodeFlow {
    /**
     * 简化的节点载荷数组
     */
    public double[] simpleNodeLoad;

    /**
     * 节点载荷数组
     */
    public double[] nodeLoad;

    /**
     * 节点载荷状态
     */
    public int[] nodeLoadState;

    /**
     * 节点压力
     */
    public double[] nodePressure;

    /**
     * 节点压力状态
     */
    public int[] nodePressureState;

    public void setData() {
        int count = PutIn.getRowCount(2) - 1;
        double[] nodeLoad = new double[count];
        int[] nodeLoadState = new int[count];
        double[] nodePressure = new double[count];
        int[] nodePressureState = new int[count];

        for(int i = 0; i < count; i++) {
            nodeLoad[i] = PutIn.getElement(i + 2, 4, 2);
            nodeLoadState[i] = (int) PutIn.getElement(2 + i, 5, 2);
            nodePressure[i] = PutIn.getElement(2 + i, 2, 2);
            nodePressureState[i] = (int)PutIn.getElement(2 + i, 3, 2);
        }

        int sum = 0;
        for(int i = 0; i < count; i++) {
            sum += nodeLoadState[i];
        }

        double[] simpleNodeLoad = new double[sum];
        for(int i = 0, j = 0; i < count; i++) {
            if(nodeLoadState[i] == 1) {
                simpleNodeLoad[j] = nodeLoad[i];
                j++;
            }
        }

        this.nodeLoad = nodeLoad;
        this.nodePressure = nodePressure;
        this.nodeLoadState = nodeLoadState;
        this.nodePressureState = nodePressureState;
        this.simpleNodeLoad = simpleNodeLoad;
    }

    public static void main(String[] args) {
        NodeFlow f = new NodeFlow();
        f.setData();
        for(int i = 0; i < f.simpleNodeLoad.length; i++) {
            //System.out.println(f.nodeLoad[i]);
            System.out.println(f.simpleNodeLoad[i]);
        }
    }
}
