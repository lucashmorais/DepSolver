import java.util.Random;


public class Color {
	Random rnd;
	long value;
	//TODO: Adicionar novo jogo de cores
	static int[] defaultColors = {0x64B65C, 0x506C92, 0xDEB570, 0xDA6E73, 0x53BE47, 0x426699, 0xE8B257, 0xE4565D};
	static int numOfUsedColors = 0;
	
	public Color()
	{
		rnd = new Random();
		if (Color.numOfUsedColors < Color.defaultColors.length)
		{
			value = Color.defaultColors[Color.numOfUsedColors];
			Color.numOfUsedColors++;
		}
		else
			value = rnd.nextLong() % 16777215;
	}
	
	public Color(int setValue)
	{
		rnd = new Random();
		value = setValue;
	}
	
	@Override
	public String toString() {
		return String.format("#%06x", value & 0xFFFFFF);
	}
	
	public static void main (String [] args)
	{
		//System.out.println("Random color: " + new Color());
		/*
		for (int val: defaultColors)
		{
			System.out.println(val);
		}
		*/
		
		for (int i = 0; i < defaultColors.length; i++)
			System.out.println(new Color());
	}
}
