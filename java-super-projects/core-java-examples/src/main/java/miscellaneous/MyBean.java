package miscellaneous;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/*
 *   Copyright (c)2000 company Technologies
 *   All Rights Reserved.
 *
 *   This software is furnished under a license and may be used and copied
 *   only  in  accordance  with  the  terms  of such  license and with the
 *   inclusion of the above copyright notice. This software or  any  other
 *   copies thereof may not be provided or otherwise made available to any
 *   other person. No title to and ownership of  the  software  is  hereby
 *   transferred.
 *
 *   The information in this software is subject to change without  notice
 *   and  should  not be  construed as a commitment  by company Technologies.
 *
 *
 *   Author: Sandeep.Maloth
 *   Date:Feb 28, 2006
 */

public class MyBean {
	String a = "sfsd";
	List list = new ArrayList();

	/**
 * 
 */
	public MyBean() {
		// TODO Auto-generated constructor stub
	}

	
	public String getA()
	{
		return a;
	}

	
	public void setA(String a)
	{
		this.a = a;
	}

	
	public List getList()
	{
		return list;
	}

	
	public void setList(List list)
	{
		this.list = list;
	}

	
	public String toString()
	{
		return new ToStringBuilder(this).append("list", this.list).append("a", this.a).toString();
	}

}