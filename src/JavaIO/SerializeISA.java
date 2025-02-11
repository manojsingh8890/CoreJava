package JavaIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Person implements Serializable {
	int id;
	String name;
	transient String contact;

	Person(int id, String name, String contact) {
		this.id = id;
		this.name = name;
		this.contact = contact;
	}
}

class Student extends Person {
	String course;
	int fee;

	public Student(int id, String name, String course, int fee) {
		super(id, name, "contact");
		this.course = course;
		this.fee = fee;
	}
}

public class SerializeISA {
	public static void main(String args[]) {
		try {
			// Creating the object
			Student s1 = new Student(211, "ravi", "Engineering", 50000);
			// Creating stream and writing the object
			FileOutputStream fout = new FileOutputStream("f.txt");
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(s1);
			out.flush();
			// closing the stream
			out.close();
			System.out.println("success");
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			// Creating stream to read the object
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("f.txt"));
			Student s = (Student) in.readObject();
			// printing the data of the serialized object
			System.out.println(s.id + " " + s.name + " " + s.course + " " + s.fee + " "+ s.contact);
			// closing the stream
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}