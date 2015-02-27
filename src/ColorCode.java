import java.util.HashMap;


public class ColorCode {
	private HashMap<String, Color> colorMap;
	
	public ColorCode ()
	{
		colorMap = new HashMap<String, Color>();
	}
	
	public void addKey (String key)
	{
		colorMap.put(key, new Color());
	}
	
	public Color getColor (String key)
	{		
		if (!colorMap.containsKey(key))
			addKey(key);
		
		return colorMap.get(key);
	}
}
