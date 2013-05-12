/**
 *
 * @author Daniel
 */
public abstract class Function
{
    public abstract double f(double x);
    
    public static class Sigmoid extends Function
    {

        @Override
        public double f(double x)
        {
            return 1.0/(1.0 + Math.exp(-x));
        }
        
    }
    
    public static class Linear extends Function
    {

        @Override
        public double f(double x)
        {
            return x; // ???
        }
        
    }
    
    public static class Perceptron extends Function
    {

        @Override
        public double f(double x)
        {
            return x < 0 ? 0 : 1;
        }
        
    }
    
    
}
