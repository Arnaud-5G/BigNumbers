import java.util.Arrays;

public class BigNumber {
    private long[] binary;
    private byte[] BCD;
    private boolean negative;

    public BigNumber(String number) {
        this.BCD = numberToBCD(number);
        this.binary = doubleDabble(this.BCD);

        // for (byte b : doubleDabble(this.binary)) {
        //     System.out.println(b);
        // }
    }

    public BigNumber(long number) {
        this.BCD = numberToBCD(Long.toString(number));
        this.binary = doubleDabble(this.BCD);
        this.negative = number < 0;
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

        if (number.equals("")) {
            return "0";
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

    //!             getters

    public boolean isNegative() {
        return this.negative;
    }

    public boolean isZero() {
        for (long l : this.binary) {
            if (l != 0) {
                return false;
            }
        }
        return true;
    }




    //!             comparison operations

    public boolean equals(BigNumber number) {
        if (this.isZero() && number.isZero()) {
            return true;
        }

        if (this.isNegative() != number.isNegative()) {
            return false;
        }

        if (this.binary.length != number.binary.length) {
            return false;
        }

        for (int i = 0; i < this.binary.length; i++) {
            if (this.binary[i] != number.binary[i]) {
                return false;
            }
        }

        return true;
    }


    public boolean isLessThan(BigNumber number) {
        if (this.isZero() && number.isZero()) {
            return false;
        }

        if (this.isNegative() && !number.isNegative()) {
            return true;
        }

        if (!this.isNegative() && number.isNegative()) {
            return false;
        }

        if (this.binary.length < number.binary.length) {
            return !this.isNegative();
        } else if (this.binary.length > number.binary.length) {
            return this.isNegative();
        }

        for (int i = this.binary.length - 1; i >= 0; i--) {
            if (this.binary[i] < number.binary[i]) {
                return !this.isNegative();
            } else if (this.binary[i] > number.binary[i]) {
                return this.isNegative();
            }
        }

        return false;
    }


    public boolean isGreaterThan(BigNumber number) {
        if (this.isZero() && number.isZero()) {
            return false;
        }

        if (this.isNegative() && !number.isNegative()) {
            return false;
        }

        if (!this.isNegative() && number.isNegative()) {
            return true;
        }

        if (this.binary.length > number.binary.length) {
            return !this.isNegative();
        } else if (this.binary.length < number.binary.length) {
            return this.isNegative();
        }

        for (int i = this.binary.length - 1; i >= 0; i--) {
            if (this.binary[i] > number.binary[i]) {
                return !this.isNegative();
            } else if (this.binary[i] < number.binary[i]) {
                return this.isNegative();
            }
        }

        return false;
    }






    //!             operations

    public BigNumber addOne() {
        for(int i = 0; i < this.binary.length; i++) {
            if(!negative) { // positive
                if (binary[i] != ~0L) {
                    binary[i]++;
                    return this;
                }
    
                binary[i] = 0;
                if (i == this.binary.length - 1) { // add a new index if we are at the end
                    binary = Arrays.copyOf(binary, binary.length + 1);
                    binary[i+1] = 1;
                    return this;
                }
            } else { // negative
                if (binary[i] != 0) { // check if number is 1 if yes then == 0 -> positive
                    if (this.binary.length == 1 && binary[0] == 1) {
                        negative = false;
                    }
                    binary[i]--;

                    if (i == this.binary.length - 1 && binary[i] == 0) { // if we are at the end and the last index is 0 then remove it
                        binary = Arrays.copyOf(binary, binary.length - 1);
                    }
                    return this;
                }

                binary[i] = ~0L;
            }
        }

        if (negative && binary[binary.length - 1] == ~0L) { // if all binaries = 0 then become 1
            for (int i = 1; i < binary.length; i++) {
                binary[i] = 0;
            }

            binary[0] = 1;
            binary = Arrays.copyOf(binary, 1); // keep only the first index

            negative = false;
        }

        return this;
    }


    public BigNumber subOne() {
        for(int i = 0; i < this.binary.length; i++) {
            if(!negative) { // positive
                if (binary[i] != 0) {
                    binary[i]--;
                    if (i == this.binary.length - 1 && binary[i] == 0) { // if we are at the end and the last index is 0 then remove it
                        binary = Arrays.copyOf(binary, binary.length - 1);
                    }
                    return this;
                }

                binary[i] = ~0L;
            } else { // negative
                if (binary[i] != ~0L) { // check if number is 0 if yes then == 1 -> positive
                    if (this.binary.length == 1 && binary[0] == 0) {
                        negative = false;
                    }
                    binary[i]++;
                    return this;
                }

                binary[i] = 0;
            }
        }

        if (!negative && binary[binary.length - 1] == ~0L) { // if all binaries = 0 then become 1
            for (int i = 1; i < binary.length; i++) {
                binary[i] = 0;
            }

            binary[0] = 1;
            binary = Arrays.copyOf(binary, 1); // keep only the first index

            negative = true;
        }

        return this;
    }




    public BigNumber add(BigNumber additive) { //TODO: optimize
        if (additive.isZero()) {
            return this;
        }

        if (this.isZero()) {
            this.binary = additive.binary;
            this.BCD = additive.BCD;
            this.negative = additive.isNegative();
            return this;
        }

        BigNumber smallest = this.isLessThan(additive) ? this : additive;

        BigNumber largest = this.isLessThan(additive) ? additive : this;

        while (!smallest.isZero()) {
            largest.addOne();
            if (smallest.isNegative()) {
                smallest.addOne();
            } else {
                smallest.subOne();
            }
        }

        return largest;
    }


    public BigNumber add(long additive) { //TODO: optimize
        while (additive != 0) {
            if (additive > 0) {
                this.addOne();
                additive--;
            } else {
                this.addOne(); // should be subtractOne()
                additive++;
            }
        }

        return this;
    }
}
