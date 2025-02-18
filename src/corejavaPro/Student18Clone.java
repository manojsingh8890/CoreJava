package corejavaPro;

public class Student18Clone implements Cloneable {
	int rollno;
	String name;

	Student18Clone(int rollno, String name) {
		this.rollno = rollno;
		this.name = name;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static void main(String args[]) {
		try {
			Student18Clone s1 = new Student18Clone(101, "amit");

			Student18Clone s2 = (Student18Clone) s1.clone();

			System.out.println(s1.rollno + " " + s1.name);
			System.out.println(s2.rollno + " " + s2.name);

		} catch (CloneNotSupportedException c) {
		}

	}
}