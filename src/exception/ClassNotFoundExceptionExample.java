package exception;

public class ClassNotFoundExceptionExample {
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

        public static void main(String[]  args) throws Exception {
                System.out.println("Loading MySQL JDBC driver");
                Class.forName(DRIVER_CLASS);
        }
}