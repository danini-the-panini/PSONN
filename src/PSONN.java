
import java.io.PrintWriter;

/**
 * Represents a Particle Swarm Optimisation specifically catered for training
 * a Neural Network.
 * @author Daniel
 */
public class PSONN extends PSO
{
    
    private NeuralNetwork nn;
    
    private DataSet trainingData, testingData;
    
    private NeuralNetwork.Statistic trainingStat = null, testingStat = null;

    /**
     * Creates a PSO for training a Neural Network
     * @param dataSet Data Set to use for training and testing the Neural Network.
     * @param maxIterations Total number of iterations to go through (stopping condition)
     * @param numHiddenUnits Number of hidden units in the Neural Network
     * @param activationFunction The Neuron Activation Function for the Neural Network.
     * @param topology The topology (e.g. Ring or Star) to use for grouping particles.
     * @param w weight/momentum factor
     * @param c1 cognitive coefficient
     * @param c2 social coefficient
     * @param numParticles The number of particles to use.
     * @param lowerBound Lower bound for sampling particle positions.
     * @param upperBound Upper bound for sampling particle positions.
     */
    public PSONN(DataSet dataSet, int maxIterations, int numHiddenUnits,
            Function activationFunction, Topology topology, double w,
            double c1, double c2, double vmax, int numParticles,
            double lowerBound, double upperBound)
    {
        super(
                // Dimensions := (I+1)J + (J+1)K
                (dataSet.getNumInputs()+1)*numHiddenUnits
                + (numHiddenUnits+1)*dataSet.getNumOutputs(),
                
                maxIterations, topology, w, c1, c2, vmax, numParticles,
                lowerBound, upperBound);
        
        dataSet.shuffle();
        int sixtyPercent = (int)(dataSet.getSize() * 0.6);
        
        trainingData = dataSet.getSubset(0, sixtyPercent);
        testingData = dataSet.getSubset(sixtyPercent);
        
        nn = new NeuralNetwork(dataSet.getNumInputs(), numHiddenUnits, dataSet.getNumOutputs(), activationFunction);
    }
    
    @Override
    protected double getFitness(double[] values)
    {
        nn.setWeights(values);
        return nn.run(trainingData).getMeanSquaredError();
    }

    @Override
    protected void outputStatistics(int i, double[] values)
    {
        nn.setWeights(values);
        trainingStat = nn.run(trainingData);
        testingStat = nn.run(testingData);
        if (writer != null)
        {
            writer.printf("%d\t%g\t%.1f%%\t%g\t%.1f%%\n", i,
                    trainingStat.getMeanSquaredError(),
                    trainingStat.getAccuracy()*100,
                    testingStat.getMeanSquaredError(),
                    testingStat.getAccuracy()*100);
        }
    }

    @Override
    protected void finalise(double[] values)
    {
        nn.setWeights(getBestParticle().getBestValues());
        trainingStat = nn.run(trainingData);
        testingStat = nn.run(testingData);
    }
    
    public NeuralNetwork.Statistic getTrainingStatistic()
    {
        return trainingStat;
    }
    
    public NeuralNetwork.Statistic getTestingStatistic()
    {
        return testingStat;
    }
    
    /**
     * Get the NeuralNetwork from this PSO. This can be called at any time but
     * should generally be called after optimisation is complete so as to
     * obtain the trained neural network.
     * @return The NeuralNetwork.
     */
    public NeuralNetwork getNeuralNetwork()
    {
        return nn;
    }
}
