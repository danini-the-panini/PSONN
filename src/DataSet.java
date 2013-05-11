
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Daniel
 */
public class DataSet
{
    private int numInputs;
    private int numOutputs;
    
    private String[] outputNames;
    
    private Pattern[] patterns;
    
    public class Pattern
    {
        private double[] inputs;
        private int output;

        public Pattern(double[] inputs, int output)
        {
            this.inputs = inputs;
            this.output = output;
        }
        
        public double getInput(int i)
        {
            return inputs[i];
        }
        
        public int getOutputIndex()
        {
            return output;
        }
        
        public double[] getOutput()
        {
            double[] outputs = new double[numOutputs];
            for (int i = 0; i < numOutputs; i++)
            {
                outputs[i] = i == output ? 1 : 0;
            }
            return outputs;
        }
    }

    public int getNumInputs()
    {
        return numInputs;
    }

    public int getNumOutputs()
    {
        return numOutputs;
    }
    
    public Pattern getPattern(int i)
    {
        return patterns[i];
    }
    
    public void shuffle()
    {
        for (int i = 0; i < patterns.length; i++)
        {
            Pattern temp = patterns[i];
            int r = (int)(Math.random()*patterns.length);
            patterns[i] = patterns[r];
            patterns[r] = temp;
        }
    }
    
    public DataSet getSubset(int start)
    {
        return getSubset(start, patterns.length);
    }
    
    public DataSet getSubset(int start, int end)
    {
        Pattern[] newPatterns = new Pattern[end - start];
        
        System.arraycopy(patterns, start, newPatterns, 0, end - start);
        
        return new DataSet(numInputs, numOutputs, outputNames, newPatterns);
    }
    
    public int getSize()
    {
        return patterns.length;
    }
    
    public DataSet(File file)
            throws IOException
    {
        Scanner in = new Scanner(file);
        
        try
        {
            numInputs = in.nextInt();
            numOutputs = in.nextInt();
            
            outputNames = new String[numOutputs];
            
            ArrayList<Pattern> plist = new ArrayList<Pattern>();
            while (in.hasNext())
            {
                double[] values = new double[numInputs];
                for (int i = 0; i < numInputs; i++)
                {
                    values[i] = in.nextDouble();
                }
                String outname = in.next();
                int outnum = -1;
                for (int i = 0; i < numOutputs && outnum == -1; i++)
                {
                    if (outputNames[i] == null)
                    {
                        outputNames[i] = outname;
                        outnum = i;
                    }
                    else if (outputNames[i].equals(outname))
                    {
                        outnum = i;
                    }
                }
                plist.add(new Pattern(values, outnum));
            }
            
            patterns = plist.toArray(new Pattern[0]);
        }
        catch (Exception e)
        {
            throw new IOException(file.getName() + " (Not a valid DataSet)");
        }
        finally
        {
            in.close();
        }
    }
    
    // for internal use only
    private DataSet(int numInputs, int numOutputs, String[] outputNames, Pattern[] patterns)
    {
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        this.outputNames = outputNames;
        this.patterns = patterns;
    }
    
    
}
