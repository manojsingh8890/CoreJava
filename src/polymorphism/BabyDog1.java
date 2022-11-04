package polymorphism;

class Animal {
	void eat() {
		System.out.println("animal is eating...");
	}
}

class Dog extends Animal {
	void eat() {
		System.out.println("dog is eating...");
	}
}

class cat extends Dog {
	void eat() {
		System.out.println("cat is eating...");
	}
}

public class BabyDog1 extends cat {

	public static void main(String args[]) {

		Animal a = new BabyDog1();

		a.eat();

	}
}