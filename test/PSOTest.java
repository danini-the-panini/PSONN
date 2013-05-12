import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class PSOTest
{

    /**
     * Test of optimise method, of class PSO.
     */
    @Test
    public void testOptimise()
    {
        System.out.println("optimise");
        PSO instance = new PSOImpl(500, new Topology.Ring(1), 0.72, 1.4, 1.4, 100, 30, -100, 100);
        instance.optimise();
        double[] actuals = instance.getBestParticle().getValues();
        double[] expected = {0.0,0.0};
        assertArrayEquals(expected, actuals, 0.000001);
    }

    public class PSOImpl extends PSO
    {

        public PSOImpl(int maxIterations, Topology topology, double w, double c1, double c2, double vmax, int numParticles, double lowerBound, double upperBound)
        {
            super(2, maxIterations, topology, w, c1, c2, vmax, numParticles, lowerBound, upperBound);
        }

        @Override
        protected double getFitness(double[] values)
        {
            double x = values[0];
            double y = values[1];
            return x*x + y*y;
        }

        @Override
        protected void outputStatistics(int i, double[] values)
        {
            // do nothing
        }

        @Override
        protected void finalise(double[] values)
        {
            // do nothing
        }
    }
}