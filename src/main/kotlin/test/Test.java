package test;

public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            a:for (int j = 0; j < 5; j++) {
                if (i == 2) {
                    break;
                }
            }
            System.out.println("aaa"+i);
        }
    }
}
