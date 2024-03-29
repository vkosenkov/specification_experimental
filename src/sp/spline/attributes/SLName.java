package sp.spline.attributes;

public class SLName implements SPLineAttribute
{
	private String name = "";
	
	public SLName(String name)
	{
		this.name = name;
	}

	@Override
	public String getStringValue()
	{
		return name;
	}

	@Override
	public void setValue(String value)
	{
		this.name = value;
	}

	@Override
	public void updateValueWith(String value)
	{
		this.name = value;
	}

	@Override
	public void updateValueWith(SPLineAttribute value)
	{
		this.name = value.getStringValue();
	}

	@Override
	public void clearValue()
	{
		name = "";
	}
}
