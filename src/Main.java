import java.io.IOException;


public class Main {

	public static void main(String [] args) throws IOException
	{
		String test;
		DepData d;
		DParser p;
		
		for (int i = 0; i < args.length; i++)
		{
			test = args[i];
			
			if (test.equals("-i"))
			{
				Constants.inputPath = args[i + 1];
				i++;
			}
			else if (test.equals("-o"))
			{
				Constants.dotPath = args[i + 1];
				i++;
			}
			else if (test.equals("-ot"))
			{
				Constants.dotPath = args[i + 1];
				Constants.printWAW = false;
				Constants.printWAR = false;
				i++;
			}
			else if (test.equals("-O"))
			{
				Constants.graphPath = args[i + 1];
				Constants.generateDotGraph = true;
				i++;				
			}
			else if (test.equals("-Ot"))
			{
				Constants.graphPath = args[i + 1];
				Constants.generateDotGraph = true;
				Constants.printWAW = false;
				Constants.printWAR = false;
				i++;				
			}
			else if (test.equals("-h"))
			{
				Constants.generateHistogram = true;
				Constants.doCluster = true;
				Constants.histoPath = args[i + 1];
				i++;
			}
			else if (test.equals("-r"))
			{
				Constants.printLinearReport = true;
			}
			else if (test.equals("-m"))
			{
				Constants.printMetrics = true;
			}
			else if (test.equals("-st"))
			{
				Constants.printTrueSort = true;
			}
			else if (test.equals("-sf"))
			{
				Constants.printFalseSort = true;
			}
			else if (test.equals("-cf"))
			{
				Constants.doCluster = true;
				Constants.clusterType = DepType.FALSE;
			}
			else if (test.equals("-ct"))
			{
				Constants.doCluster = true;
				Constants.clusterType = DepType.TRUE;
			}
			else if (test.equals("--topbottom"))
			{
				
			}
		}
		
		if (Constants.inputPath != null)
			p = new DParser(Constants.inputPath);
		else
		{
			System.out.println("A valid input file must be specified (use '-i INPUT').");
			return;
		}
		
		d = p.parseFromFile();
		d.solveDependencies();
		d.printAllCalls();
		if (Constants.printMetrics) d.printDepMetrics();
		if (Constants.printLinearReport) d.printLinearDepGraph();		
		if (Constants.printFalseSort) d.printFalseSort();
		if (Constants.printTrueSort) d.printTrueSort();
		if (Constants.generateHistogram)
			if (Constants.histoPath != null)
				d.histoPlot();
			else
			{
				System.out.println("You should specify the histogram file path after \"-h\"");
				return;
			}
		if (Constants.generateDotGraph)
			if (Constants.graphPath != null)
				d.printDotGraph();
			else
			{
				System.out.println("You should specify the dependency graph file path");
				return;			
			}
	}	
}
