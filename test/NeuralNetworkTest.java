import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class NeuralNetworkTest
{

    /**
     * Test of run method, of class NeuralNetwork.
     */
    @Test
    public void testRun_DataSetPattern()
    {
        System.out.println("run");
        NeuralNetwork instance = new NeuralNetwork(2, 3, 1, new Function.Perceptron());
        double[] weights = {
            1, 1, 0, // first input weights
            0, 1, 1, // second input weights
            0.5, 1.5, 0.5, // bias weights
            
            1, -2, 1, // hidden unit weights
            0.5         // hidden unit bias weight
        };
        instance.setWeights(weights);
        
        double[] inputs, expected, result;
        
        System.out.println("Testing 0,0");
        inputs = new double[]{0,0};
        result = instance.run(inputs);
        expected = new double[]{0};
        assertArrayEquals(expected, result, 0.0);
        
        System.out.println("Testing 0,1");
        inputs = new double[]{0,1};
        result = instance.run(inputs);
        expected = new double[]{1};
        assertArrayEquals(expected, result, 0.0);
        
        System.out.println("Testing 1,0");
        inputs = new double[]{1,0};
        result = instance.run(inputs);
        expected = new double[]{1};
        assertArrayEquals(expected, result, 0.0);
        
        System.out.println("Testing 1,1");
        inputs = new double[]{1,1};
        result = instance.run(inputs);
        expected = new double[]{0};
        assertArrayEquals(expected, result, 0.0);
        
    }

    /**
     * Test of meanSquared method, of class NeuralNetwork.
     */
    @Test
    public void testMeanSquared()
    {
        System.out.println("meanSquared");
        double[] yHat = {5, 3, 7, -2, 0, 9};
        double[] y = {7, 2, 7, 0, -3, 10};
        double expResult = 19.0/6.0;
        double result = NeuralNetwork.meanSquared(yHat, y);
        assertEquals(expResult, result, 0.0);
    }
}