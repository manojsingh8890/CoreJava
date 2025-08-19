package JavaProgram;

public class Test {
    public void print(Integer i) {
        System.out.println("Integer");
    }

    public void print(int i) {
        System.out.println("int");
    }

    public void print(long i) {
        System.out.println("long");
    }
    
    
    public static void foo(Integer i) {
        System.out.println("foo(Integer)");
    }

    public static void foo(short i) {
        System.out.println("foo(short)");
    }

    public static void foo(long i) {
        System.out.println("foo(long)");
    }

    public static void foo(int... i) {
        System.out.println("foo(int ...)");
    }



    public static void main(String args[]) {
        Test test = new Test();
        test.print(10);
        
        System.out.println("---------------------------");
        
        Derived b = new DeriDerived();
        
        System.out.println("----------------------------");
        
        foo(10);
        
        System.out.println("----------------------------");
        
        BaseClass po = new DerivedClass();
       // po.foo(); // BASE_FOO_CALL
        po.bar();
        
        System.out.println("----------------------------");
        
        
        Thread myThread = new MyThread();
        myThread.run(); // #1
        System.out.println("In main method; thread name is: " + Thread.currentThread().getName());

    }
}

class Base {
    public Base() {
        System.out.println("Base");
    }
}

class Derived extends Base {
    public Derived() {
        System.out.println("Derived");
    }
}

class DeriDerived extends Derived {
    public DeriDerived() {
        System.out.println("DeriDerived");
    }
}

class BaseClass {
    private void foo() {
        System.out.println("In BaseClass.foo()");
    }

    void bar() {
        System.out.println("In BaseClass.bar()");
    }

    public static void main(String[] args) {
        BaseClass po = new DerivedClass();
        po.foo(); // BASE_FOO_CALL
        po.bar();
    }
}

class DerivedClass extends BaseClass {
    void foo() {
        System.out.println("In Derived.foo()");
    }

    void bar() {
        System.out.println("In Derived.bar()");
    }
}


class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("In run method; thread name is: " + Thread.currentThread().getName());
    }
}

 class ThreadTest {

    public static void main(String args[]) {
        Thread myThread = new MyThread();
        myThread.run(); // #1
        System.out.println("In main method; thread name is: " + Thread.currentThread().getName());
    }
}

