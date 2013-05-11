
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author daniel
 */
public class Main
{
    public static final String prompt = "> ";
    
    public static Scanner in = new Scanner(System.in);
    
    public static boolean isOk(int input, int min, int max)
    {
        return ((min <= Integer.MIN_VALUE || input >= min) && (max >= Integer.MAX_VALUE || input <= max));
    }
    
    public static boolean isOk(double input, double min, double max)
    {
        return ((min <= Double.NEGATIVE_INFINITY || input >= min) && (max >= Double.POSITIVE_INFINITY || input <= max));
    }
    
    public static int readInteger(String question, int min, int max)
    {
        int input = 0;
        boolean ok = false;
        if (question != null)
            System.out.println(question);
        while (!ok)
        {
            try
            {
                System.out.print(prompt);
                ok = isOk(input = Integer.parseInt(in.nextLine()), min, max);
                if (!ok)
                {
                    System.out.printf("Invalid input, value must be in the range [%d,%d].\n",min,max);
                }
            }
            catch (Exception e)
            {
                System.out.println("Invalid input");
            }
        }
        return input;
    }
    
    public static double readNumber(String question, double min, double max)
    {
        double input = 0;
        boolean ok = false;
        if (question != null)
            System.out.println(question);
        while (!ok)
        {
            try
            {
                System.out.print(prompt);
                ok = isOk(input = Double.parseDouble(in.nextLine()), min, max);
                if (!ok)
                {
                    System.out.printf("Invalid input, value must be in the range [%d,%d].\n",min,max);
                }
            }
            catch (Exception e)
            {
                System.out.println("Invalid input");
            }
        }
        return input;
    }
    
    public static int readChoice(String question, String[] choices)
    {
        System.out.println(question);
        for (int i = 0; i < choices.length; i++)
        {
            System.out.println("\t" + (i+1) + ". " + choices[i]);
        }
        return readInteger(null, 1, choices.length)-1;
    }
    
    public static <T> T readObject(String question, T[] choices)
    {
        String[] choiceNames = new String[choices.length];
        for (int i = 0; i < choices.length; i++)
        {
            choiceNames[i] = choices[i].getClass().getSimpleName();
        }
        return choices[readChoice(question, choiceNames)];
    }
    
    public static String read(String question)
    {
        System.out.println(question);
        System.out.print(prompt);
        return in.nextLine();
    }
    
    public static void main(String[] args)
    {
        
        String filename = read("Enter the path to a data file.");
        
        DataSet data = null;
        PrintWriter writer = null;
        try
        {
            data = new DataSet(new File(filename));
            writer = new PrintWriter(filename+".results");
        } catch (IOException ex)
        {
            System.out.println("Error reading file: " + ex.getMessage());
        }
        if (data == null)
        {
            System.exit(1);
        }
        
        int maxIterations = readInteger("How many iterations? (Stopping condition)", 1, Integer.MAX_VALUE);
        int numHiddenUnits = readInteger("How many hidden units?", 1, Integer.MAX_VALUE);
        Function activationFunction = readObject("What activation function to use?",
                new Function[]{
                    new Function.Sigmoid(),
                    new Function.Linear()
                }
        );
        Topology topology;
        int topoChoice = readChoice("What topology to use?",
                new String[]{
                    "Star",
                    "Ring"
                }
        );
        switch (topoChoice)
        {
            case 0:
                topology = new Topology.Star();
                break;
            case 1:
                topology = new Topology.Ring(readInteger("How big should the neighbourhood be?", 0, Integer.MAX_VALUE));
                break;
            default:
                topology = null; // shouldn't happen
                break;
        }
        double w = readNumber("What is the inertia wieght?", 0.0, Double.POSITIVE_INFINITY),
                c1 = readNumber("What is the cognitive acceleration coefficient?", 0.0, Double.POSITIVE_INFINITY),
                c2 = readNumber("What is the social acceleration coefficient?", 0.0, Double.POSITIVE_INFINITY),
                vmax = readNumber("What is the maximum velocity?", 0.0, Double.POSITIVE_INFINITY);
        int numParticles = readInteger("How many particles?", 1, Integer.MAX_VALUE);
        double lowerBound = readNumber("What is the lower bound for particle initialisation?", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
                upperBound= readNumber("What is the upper bound for particle initialisation?", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        // Testing the PSO.
        /*TestPSO test = new TestPSO(maxIterations, topology, w, c1, c2, vmax, numParticles, lowerBound, upperBound);
        
        test.optimise();*/
        
        PSONN pso = new PSONN(data, maxIterations, numHiddenUnits, activationFunction, topology, w, c1, c2, vmax, numParticles, lowerBound, upperBound);
        pso.setWriter(writer);
        NeuralNetwork.Statistic tstat;
        
        System.out.println("\nTraining...");
        pso.optimise();
        
        tstat = pso.getTrainingStatistic();
        System.out.println("Training result:");
        System.out.printf("\tMSE: %g\n\tAccuracy: %.1f%%\n", tstat.getMeanSquaredError(), tstat.getAccuracy()*100);
        
        tstat = pso.getTestingStatistic();
        System.out.println("Test result:");
        System.out.printf("\tMSE: %g\n\tAccuracy: %.1f%%\n", tstat.getMeanSquaredError(), tstat.getAccuracy()*100);
    }
}
