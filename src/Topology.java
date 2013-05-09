/**
 *
 * @author Daniel
 */
public abstract class Topology
{
    protected Particle[] population;
    
    public abstract void update();
    
    public abstract double[] getBest(int i);
    
    public void setPopulation(Particle[] population)
    {
        this.population = population;
    }
    
    public static class Star extends Topology
    {
        private int best = 0;

        @Override
        public void update()
        {
            best = 0;
            for (int i = 1; i < population.length; i++)
            {
                if (population[i].getFitness() < population[best].getFitness())
                {
                    best = i;
                }
            }
        }

        @Override
        public double[] getBest(int i)
        {
            return population[best].getValues();
        }
        
    }
    
    public static class Ring extends Topology
    {
        private int neighbourhood;
        private int[] best;

        public Ring(int neighbourhood)
        {
            this.neighbourhood = neighbourhood;
        }

        @Override
        public void setPopulation(Particle[] population)
        {
            super.setPopulation(population);
            best = new int[population.length];
        }

        @Override
        public void update()
        {
            for (int i = 0; i < population.length; i++)
            {
                best[i] = 0;
                for (int j = 1; j <= neighbourhood; j++)
                {
                    int k = wrap(i-j);
                    if (population[k].getFitness() < population[best[i]].getFitness())
                    {
                        best[i] = k;
                    }
                    k = wrap(i+j);
                    if (population[k].getFitness() < population[best[i]].getFitness())
                    {
                        best[i] = k;
                    }
                }
            }
        }
        
        private int wrap(int i)
        {
            if (i < 0) return wrap(i + population.length);
            return i % population.length;
        }

        @Override
        public double[] getBest(int i)
        {
            return population[best[i]].getValues();
        }
        
    }
}
