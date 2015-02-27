import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class DepData {
	private ArrayList<Call> calls;
	private ArrayList<Set<Call>> sortedList;
	private ArrayList<Set<Call>> trulySortedList;
	private ColorCode colorCode;
	
	
	public DepData ()
	{
		calls = new ArrayList<Call>();
		colorCode = new ColorCode();
	}
	
	public DepData (Collection<Call> newCalls)
	{
		calls = new ArrayList<Call>(newCalls);
		//maximum = new HashSet<Call>();
		colorCode = new ColorCode();
	}
	
	public void addCall (Call newCall)
	{
		calls.add(newCall);
	}
	
	public void removeCall (Call toRemove)
	{
		calls.remove(toRemove);
	}	
	
	public void printDotGraph() throws IOException {
		Writer writer;
		
		if (Constants.dotPath == null)
			Constants.dotPath = System.getProperty("user.dir") + "/.dotswap";
		
		Path p = Paths.get(Constants.dotPath);
		
		writer = Files.newBufferedWriter(p, Charset.defaultCharset());
		
		writer.write("digraph AllDep {\n");
		writer.write("\trankdir=" + Constants.graphOrientation + ";\n");
		writer.write("\tcompound=\"true\";\n\n");
		
		if (Constants.doCluster)
			switch (Constants.clusterType) {
			case TRUE:
				clusterDotPrint(writer, trulySortedList);			
				break;
			case FALSE:
				clusterDotPrint(writer, sortedList);
				break;
			default:
				break;
			}
		else
			simpleDotPrint(writer);
		
		printColorMapping(writer, 1);
		
		writer.write("}\n");
		writer.close();
		
		if (Constants.generateDotGraph)
			generateDotGraph(p);
	}

	private void generateDotGraph(Path p) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		list.add("dot");
		list.add(p.toString());
		list.add(Constants.imgFormat.toString());
		list.add("-o");
		list.add(Constants.graphPath);
		
		ProcessBuilder builder = new ProcessBuilder(list);

		System.out.println(">> Executing dot as the following:");
		System.out.println("\t" + builder.command().toString());
		
		builder.start();
	}

	private void printColorMapping(Writer writer, int tabDepth) throws IOException {
		for (Call c: calls)
		{
			for (int i = 0; i < tabDepth; i++)
				writer.write("\t");
			writer.write(c + " [color=\"" + colorCode.getColor(c.procedureName) + "\", style=filled]\n");
		}
	}

	private void clusterDotPrint(Writer writer, ArrayList<Set<Call>> list) throws IOException {
		int counter = 0;
		
		for (Set<Call> s: list)
		{
			writer.write("\tsubgraph cluster" + counter++ + "{\n");
			writer.write("\t\tstyle=dashed;\n");
			simpleDotPrint(writer, s);
			writer.write("\t}\n\n");
		}
	}

	private void simpleDotPrint(Writer writer, Set<Call> set) throws IOException {
		for (Call c: set)
		{
			boolean printed = false;
			
			//Printing RAW dependencies
			if (Constants.printRAW) for (Call d: c.getRAWDependencies())
			{
				printed = true;
				writer.write("\t\t" + d + " -> " + c + " [color=\"#ffa193\"];\n"); //Paint in red
			}
			//Printing WAW dependencies
			if (Constants.printWAW) for (Call d: c.getWAWDependencies())
			{
				printed = true;
				writer.write("\t\t" + d + " -> " + c + " [color=\"#93c7ff\"];\n"); //Paint in blue
			}
			//Printing WAR dependencies
			if (Constants.printWAR) for (Call d: c.getWARDependencies())
			{
				printed = true;
				writer.write("\t\t" + d + " -> " + c + " [color=\"#acff93\"];\n"); //Paint in green
			}
			
			if (!printed)
				writer.write("\t\t" + c.toString() + ";\n");
		}
	}

	private void simpleDotPrint(Writer writer) throws IOException {
		for (Call c: calls)
		{
			boolean printed = false;
			
			//Printing RAW dependencies
			if (Constants.printRAW) for (Call d: c.getRAWDependencies())
			{
				printed = true;
				writer.write("\t\t" + d + " -> " + c + " [color=\"#ffa193\"];\n"); //Paint in red
			}
			//Printing WAW dependencies
			if (Constants.printWAW) for (Call d: c.getWAWDependencies())
			{
				printed = true;
				writer.write("\t\t" + d + " -> " + c + " [color=\"#93c7ff\"];\n"); //Paint in blue
			}
			//Printing WAR dependencies
			if (Constants.printWAR) for (Call d: c.getWARDependencies())
			{
				printed = true;
				writer.write("\t\t" + d + " -> " + c + " [color=\"#acff93\"];\n"); //Paint in green
			}
			
			if (!printed)
				writer.write("\t\t" + c.toString() + ";\n");				
		}
	}

	public void printAllCalls() {
		System.out.println(">> Listing of all parsed calls (" + calls.size() + " in total):");
		
		for (Call c: calls)
		{
			System.out.println("\t" + c.toString());
		}		
	}

	public void printLinearDepGraph() {
		System.out.println(">> Dependency Report");
		for (Call c: calls)
		{
			System.out.println("\t" + c.toString());
			
			System.out.println("\t\tRAW Dependencies (" + c.getRAWDependencies().size() + " in total):");
			for (Call d: c.getRAWDependencies())
			{
				System.out.println("\t\t" + d.toString());
			}
			
			System.out.println("\t\tWAW Dependencies (" + c.getWAWDependencies().size() + " in total):");
			for (Call d: c.getWAWDependencies())
			{
				System.out.println("\t\t" + d.toString());
			}
			
			System.out.println("\t\tWAR Dependencies (" + c.getWARDependencies().size() + " in total):");
			for (Call d: c.getWARDependencies())
			{
				System.out.println("\t\t" + d.toString());
			}
		}
	}

	public void solveDependencies() {
		for (Call c: calls)
		{
			for (Call d: calls)
			{
				if (d != c && d.getCallNumber() < c.getCallNumber())
				{
					//RAW Test
					for (Integer pos: d.getWrites())
					{
						if (c.readsFrom(pos))
						{
							c.addRAW(d);
							break;
						}
					}
					//WAW Test
					for (Integer pos: d.getWrites())
					{
						if (c.writesTo(pos))
						{
							c.addWAW(d);
							break;
						}
					}
					//WAR Test
					for (Integer pos: d.getReads())
					{
						if (c.writesTo(pos))
						{
							c.addWAR(d);
							break;
						}
					}					
				}
			}
		}
		
		clearUntrueDeps();
		topologicallySort();
		trueTopologicalSort();
	}
	
	private void topologicallySort()
	{
		sortedList = new ArrayList<Set<Call>>();
		HashSet<Call> aliveNodes = new HashSet<Call>(calls);
		
		while (!aliveNodes.isEmpty())
		{
			HashSet<Call> freeNodes = new HashSet<Call>();
			
			for (Call c: aliveNodes)
			{				
				if (!c.dependsOfAny(aliveNodes))
					freeNodes.add(c);
			}
			
			aliveNodes.removeAll(freeNodes);
			sortedList.add(freeNodes);
		}	
	}
	
	private void trueTopologicalSort()
	{
		trulySortedList = new ArrayList<Set<Call>>();
		HashSet<Call> aliveNodes = new HashSet<Call>(calls);
		
		while (!aliveNodes.isEmpty())
		{
			HashSet<Call> freeNodes = new HashSet<Call>();
			
			for (Call c: aliveNodes)
			{				
				if (!c.trulyDependsOfAny(aliveNodes))
					freeNodes.add(c);
			}
			
			aliveNodes.removeAll(freeNodes);
			trulySortedList.add(freeNodes);
		}			
	}

	private void clearUntrueDeps() {
		for (Call c: calls)
			for (Call d: c.getRAWDependencies())
				c.removeAllFromRAW(d.getRAWDependencies());
	}

	public void printDepMetrics() {
		int effectiveFalseDeps = 0;
		int totalTrueDeps = 0;
		HashSet<Call> auxSet;
		DecimalFormat decFor = new DecimalFormat("##.#%");
		
		for (Call c: calls)
		{
			auxSet = c.getWARDependencies();
			auxSet.addAll(c.getWAWDependencies());
			auxSet.removeAll(c.getAllRAWDependencies());
			
			effectiveFalseDeps += auxSet.size();
			totalTrueDeps += c.getRAWDependencies().size();
		}
		
		System.out.println(">> Dependency metrics (" + (effectiveFalseDeps + totalTrueDeps) + " in total):");
		System.out.println("\tNumber of effective false dependencies:\t" + effectiveFalseDeps + "\t(" + decFor.format((double)effectiveFalseDeps/(totalTrueDeps + effectiveFalseDeps)) + ")");
		System.out.println("\tNumber of true dependencies:\t\t" + totalTrueDeps + "\t(" + decFor.format((double )totalTrueDeps / (totalTrueDeps + effectiveFalseDeps)) + ")");
	}

	public void printFalseSort() {
		int counter = 0;
		
		System.out.println(">> Topological sort considering all dependencies:");
		for (Set<Call> s: sortedList)
		{
			System.out.println("\t#" + counter++ + ": " + s);
		}
	}

	public void printTrueSort() {		
		int counter = 0;
		
		System.out.println(">> Topological sort considering only true dependencies:");		
		for (Set<Call> s: trulySortedList)
		{
			System.out.println("\t#" + counter++ + ": " + s);
		}
	}
	
	public void histoPlot() throws IOException {
		Writer writerData, writerDescriptor;
		
		if (Constants.histoDataPath == null)
			Constants.histoDataPath = ".histoData";
		if (Constants.histoDescriptorPath == null)
			Constants.histoDescriptorPath = ".histoDescriptor";		
			
		Path pData = Paths.get(Constants.histoDataPath);
		Path pDescriptor = Paths.get(Constants.histoDescriptorPath);		
		
		writerData = Files.newBufferedWriter(pData, Charset.defaultCharset());
		writerDescriptor = Files.newBufferedWriter(pDescriptor, Charset.defaultCharset());
						
		createHistData(writerData);
		createHistoDescriptor(writerDescriptor);
		callGnuPlot();
	}
	
	private void createHistData(Writer writer) throws IOException {
		ArrayList<Set<Call>> list;
		
		list = Constants.clusterType == DepType.FALSE ? sortedList : trulySortedList;
		
		for (int i = 0; i < list.size(); i++)
		{
			Set<Call> s = list.get(i);			
			writer.write(i + "\t" + s.size() + "\n");
		}
		
		writer.close();
	}

	private void createHistoDescriptor(Writer writer) throws IOException {		
		writer.write("reset\n");
		writer.write("unset key\n");
		writer.write("set terminal png\n");
		writer.write("set output " + "\"" + Constants.histoPath + "\"\n");
		writer.write("set boxwidth 0.9 relative\n");
		writer.write("set style data histograms\n");
		writer.write("set style histogram cluster\n");
		writer.write("set style fill solid 1.0\n");
		writer.write("set xtics border offset 1\n");
		writer.write("plot for [COL=2:2] '" + Constants.histoDataPath + "' using COL:xticlabels(1)\n");
		writer.close();
	}
	
	private void callGnuPlot() throws IOException {
		ProcessBuilder builder;
		ArrayList<String> callArgs = new ArrayList<String>();
		
		callArgs.add("gnuplot");
		callArgs.add(Constants.histoDescriptorPath);
		
		builder = new ProcessBuilder(callArgs);
		
		System.out.println(">> Executing gnuplot as the following: ");
		System.out.println("\t" + builder.command().toString());
		
		builder.start();		
	}

	public static void main (String[] args)
	{
		System.out.println("'Entre aspas simples'");
//		System.out.println(System.getProperty("user.dir") + "/.dotswap");
//		System.out.println("[]");
	}
}
