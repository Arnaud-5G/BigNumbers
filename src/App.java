public class App {
    public static void main(String[] args) throws Exception {
        // 7.5 ms
        BigNumber number = new BigNumber("999999999999999999999999999999999999999999999999999999999");

        for (String string : number.toBinary()) {
            System.out.print(string);
        }
    }
}