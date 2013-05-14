package miscellaneous;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class DimensionalCombination {

	public static void main(String[] args)
	{
		String itemId = "1";
		long messageCount = 10;
		long itemIdLong = Long.parseLong(itemId);
		for (int i = 0; i < messageCount; i++) {
			itemId = (itemIdLong + i) + "";
			System.out.println(itemId);

		}
	}

	public static List getDimensionCombinations(String[] dimArray)
	{
		// Get the list dimension codes. nc1s and nc2s ( in that order ).
		List nc1Andnc2 = new ArrayList();
		for (int i = 0; i < dimArray.length; i++) {
			nc1Andnc2.add(dimArray[i]);
		}

		for (int i = 0; i < dimArray.length; i++) {
			for (int j = i + 1; j < dimArray.length; j++) {
				SortedSet set = new TreeSet();
				if (dimArray[i].compareTo(dimArray[j]) > 0) // swap
				{
					String temp = dimArray[i];
					dimArray[i] = dimArray[j];
					dimArray[j] = temp;
				}

				nc1Andnc2.add(dimArray[i] + "," + dimArray[j]);
			}
		}
		// System.out.println(nc1Andnc2);
		return nc1Andnc2;
	}
	/*
	 * public static void main(String[] args) {
	 * 
	 * //Big decimal test for devision BigDecimal b = new BigDecimal(15); BigDecimal d = new BigDecimal(4);
	 * 
	 * MyBean bean = new MyBean(); BeanWrapper bw = new BeanWrapperImpl(bean); Map propertyVal = new HashMap(); propertyVal.put("a","hello"); MyBean innerBean = new MyBean();
	 * propertyVal.put("list[1]",innerBean); propertyVal.put("list[1].a","helloInnerBean");
	 * 
	 * bw.setPropertyValues(propertyVal);
	 * 
	 * System.out.println((bean)); StringArrayPropertyEditor spe = new StringArrayPropertyEditor(); spe.setAsText("a,b,c,d,e");
	 * 
	 * System.out.println(StringUtils.commaDelimitedListToSet("a,b,c,d,e")); System.out.println(ClassLoaderUtils.showClassLoaderHierarchy(bean.getClass().getClassLoader()));
	 * 
	 * 
	 * try { MyBean bean = (MyBean)Beans.instantiate(ClassLoader.getSystemClassLoader(), "MyBean"); System.out.println(bean.getA()); } catch (ClassNotFoundException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * 
	 * 
	 * try { // Execute command String command = "dir"; Process child = Runtime.getRuntime().exec(command);
	 * 
	 * // Get the input stream and read from it InputStream in = child.getInputStream(); int c; while ((c = in.read()) != -1) { System.out.println(((char)c)); } in.close();
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } List chosenClassesList = new ArrayList(); chosenClassesList.add("sdafdsa");
	 * 
	 * String chosenClasses[] = new String[chosenClassesList.size()]; for(int i=0;i<chosenClassesList.size();i++) { chosenClasses[i] = (String)chosenClassesList.get(i);
	 * System.out.println(chosenClasses[i]); }
	 * 
	 * // List chosenClassesList = new ArrayList(); // chosenClassesList.add("sdfasdf"); // String chosenClasses[] = new String[chosenClassesList.size()]; // chosenClasses =
	 * (String[])chosenClassesList.toArray(chosenClasses); // System.out.println(chosenClasses); //System.out.println((double)10/(double)0);
	 * 
	 * // HashMap finalHashMap = new HashMap(); // finalHashMap.put("2",null); // finalHashMap.put("1",null); // System.out.println(finalHashMap); // // String s1 = new String( "Java" ); // String s2
	 * = new String( "Java" ); //System.out.println(s1.hashCode()); //System.out.println(s2.hashCode()); // System.out.println( "The result is: " + ( ( s1 == s2 ) ? "true" : "false" )); // String
	 * query = "INSERT INTO FACT_DEFN ( FACT_DEFN_ID, C_VERSION, CHART_DEFN_ID, DEFINITION, FACT_COMPUTER," // + "ENCODED_PARAMETERS ) VALUES (	FACT_DEFN_PK_SQ.NEXTVAL, 1," // +
	 * "(select chart_Defn_id from chart_defn where name='CA-3' and type='B' and dimension_codes='$STRINGTOREPLACE')," // +
	 * "'select ''The average age of the entire group is ''|| round(avg(age),0) as fact from(select (EXTRACT(YEAR FROM SYSDATE) -EXTRACT(YEAR FROM DATE_OF_BIRTH))- (CASE WHEN ( EXTRACT(MONTH FROM SYSDATE) < EXTRACT(MONTH FROM DATE_OF_BIRTH) OR ( EXTRACT( MONTH FROM SYSDATE) = EXTRACT (MONTH FROM DATE_OF_BIRTH) AND EXTRACT( DAY FROM SYSDATE) <   EXTRACT (DAY FROM DATE_OF_BIRTH))) THEN 1 ELSE 0 END)as age from CENSUS_DATA where date_of_birth is not null and account_version_id=? and CENSUS_VERSION_NUMBER=(select MAX(CENSUS_VERSION_NUMBER) from CENSUS_DATA where account_version_id=? )) where age is not null',"
	 * // +"'default', 'ACCOUNTVERSIONID,ACCOUNTVERSIONID');"; // // String[] dimArray = {"Class","Division","EmployeeStatus","Gender","Location","PlanType"}; // List dims =
	 * getDimensionCombinations(dimArray); // for (Iterator iter = dims.iterator(); iter.hasNext();) // { // String dimension = (String) iter.next(); // String tempQuery =
	 * query.replaceAll("\\$STRINGTOREPLACE",dimension); // System.out.println(tempQuery); // System.out.println(); // } // String [] sectionColors = new String[]{"","",""}; // int index=0; // int
	 * numSections =20; // for (int i = 0; i < numSections; i++) // { // index = numSections > sectionColors.length ? i % sectionColors.length : i; // System.out.println(index); // } // String
	 * dimArray[] ={"CA","AB"}; // // List testList = new ArrayList(); // testList.add("abc"); // testList.add("def"); // SortedSet set = new TreeSet(); // set.addAll(testList); //
	 * System.out.println(set); // //Set inputDimensions = new HashSet(); //String testInput ="test a $$"; //String temp = testInput.replaceAll("\\$","A"); //System.out.println(temp);
	 * 
	 * //for (int index = 0; index < dimArray.length; index++) //{ // String strDimension = dimArray[index]; // Set tempDimensionSet = new HashSet(); // tempDimensionSet.add(strDimension); //
	 * inputDimensions.add(tempDimensionSet); //} //System.out.println(inputDimensions);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // // ArrayList groupBy = new ArrayList(); // groupBy.add("Class"); // groupBy.add("Division"); // groupBy.add("Location"); // // // if(groupBy.size()>1) //to generate combinations of groupby
	 * like CLASSDIVISION,CLASSDIVISIONGENDER.... // { // int groupBySize= groupBy.size(); // for (int index = 0; index < groupBy.size(); index++) // { // String current = (String)groupBy.get(index);
	 * // // for (int j = index+1; j < groupBySize; j++) // { // String next1 = (String)groupBy.get(j); // groupBy.add(current+next1); // for (int k = j+1; k < groupBySize; k++) // { // String next2 =
	 * (String)groupBy.get(k); // groupBy.add(current+next1+next2); // } // // System.out.println(groupBy); // } // } // } // // String str = ""; //testing the split // String array[] =
	 * str.split("~"); // System.out.println(array); // for (int i = 0; i < array.length; i++) // { // System.out.println(array[i]); // } }
	 */

}
