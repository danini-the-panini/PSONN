/**
 * Represents a Particle Swarm Optimisation specifically catered for training
 * a Neural Network.
 * @author Daniel
 */
public class PSONN extends PSO
{
    
    private NeuralNetwork nn;
    
    private DataSet trainingData, testingData;
    
    private NeuralNetwork.Statistic tstat = null;

    /**
     * Creates a PSO for training a Neural Network
     * @param dataSet Data Set to use for training and testing the Neural Network.
     * @param maxIterations Total number of iterations to go through (stopping condition)
     * @param numHiddenLayers Number of hidden layers in the Neural Network
     * @param activationFunction The Neuron Activation Function for the Neural Network.
     * @param topology The topology (e.g. Ring or Star) to use for grouping particles.
     * @param w weight/momentum factor
     * @param c1 cognitive coefficient
     * @param c2 social coefficient
     * @param numParticles The number of particles to use.
     * @param lowerBound Lower bound for sampling particle positions.
     * @param upperBound Upper bound for sampling particle positions.
     */
    public PSONN(DataSet dataSet, int maxIterations, int numHiddenLayers,
            Function activationFunction, Topology topology, double w,
            double c1, double c2, double vmax, int numParticles,
            double lowerBound, double upperBound)
    {
        super(
                // Dimensions := (I+1)J + (J+1)K
                (dataSet.getNumInputs()+1)*numHiddenLayers
                + (numHiddenLayers+1)*dataSet.getNumOutputs(),
                
                maxIterations, topology, w, c1, c2, vmax, numParticles,
                lowerBound, upperBound);
        
        dataSet.shuffle();
        int sixtyPercent = (int)(dataSet.getSize() * 0.6);
        
        trainingData = dataSet.getSubset(0, sixtyPercent);
        testingData = dataSet.getSubset(sixtyPercent);
        
        nn = new NeuralNetwork(dataSet.getNumInputs(), numHiddenLayers, dataSet.getNumOutputs(), activationFunction);
    }
    
    @Override
    protected double getFitness(double[] values)
    {
        nn.setWeights(values);
        tstat = nn.run(trainingData);
        // TODO: save this statistic to file.
        return tstat.getMeanSquaredError();
    }
    
    public NeuralNetwork.Statistic getTrainingStatistic()
    {
        return tstat;
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
    
    /**
     * Test the Neural Network.
     * @return The result of the test.
     */
    public NeuralNetwork.Statistic test()
    {
        return nn.run(testingData);
    }
}
