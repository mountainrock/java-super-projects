package jdk1_5;

import java.util.EnumMap;
import java.util.Map;

import org.apache.poi.hssf.record.formula.functions.Rank;

public class EnumAdvancedDemo {

	public static void main(String[] args)
	{
		System.out.println(Rainbow.violet);
	}
}

enum Rainbow {
	violet(){
		@Override
		public String toString()
		{
			// TODO Auto-generated method stub
			return "v";
		}// , indigo, blue, green, yellow, orange, red
	}
	

}
