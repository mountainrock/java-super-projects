package parser.javaiq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;


public class QuestionBank{
	String group;
	List<Category> categories= new ArrayList<Category>();
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<Category> getQas() {
		return categories;
	}
	public void setQas(List<Category> qas) {
		this.categories = qas;
	}
	
	public void addCategory(Category c) {
		categories.add(c);
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}

