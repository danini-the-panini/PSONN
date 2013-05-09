/**
 *
 * @author Daniel
 */
public class NeuralNetwork
{
    private double[][] inputWeights;
    private double[][] hiddenWeights;
    
    private int numInputs;
    private int numHiddenUnits;
    private int numOutputs;
    
    private Function function;

    public NeuralNetwork(int numInputs, int numHiddenUnits, int numOutputs, Function activationFunction)
    {
        // +1 for bias unit.
        this.numInputs = numInputs+1;
        this.numHiddenUnits = numHiddenUnits+1;
        
        this.numOutputs = numOutputs;
        
        inputWeights = new double[numInputs+1][numHiddenUnits];
        hiddenWeights = new double[numHiddenUnits+1][numOutputs];
        
        this.function = activationFunction;
    }
    
    public class Statistic
    {
        private double meanSquaredError;
        private double accuracy;

        public Statistic(double meanSquaredError, double accuracy)
        {
            this.meanSquaredError = meanSquaredError;
            this.accuracy = accuracy;
        }

        public double getAccuracy()
        {
            return accuracy;
        }

        public double getMeanSquaredError()
        {
            return meanSquaredError;
        }
    }
    
    /**
     * Runs the neural network on a data set.
     * @param dataSet
     * @return The MSE.
     */
    public Statistic run(DataSet dataSet)
    {
        // TODO: This function needs to return more than just the MSE. It needs to return the number of incorrect classifications as well.
        double MSE = 0;
        
        int numCorrect = 0;
        int numPatterns = dataSet.getSize();
        
        for (int p = 0; p < numPatterns; p++)
        {
            DataSet.Pattern pattern = dataSet.getPattern(p);
            
            double[] output = run(pattern);
            
            MSE += meanSquared(output, pattern.getOutput());
            
            if (closeEnough(output, pattern.getOutput(), 0.3f))
                numCorrect++;
        }
        
        return new Statistic(MSE / (double)numPatterns, (double)numCorrect/(double)numPatterns);
    }
    
    public static boolean closeEnough(double[] a, double[] b, double delta)
    {
        if (a.length != b.length) return false; // can't compare vectors of unequal length
        double temp;
        delta *= delta;
        for (int i = 0 ; i < a.length; i++)
        {
            temp = a[i] - b[i];
            if (temp > delta) return false;
        }
        return true;
    }
    
    /**
     * Run the neural network on a single pattern.
     * @param pattern
     * @return The output values.
     */
    public double[] run(DataSet.Pattern pattern)
    {
        double[] hiddenValues = new double[numHiddenUnits];
        hiddenValues[numHiddenUnits-1] = -1; // bias unit

        // Do input layer -> hidden layer
        for (int j = 0; j < numHiddenUnits-1; j++) // -1 for hidden bias unit
        {
            hiddenValues[j] = 0;
            for (int i = 0; i < numInputs; i++)
            {
                // if it's the last one, it's the bias unit.
                double x = i == numInputs-1 ? -1 : pattern.getInput(i);

                hiddenValues[j] += x * inputWeights[i][j];
            }
            hiddenValues[j] = f(hiddenValues[j]);
        }

        double[] outputValues = new double[numOutputs];

        // Do hidden layer -> output layer
        for (int k = 0; k < numOutputs; k++)
        {
            outputValues[k] = 0;
            for (int j = 0; j < numHiddenUnits; j++)
            {
                outputValues[k] += hiddenValues[j] * hiddenWeights[j][k];
            }
            outputValues[k] = f(outputValues[k]);
        }
        
        return outputValues;
    }
    
    /**
     * Set the weights from a particle's vector.
     * @param weights 
     */
    public void setWeights(double[] weights)
    {
        if (weights.length != getNumWeights())
            throw new IllegalArgumentException("Incorrect number of weights: " + String.format("expect: %d, got: %d", getNumWeights(), weights.length));
        
        // set the weights between input and hidden (inputs)*(hidden-1)
        for (int i = 0; i < numInputs; i++)
        {
            System.arraycopy(weights, i*(numHiddenUnits-1), inputWeights[i], 0, numHiddenUnits-1);
        }
        // set the wights between hidden and output (hidden)*(output)
        for (int j = 0; j < numHiddenUnits; j++)
        {
            System.arraycopy(weights, numInputs*(numHiddenUnits-1)+j*numOutputs,
                    hiddenWeights[j], 0, numOutputs);
        }
    }
    
    public int getNumWeights()
    {
        return numInputs*(numHiddenUnits-1) + numHiddenUnits*numOutputs;
    }
    
    
    // makes activation function calls look nicer in the code
    // f(x) instead of function.f(x)
    private double f(double x)
    {
        return function.f(x);
    }
    
    /**
     * Calculates the mean squared error between two vectors.
     * @param yHat The estimated values.
     * @param y The real values.
     * @return The MSE between yHat and y.
     */
    public static double meanSquared(double[] yHat, double[] y)
    {
        if (yHat.length != y.length)
            throw new IllegalArgumentException("Array length mismatch.");
        
        double sum = 0;
        double temp;
        for (int i = 0; i < y.length; i++)
        {
            temp = yHat[i] - y[i];
            sum += temp * temp;
        }
        
        return sum / y.length;
    }
}
