public class BigNumber {
    private String number;
    private boolean negative;

    public BigNumber(String number) {
        this.number = number;
    }

    public BigNumber(BigNumber number) {
        this.number = number.toString();
        this.negative = number.isNegative();
    }

    public void add(BigNumber number) {
        boolean addedOne = false;

        while(this.number.length() - number.toString().length() < 0) {
            this.number = "0" + this.number;
        }

        while(number.toString().length() - this.number.length() < 0) {
            number = new BigNumber("0" + number.toString());
        }

        for (int i = number.toString().length()-1; i >= 0; i--) {
            byte num1 = Byte.parseByte(String.valueOf(this.number.charAt(i)));
            byte num2 = Byte.parseByte(String.valueOf(number.toString().charAt(i)));

            num1 += num2;

            if(addedOne) {
                num1++;
                addedOne = false;
            }

            if(String.valueOf(num1).length() > 1) {
                addedOne = true;
            }

            StringBuilder newNumber = new StringBuilder(this.number);
            newNumber.setCharAt(i, String.valueOf(num1).charAt(String.valueOf(num1).length()-1));

            this.number = newNumber.toString();
        }

        if(addedOne) {
            this.number = "1" + this.number;
        }
    }

    @Override
    public String toString() {
        return this.number;
    }

    public boolean isNegative() {
        return this.negative;
    }

    public int size() {
        return number.length();
    }
}
