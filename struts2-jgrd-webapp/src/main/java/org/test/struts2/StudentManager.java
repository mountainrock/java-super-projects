package org.test.struts2;

import java.util.ArrayList;
import java.util.List;

public class StudentManager {
	
	List<Student> students = new ArrayList<Student>();

	public List<Student> find(int from, Integer rows) {
		List<Student> newList = new ArrayList<Student>();
		for(int i = from;i < (from+rows);i++) {
			newList.add(students.get(i));
		}
		return newList;
	}

	public void constructStudentsLists() {
		for(int i = 0;i < 30;i++) {
			Student student = new Student();
			student.setStudentAddress("TEST");
			student.setStudentID("11111111111");
			student.setStudentName("AVINASH");
			students.add(student);
			
		}
		
	}

}
