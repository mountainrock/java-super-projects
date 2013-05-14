package search.example.srch;

public class Sort {
	private String fieldName;
	private boolean reverse;

	public Sort() {
	}

	public Sort(String fieldName, boolean reverse) {
		this.fieldName = fieldName;
		this.reverse = reverse;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public boolean isReverse()
	{
		return reverse;
	}

	public void setReverse(boolean reverse)
	{
		this.reverse = reverse;
	}

	public String toString()
	{
		return "{fieldName=" + fieldName + ", reverse=" + reverse + "}";
	}

}