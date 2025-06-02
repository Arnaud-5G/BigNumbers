public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Testing addOne:");
        double startTime = System.nanoTime();
        BigNumber number = new BigNumber("1000000000000000000000000");
        for (int i = 0; i < 370000000; i++) {
            number.addOne();
        }
        double elapsedTime = (System.nanoTime() - startTime) / 1000000d; // Convert to milliseconds

        System.out.println("Time taken: " + elapsedTime + " ms"); // 105.8887 ms
        System.out.println("Average time taken: " + (elapsedTime / 370000000d) * 1000000d + " nano"); // 0.28618567567567565 nano

        System.out.println("Testing add with long:");
        startTime = System.nanoTime();
        number = new BigNumber("1000000000000000000000000");
        number.add(370000000);
        elapsedTime = (System.nanoTime() - startTime) / 1000000d; // Convert to milliseconds

        System.out.println("Time taken: " + elapsedTime + " ms"); // 164.9893 ms
        System.out.println("Average time taken: " + (elapsedTime / 370000000d) * 1000000d + " nano"); // 0.445917027027027 nano

        System.out.println("Testing add with BigNumber:");
        startTime = System.nanoTime();
        number = new BigNumber("1000000000000000000000000");
        BigNumber number1 = new BigNumber("370000000");
        number.add(number1);
        elapsedTime = (System.nanoTime() - startTime) / 1000000d; // Convert to milliseconds

        System.out.println("Time taken: " + elapsedTime + " ms"); // 946.792 ms
        System.out.println("Average time taken: " + (elapsedTime / 370000000d) * 1000000d + " nano"); // 2.5588972972972974 nano

        // testAddOne();
        // testSubOne();
        // testAdd();
    }

    public static void testAddOne() {
        BigNumber number = new BigNumber("1000000000000000000000000");
        BigNumber number1 = new BigNumber("9999999999");
        BigNumber number2 = new BigNumber("-1000000000000000000000000");
        BigNumber number3 = new BigNumber("-10000000000000000000000000000000000000");
        BigNumber number4 = new BigNumber("0");
        BigNumber number5 = new BigNumber("-1");
        BigNumber number6 = new BigNumber("18446744073709551617"); // 2^64 + 1
        BigNumber number7 = new BigNumber("18446744073709551616"); // 2^64
        BigNumber number8 = new BigNumber("18446744073709551615"); // 2^64 - 1
        System.out.println(number.addOne().toString()); // 1000000000000000000000001
        System.out.println(number1.addOne().toString()); // 10000000000
        System.out.println(number2.addOne().toString()); // -999999999999999999999999
        System.out.println(number3.addOne().toString()); // -9999999999999999999999999999999999999
        System.out.println(number4.addOne().toString()); // 1
        System.out.println(number5.addOne().toString()); // 0
        System.out.println(number5.addOne().toString()); // 1
        System.out.println(number6.addOne().toString()); // 18446744073709551618
        System.out.println(number7.addOne().toString()); // 18446744073709551617
        System.out.println(number8.addOne().toString()); // 18446744073709551616
    }

    public static void testSubOne() {
        BigNumber number = new BigNumber("1000000000000000000000000");
        BigNumber number1 = new BigNumber("9999999999");
        BigNumber number2 = new BigNumber("-1000000000000000000000000");
        BigNumber number3 = new BigNumber("-10000000000000000000000000000000000000");
        BigNumber number4 = new BigNumber("0");
        BigNumber number5 = new BigNumber("-1");
        BigNumber number6 = new BigNumber("18446744073709551617"); // 2^64 + 1
        BigNumber number7 = new BigNumber("18446744073709551616"); // 2^64
        BigNumber number8 = new BigNumber("18446744073709551615"); // 2^64 - 1
        System.out.println(number.subOne().toString()); // 999999999999999999999999
        System.out.println(number1.subOne().toString()); // 9999999998
        System.out.println(number2.subOne().toString()); // -1000000000000000000000001
        System.out.println(number3.subOne().toString()); // -10000000000000000000000000000000000001
        System.out.println(number4.subOne().toString()); // -1
        System.out.println(number5.subOne().toString()); // -2
        System.out.println(number6.subOne().toString()); // 18446744073709551616
        System.out.println(number7.subOne().toString()); // 18446744073709551615
        System.out.println(number8.subOne().toString()); // 18446744073709551614
    }

    public static void testAdd() {
        BigNumber number = new BigNumber("1000000000000000000000000");
        BigNumber number1 = new BigNumber("999999999");

        System.out.println(number1.add(number).toString()); // 100000000000000000009999999999
    }
}
