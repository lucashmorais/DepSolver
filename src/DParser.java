import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;


public class DParser {
	BufferedReader in;	
	
	public DParser(String path) throws FileNotFoundException
	{
		in = new BufferedReader(new FileReader(path));
	}
	
	public void close () throws IOException
	{
		in.close();
	}
	
	public DepData parseFromFile() throws IOException {		
		DepData data = new DepData();
		
		for (int i = 0; notEnded(); i++)
		{
			String name;
			MemRange reads = new MemRange();
			MemRange writes = new MemRange();
			Call newCall;
			
			name = parseName();
			newCall = new Call(i, name);
			parseReadsAndWrites(reads, writes, newCall);
			newCall.reads = reads;
			newCall.writes = writes;
			
			data.addCall(newCall);			
		}
		
		return data;		
	}

	private void parseReadsAndWrites(MemRange reads, MemRange writes, Call newCall) throws IOException
	{
		Integer start, end;
		
		while (true)
		{
			if (nextLineIsRead())
			{
				String temp = in.readLine();
				String[] elements = temp.split(" ");
				
				start = Integer.parseInt(elements[1], 16);
				end = Integer.parseInt(elements[2], 16);
				reads.add(start, end, newCall);
			}
			else if (nextLineIsWrite())
			{
				String temp = in.readLine();				
				String[] elements = temp.split(" ");
				
				start = Integer.parseInt(elements[1], 16);
				end = Integer.parseInt(elements[2], 16);
				writes.add(start, end, newCall);				
			}
			else
				break;
		}
	}

	private boolean notEnded() throws IOException {
		boolean b;
		
		in.mark(300);
		
		b = !in.readLine().matches("\\d+;\\s+\\d+");
		
		in.reset();
		
		return b;
	}

	@SuppressWarnings("unused")
	private HashSet<Integer> parseReads() throws IOException {
		Integer pos;
		HashSet<Integer> reads = new HashSet<Integer>();
		
		while (nextLineIsRead())
		{
				String temp = in.readLine();
				pos = Integer.parseInt(temp.replace("R: ", ""), 16);
				reads.add(pos);
		}		
		
		return reads;
	}

	private boolean nextLineIsRead() throws IOException {
		boolean b;		
		in.mark(300);
		
		b = in.readLine().matches("R: [0-9a-fA-F]+ [0-9a-fA-F]+");
		
		in.reset();
		
		return b;
	}
	
	private boolean nextLineIsWrite() throws IOException {
		boolean b;		
		in.mark(300);
		
		b = in.readLine().matches("W: [0-9a-fA-F]+ [0-9a-fA-F]+");
		
		in.reset();
		
		return b;
	}

	@SuppressWarnings("unused")
	private HashSet<Integer> parseWrites() throws IOException {
		Integer pos;
		HashSet<Integer> writes = new HashSet<Integer>();
		
		while (nextLineIsWrite())
		{
				String temp = in.readLine();
				pos = Integer.parseInt(temp.replace("W: ", ""), 16);
				writes.add(pos);
		}		
		
		return writes;
	}

	private String parseName() throws IOException {
		return in.readLine();
	}
	
	public static void main (String[] args)
	{
		//testNotEnded();
		testHexScan();
	}

	@SuppressWarnings("unused")
	private static void testNotEnded() {
		boolean t1, t2;
		t1 = "50;     0".matches("\\d+;\\s+\\d+");
		t2 = "50;     0".matches("\\d+");
		
		System.out.println("T1: " + t1);
		System.out.println("T2: " + t2);
	}
	
	private static void testHexScan()
	{
		String s1, s2;
		Scanner scan1, scan2;
		
		s1 = "874f90";
		s2 = "R: 874fd0";
		
		scan1 = new Scanner(s1);
		scan2 = new Scanner(s2.replaceFirst("R: ", ""));
		
		System.out.println("First value: " + scan1.nextInt(16));
		System.out.println("Second value: " + scan2.nextInt(16));
		System.out.println("T1: " + s2.matches("R: [0-9a-fA-F]+"));
		
		scan1.close();
		scan2.close();
	}
}
