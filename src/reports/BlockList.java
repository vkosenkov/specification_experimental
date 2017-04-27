package reports;

public interface BlockList
{
	public void add(Block block);
	public Block get(int i);
	public Block getLast();
	public void remove(Block block);
	public void remove(int i);
}