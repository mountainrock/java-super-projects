package parser.javaiq;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Category{
	String name;
	List<QuestionAnswer> qas= new ArrayList<QuestionAnswer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<QuestionAnswer> getQas() {
		return qas;
	}

	public void setQas(List<QuestionAnswer> qas) {
		this.qas = qas;
	}

	public void addQa(QuestionAnswer qa) {
		qas.add(qa);
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}