/**
 *
 * @author Daniel
 */
public class TestPSO extends PSO
{

    public TestPSO(int maxIterations, Topology topology, double w, double c1, double c2, double vmax, int numParticles, double lowerBound, double upperBound)
    {
        super(2, maxIterations, topology, w, c1, c2, vmax, numParticles, lowerBound, upperBound);
    }

    @Override
    protected double getFitness(double[] values)
    {
        double x = values[0] - 4;
        double y = values[1] + 2;
        return x*x + y*y;
    }
    
}