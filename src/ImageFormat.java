
public enum ImageFormat {
	PNG("-Tpng"), SVG("-Tsvg");
	String dotArg;
	
	private ImageFormat(String arg)
	{
		dotArg = arg;
	}
	
	@Override
	public String toString()
	{
		return dotArg;
	}
}
