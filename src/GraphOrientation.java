
public enum GraphOrientation {
	TB, BT, LR, RL;
	
	@Override
	public String toString()
	{
		return "\"" + this.name() + "\"";
	}
}
