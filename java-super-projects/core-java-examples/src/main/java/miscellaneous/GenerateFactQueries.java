package miscellaneous;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenerateFactQueries {
	/**
	 * 
	 * @param dimArray
	 * @return
	 *//*
	public List getDimensionCombinations(String[] dimArray)
	{
		// Get the list dimension codes. nc1s and nc2s ( in that order ).
		List nc1Andnc2 = new ArrayList();
		for (int i = 0; i < dimArray.length; i++) {
			nc1Andnc2.add(dimArray[i]);
		}

		for (int i = 0; i < dimArray.length; i++) {
			for (int j = i + 1; j < dimArray.length; j++) {
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

	*//**
	 * 
	 * @param args
	 *//*
	public static void main(String[] args)
	{
		String chartName = "CA-4";
		String chartType = "P";
		// String[] dimArray = {"Class","Division","EmployeeStatus","Gender","Location","PlanType"};
		String[] dimArray = { "Class", "CoverageType" };
		String factDefnQueryWithEscapedQuotes = "select round(sum(A),2) || ''% of the insured population covers dependents'' as fact from (select coverage_tier,(100 * ratio_to_report(count(*)) over()) as A from census_data where  coverage_tier is not null  and account_version_id=? and CENSUS_VERSION_NUMBER=( select MAX(CENSUS_VERSION_NUMBER) from CENSUS_DATA where account_version_id=? ) group by coverage_tier ) where coverage_tier!=''S''";
		String factComputer = "default";
		String preparedStmtParams = "ACCOUNTVERSIONID,ACCOUNTVERSIONID";
		GenerateFactQueries factQueries = new GenerateFactQueries();

		String query = "INSERT INTO FACT_DEFN ( FACT_DEFN_ID, C_VERSION, CHART_DEFN_ID, DEFINITION, FACT_COMPUTER," + "ENCODED_PARAMETERS ) VALUES (	FACT_DEFN_PK_SQ.NEXTVAL, 1,"
				+ "(select chart_Defn_id from chart_defn where " + "name='" + chartName + "'" + " and type='" + chartType + "' and dimension_codes='$STRINGTOREPLACE')," + "'"
				+ factDefnQueryWithEscapedQuotes + "'," + "'" + factComputer + "'," + "'" + preparedStmtParams + "');";

		List dims = factQueries.getDimensionCombinations(dimArray);
		for (Iterator iter = dims.iterator(); iter.hasNext();) {
			String dimension = (String) iter.next();
			String tempQuery = query.replaceAll("\\$STRINGTOREPLACE", dimension);
			System.out.println(tempQuery);
			System.out.println();
		}
	}*/
}
