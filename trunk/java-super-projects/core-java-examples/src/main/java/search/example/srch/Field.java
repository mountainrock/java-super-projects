package search.example.srch;

public class Field {

	public static final String MODIFIED = "modified";

	public Field() {
	}

	public Field(String name, String value, boolean tokenized) {
		this(name, new String[] { value }, tokenized);
	}

	public Field(String name, String[] values, boolean tokenized) {
		this.name = name;
		this.values = values;
		this.tokenized = tokenized;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isTokenized()
	{
		return this.tokenized;
	}

	public void setTokenized(boolean type)
	{
		this.tokenized = type;
	}

	public String getValue()
	{
		if ((this.values != null) && (this.values.length > 0)) {
			return this.values[0];
		} else {
			return null;
		}
	}

	public void setValue(String value)
	{
		setValues(new String[] { value });
	}

	public String[] getValues()
	{
		return this.values;
	}

	public void setValues(String[] values)
	{
		this.values = values;
	}

	private String name;
	private boolean tokenized;
	private String[] values;

}