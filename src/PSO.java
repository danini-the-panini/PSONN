
import java.io.PrintWriter;


/**
 * Generic Particle Swarm Optimisation Algorithm.
 * To utilise, create a subclass of this class, and override the getFitness(double[]) function.
 * 
 * @author daniel
 */
public abstract class PSO
{
    private int maxIterations;
    private Particle[] particles;
    private double vmax;
    
    private double w, c1, c2; // weight, cognitive and social coefficients.
    
    private Topology topology;
    
    protected PrintWriter writer = null;
    
    /**
     * Gets the fitness of a particular vector.
     * @param v The vector to calculate the fitness of.
     * @return The fitness of the vector.
     */
    protected abstract double getFitness(double[] values);
    
    protected abstract void outputStatistics(int i, double[] values);
    
    protected abstract void finalise(double[] values);
    
    // update each particle's fitness using the overridden getFitness function.
    private void updateFitness()
    {
        for (int j = 0; j < particles.length; j++)
        {
            double fitness = getFitness(particles[j].getValues());
            particles[j].updateFitness(fitness);
        }
        topology.update();
    }

    /**
     * Creates a Particle Swarm Optimisation
     * @param dimensions Number of dimensions for each particle.
     * @param maxIterations Total number of iterations to go through (stopping condition)
     * @param topology The topology (e.g. Ring or Star) to use for grouping particles.
     * @param w weight/momentum factor
     * @param c1 cognitive coefficient
     * @param c2 social coefficient
     * @param vmax maximum velocity
     * @param numParticles The number of particles to use.
     * @param lowerBound Lower bound for sampling particle positions.
     * @param upperBound Upper bound for sampling particle positions.
     */
    public PSO(int dimensions, int maxIterations, Topology topology, double w,
            double c1, double c2, double vmax, int numParticles,
            double lowerBound, double upperBound)
    {
        this.topology = topology;
        this.w = w;
        this.c1 = c1;
        this.c2 = c2;
        this.vmax = vmax;
        this.maxIterations = maxIterations;
        
        particles = new Particle[numParticles];
        
        for (int i = 0; i < particles.length; i++)
        {
            particles[i] = new Particle(dimensions, lowerBound, upperBound);
        }
        
        topology.setPopulation(particles);
    }
 
    /**
     * Runs the Particle Swarm Optimisation.
     */
    public void optimise()
    {
        
        // first run to get the initial fitness
        updateFitness();
        
        // commence particle swarm optimisation!
        for (int i = 1; i <= maxIterations; i++)
        {
            // update each particle's position
            for (int j = 0; j < particles.length; j++)
            {
                particles[j].update(w, c1, c2, vmax, topology.getBest(j));
            }
            
            updateFitness();
            
            outputStatistics(i, getCurrentBestParticle().getValues());
        }
        
        finalise(getBestParticle().getBestValues());
        
        if (writer != null)
            writer.flush();
    }
    
    public Particle getCurrentBestParticle()
    {
        int best = 0;
        for (int i = 0; i < particles.length; i++)
        {
            if (particles[i].getFitness() < particles[best].getFitness())
                best = i;
        }
        return particles[best];
    }
    
    public Particle getBestParticle()
    {
        int best = 0;
        for (int i = 0; i < particles.length; i++)
        {
            if (particles[i].getBestFitness() < particles[best].getBestFitness())
                best = i;
        }
        return particles[best];
    }

    /**
     * Sets a destination to output the PSO's training statistics.
     * @param writer 
     */
    public void setWriter(PrintWriter writer)
    {
        this.writer = writer;
    }
}
