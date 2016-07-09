package datastruct;

/**
 * ”≥…‰πÿœµ
 * @author Ekira
 *
 */
public class Mapping {
	private int key = 0;
	private int value = 0;
	
	public Mapping() { }
	public Mapping(int key, int value) { setMapping(key, value); }
	
	public void setMapping(int key, int value) { this.key = key; this.value = value; }
	public void setKey(int key) { this.key = key; }
	public void setValue(int value) { this.value = value; }
	
	public int getKey() { return this.key; }
	public int getValue() { return this.value; }

}
