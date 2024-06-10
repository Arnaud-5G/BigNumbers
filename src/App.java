public class App {
    public static void main(String[] args) throws Exception {
        BigNumber number = new BigNumber("9");
        number.add(new BigNumber("9"));
        System.out.println(number);
    }
}
