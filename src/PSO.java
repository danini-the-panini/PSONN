
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
    
    /**
     * Gets the fitness of a particular vector.
     * @param v The vector to calculate the fitness of.
     * @return The fitness of the vector.
     */
    protected abstract double getFitness(double[] values);
    
    // update each particle's fitness using the overridden getFitness function.
    private void updateFitness()
    {
        for (int j = 0; j < particles.length; j++)
        {
            double fitness = getFitness(particles[j].getValues());
            particles[j].updateFitness(fitness);
        }
        topology.update();
        
        //printBest();
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
        for (int i = 0; i < maxIterations; i++)
        {
            // update each particle's position
            for (int j = 0; j < particles.length; j++)
                particles[j].update(w, c1, c2, vmax, topology.getBest(j));
            
            updateFitness();
        }
        
        printBest();
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
    
    double lastBest = Double.POSITIVE_INFINITY;
    public void printBest()
    {
        Particle p = getBestParticle();
        double pbest = p.getBestFitness();
        
        double[] x = p.getBestValues();
        
        System.out.print("fitness: " + pbest + ", v = ");
        for (int i = 0; i < x.length; i++)
            System.out.print(x[i] + (i == x.length-1 ? "." : ", "));
        System.out.println();
    }
}
