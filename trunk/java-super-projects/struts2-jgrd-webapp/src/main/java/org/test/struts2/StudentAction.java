package org.test.struts2;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class StudentAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1492653534653704237L;
	private Integer rows = 0;
	private Integer page = 0;
	private String sord;
	private String sidx;
	private String searchField;
	private String searchString;
	private String searchOper;
	private Integer total = 0;
	private Integer records = 0;

	private List<Student> listStudentUtils = new ArrayList<Student>();

	public String listStudents() {
	//	Logger.
		String type = "";
		StudentManager studentManager = new StudentManager();
		studentManager.constructStudentsLists();
		List<Student> students = new ArrayList<Student>();
		try {			
	   	    int to = (rows * page);
	        int from = to - rows;
	            
		
			students = studentManager.find(from, rows);
			records=30; //global total. 
			total = (int) Math.ceil((double) records / (double) rows);
			int i=0;
			for (Student student : students) {
				student.setStudentName(student.getStudentName() +i++);
				this.listStudentUtils.add(student);
			}
			type = SUCCESS;
		} catch (Exception e) {
			type = ERROR;
		}
		return type;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchOper() {
		return searchOper;
	}

	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;
	}

	public List<Student> getListStudentUtils() {
		return this.listStudentUtils;
	}

	public void setListStudentUtils(List<Student> listStudentUtils) {
		this.listStudentUtils = listStudentUtils;
	}

}
