
import java.io.File;
import java.io.IOException;

/**
 *
 * @author daniel
 */
public class Main
{
    public static void main(String[] args)
    {
        
        DataSet data = null;
        try
        {
            data = new DataSet(new File("data-iris.txt"));
        } catch (IOException ex)
        {
            System.out.println("Error reading file: " + ex.toString());
        }
        if (data == null)
        {
            System.exit(1);
        }
        
        int maxIterations = 1000;
        int numHiddenLayers = 30;
        Function activationFunction = new Function.Sigmoid();
        Topology topology = new Topology.Ring(1);
        double w = 0.72, c1 = 1.4, c2 = 1.4, vmax = 100;
        int numParticles = 30;
        double lowerBound = -1, upperBound= 1;
        
        TestPSO test = new TestPSO(maxIterations, topology, w, c1, c2, vmax, numParticles, lowerBound, upperBound);
        
        test.optimise();
        
        PSONN pso = new PSONN(data, maxIterations, numHiddenLayers, activationFunction, topology, w, c1, c2, vmax, numParticles, lowerBound, upperBound);
        
        System.out.println("\nCommencing optimisation...");
        pso.optimise();
        
        NeuralNetwork.Statistic tstat = pso.getTrainingStatistic();
        
        System.out.printf("MSE: %g, ACC: %.1f\n", tstat.getMeanSquaredError(), tstat.getAccuracy()*100);
        
        System.out.println("\nCommence generalisation test...");
        tstat = pso.test();
        
        System.out.printf("MSE: %g, ACC: %.1f\n", tstat.getMeanSquaredError(), tstat.getAccuracy()*100);
    }
}
