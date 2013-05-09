/**
 * Represents a particle or entity in a Particle Swarm Optimisation.
 * @author daniel
 */
public class Particle
{
    private double[] values;
    private double fitness;
    private double[] pbest;
    private double pbestFitness;
    
    private double[] velocity;

    /**
     * Creates a new particle with number of values, and an upper and lower bound from which to sample initial values.
     * @param numValues
     * @param lowerBound
     * @param upperBound 
     */
    public Particle(int numValues, double lowerBound, double upperBound)
    {
        // x[i] ~U(lowerBound,upperBound)
        values = new double[numValues];
        
        velocity = new double[numValues];
        
        
        double range = upperBound - lowerBound;
        for (int i = 0; i < numValues; i++)
        {
            values[i] = range*Math.random() + lowerBound;
            velocity[i] = 0; // initialize velocities to zero.
        }
        
        pbest = new double[values.length];
        System.arraycopy(values, 0, pbest, 0, values.length);
        fitness = Double.POSITIVE_INFINITY;
        pbestFitness = fitness;
    }
    
    /**
     * Update the particle's position.
     * @param w Weight value.
     * @param c1 Cognitive coefficient.
     * @param c2 Social coefficient.
     * @param gbest Global/Local best.
     */
    public void update(double w, double c1, double c2, double vmax,
            double[] gbest)
    {
        // r1, r2 ~U(0,1)
        double r1 = Math.random();
        double r2 = Math.random();
        
        // v(t) = w * v(t-1) + c1 * r1 * (pbest - x(t)) + c2 * r2 * (gbest - x(t))
        // NOTE: gbest here may also refer to lbest, based on PSO topology.
        velocity = add(add(multiply(velocity, w), multiply(subtract(pbest, values), c1*r1)),multiply(subtract(gbest, values), c2*r2));
        
        // clamp velocity to vmax
        double lensq = lengthSquared(velocity);
        if (lensq > vmax*vmax)
        {
            velocity = multiply(velocity,1.0/(Math.sqrt(lensq)));
        }
        
        // x(t+1) = x(t) + v(t)
        values = add(values,velocity);
    }

    public double[] getValues()
    {
        return values;
    }

    public double getFitness()
    {
        return fitness;
    }

    public double getBestFitness()
    {
        return pbestFitness;
    }
    
    public double[] getBestValues()
    {
        return pbest;
    }
    
    /**
     * Update the particle's personal best.
     * @param fitness New mean square error.
     */
    public void updateFitness(double fitness)
    {
        this.fitness = fitness;
        if (fitness < pbestFitness)
        {
            System.arraycopy(values, 0, pbest, 0, values.length);
            pbestFitness = fitness;
        }
    }
    
    // subtract two vectors
    public static double[] subtract(double[] a, double[] b)
    {
        if (a.length != b.length) throw new IllegalArgumentException("Vector component count mismatch.");
        
        double[] result = new double[a.length];
        
        for (int i = 0; i < a.length; i++)
        {
            result[i] = a[i]-b[i];
        }
        
        return result;
    }
    
    // add two vectors
    public static double[] add(double[] a, double[] b)
    {
        if (a.length != b.length) throw new IllegalArgumentException("Vector component count mismatch.");
        
        double[] result = new double[a.length];
        
        for (int i = 0; i < a.length; i++)
        {
            result[i] = a[i] + b[i];
        }
        
        return result;
    }
    
    // multiply a vector with a scalar
    public static double[] multiply(double[] a, double b)
    {
        double[] result = new double[a.length];
        
        for (int i = 0; i < a.length; i++)
        {
            result[i] = a[i]*b;
        }
        
        return result;
    }
    
    // computes |v|^2
    public static double lengthSquared(double[] v)
    {
        double lensq = 0;
        for (int i = 0; i < v.length; i++)
            lensq += v[i]*v[i];
        return lensq;
    }
}
