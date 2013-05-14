package apache.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * http://www.java2s.com/Code/Java/Apache-Common/BeanComparatorSortingbasedonPropertiesofclass.htm
 *
 */
public class ComparatorDemo {
	List<Person> personList = new ArrayList<Person>();

	void sortPersons(String propertyName)
	{
		Comparator<Person> comp = new BeanComparator(propertyName);
		Collections.sort(personList, comp);
		for (Person person : personList) {
			System.out.println(person);
		}
	}

	void setUpData()
	{
		personList.add(new Person("jennefer", "gowtham", 35000));
		personList.add(new Person("britney", "spears", 45000));
		personList.add(new Person("tom", "gowtham", 36000));
		personList.add(new Person("joe", "dummy", 45000));
		personList.add(new Person("Aoe", "dummy", 45000));
		personList.add(new Person("aoe", "dummy", 45000));
	}

	public static void main(String[] args)
	{
		ComparatorDemo beanComparatorExample = new ComparatorDemo();
		beanComparatorExample.setUpData();
		beanComparatorExample.sortPersons("firstName");
	}
}

