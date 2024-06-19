import java.util.Arrays;

public class BigNumber {
    private long[] binary;
    private byte[] BCD;
    private boolean negative;

    public BigNumber(String number) {
        this.BCD = numberToBCD(number);
        this.binary = doubleDabble(this.BCD);

        for (byte b : doubleDabble(this.binary)) {
            System.out.println(b);
        }
    }

    public BigNumber(BigNumber number) {
        this.BCD = numberToBCD(number.toUnsignedString());
        this.binary = doubleDabble(this.BCD);
        this.negative = number.isNegative();
    }

    private byte[] numberToBCD(String number) {
        if(number.charAt(0) == '-') {
            number = number.substring(1);
            this.negative = true;
        }

        byte[] BCD = new byte[number.length()];

        for (int i = 0; i < number.length(); i++) {
            BCD[i] = Byte.valueOf(String.valueOf(number.charAt(i)));
        }

        return BCD;
    }

    private byte[] doubleDabble(long[] binary) {
        byte[] BCD = new byte[((binary.length * 64) - 4) / 2 + 1]; // Maximum possible size
        int binaryArrayIndex = binary.length - 1;
        int bitPosition = 63;
        boolean isComplete = false;
        boolean hasOverflown = false;

        while(!isComplete) {
            if((binary[binaryArrayIndex] & (1L << bitPosition)) == (1L << bitPosition)) {
                hasOverflown = true;
            }

            for (int i = 0; i < BCD.length; i++) {
                boolean isOverflowing = hasOverflown;
                hasOverflown = false;

                if(BCD[i] >= 5) {
                    BCD[i] += 3;
                }

                if((BCD[i] & 8) == 8) {
                    hasOverflown = true;
                }

                BCD[i] <<= 1;

                BCD[i] &= 0b00001111; // remove any bits that exceed the limit

                if(isOverflowing) {
                    BCD[i] |= 1;
                }
            }
            
            bitPosition--;

            if(bitPosition == -1) {
                binaryArrayIndex--;
                bitPosition = 63;
            }

            hasOverflown = false;

            if(binaryArrayIndex == -1 && bitPosition == 63) {
                isComplete = true;
            }
        }

        int numberOfTrimsNeeded = 0;
        for (int i = BCD.length - 1; i >= 0; i--) {
            if(BCD[i] == 0) {
                numberOfTrimsNeeded++;
            } else {
                break;
            }
        }

        // Trim the array to the actual size
        return Arrays.copyOf(BCD, BCD.length - numberOfTrimsNeeded);
    }

    private long[] doubleDabble(byte[] BCD) {
        long[] binaryArray = new long[(BCD.length * 8) / 64 + 1]; // Maximum possible size
        int binaryArrayIndex = 0;
        int bitPosition = 0;
        boolean isComplete = false;
        boolean hasOverflown = false;

        while(!isComplete) {
            for (int i = 0; i < BCD.length; i++) {
                boolean isOverflowing = hasOverflown;
                hasOverflown = false;

                if((BCD[i] & 1) == 1) {
                    hasOverflown = true;
                }

                BCD[i] >>>= 1;

                if(isOverflowing) {
                    BCD[i] |= 8;
                }

                if(BCD[i] >= 8) {
                    BCD[i] -= 3;
                }
            }

            if(hasOverflown) {
                binaryArray[binaryArrayIndex] |= (1L << bitPosition);
                hasOverflown = false;
            }

            bitPosition++;

            if(bitPosition == 64) {
                binaryArrayIndex++;
                bitPosition = 0;
            }

            isComplete = true;
            for (byte b : BCD) {
                isComplete &= (b == 0);
            }
        }

        // Trim the array to the actual size
        return Arrays.copyOf(binaryArray, binaryArrayIndex + (bitPosition > 0 ? 1 : 0));
    }

    @Override
    public String toString() {
        String number = "";
        byte[] BCD = doubleDabble(this.binary);

        for (int i = 0; i < BCD.length; i++) {
            number = String.valueOf(BCD[i]) + number;
        }

        if(isNegative()) {
            return "-" + number;
        }

        return number;
    }

    public String toUnsignedString() {
        String number = "";
        for (int i = 0; i < BCD.length; i++) {
            number = String.valueOf(BCD[i]) + number;
        }
        return number;
    }

    public String[] toBinary() {
        String[] binary = new String[this.binary.length];
        for (int i = binary.length - 1; i >= 0 ; i--) {
            binary[i] = Long.toBinaryString(this.binary[binary.length - 1 - i]);

            // add leading zeros
            while(binary[i].length() < 64 && i != 0) {
                binary[i] = "0" + binary[i];
            }
        }
        return binary;
    }

    public boolean isNegative() {
        return this.negative;
    }
}
