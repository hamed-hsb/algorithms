package com.company;

// develop by hamed safarzadeh
// version 1
public class FDH64 {

    /**
     * This class is an algorithm for generating a hash of data.
     * The name of this algorithm is Fried Data Hash (FDH).
     * This algorithm is based on binary information. We start by converting the information into two parts and then converting it into decimal and then binary.
     * The steps of this algorithm are as follows.
     *  1- Receive information and divide the information into 5 sections
     *  2- Convert ascii data to decimals and add the decimals to each part
     *  3- Convert the number of each part to binary
     *  4- The number of bits in each segment must be 3 bits
     *  5- If there were fewer bits in each segment, we would add that bit
     *  6- Converts and converts bits to the desired bits using the algorithm (conversion, displacement, composition)
     *  7- Then we convert the bits to decimals
     *  8- Decimal conversion to ascii
     */


    private  String Result;
    private String data;
    private static byte ARRAY_NUMBER = 8;
    private static byte DIVIED = 8;



    public FDH64(String data){ this.data = data;}
    public  String getHash(){
         balance(binaryOpration(decimalToBinary(convertToSumAsscii(dataSplit(data)))));
       return  Result;
    }


    /**
     * this function for
     * @param data
     * @return
     */
    private  String[] dataSplit(String data) {
        if (data.length() < 512) {
            data = StrCombin(data);
        }
        int div = data.length() / 8;
        String[] dataArray = new String[8];
        int beginIndex = 0;
        int endIndex = div;
        for (int i = 0; i < 8; i++) {
            dataArray[i] = data.substring(beginIndex, endIndex);
            beginIndex = endIndex;
            endIndex += div;
            if (endIndex > data.length()) {
                endIndex = data.length();
            }
        }

        return dataArray;
    }

    private Integer[] convertToSumAsscii(String[] dataArray) {
        int number = 0;
        Integer[] intArray = new Integer[8];
        for (int i = 0; i < 8; i++) {
            char[] ch = dataArray[i].toCharArray();
            for (char chr : ch) {
                number += (int) chr;
            }
            intArray[i] = number;
            number = 0;
        }

        for (int i = 7; i > 0; i--) {

            for (int j = 0; j < i; j++) {
                intArray[i] += intArray[j];
            }
        }


        return intArray;
    }

    private String[] decimalToBinary(Integer number[]) {
        String[] binary = new String[8];
        int index = 0;
        String str = "";
        int def = 0;
        int len = 0;
        int low = 0;
        boolean conti = true;

        for (int i = 0; i < 8; i++) {

            if (Integer.toBinaryString(number[i]).length() < 32) {
                len = Integer.toBinaryString(number[i]).length();
                def = 32 - Integer.toBinaryString(number[i]).length();

                for (int j = 0; j < def; j++) {

                    while (true) {
                        len -= low;
                        str += Integer.toBinaryString(number[i]).substring(0, len);
                        if (str.length() > def) {
                            str = str.substring(0, def);
                            if (str.length() == def)
                                break;
                        } else if (str.length() == def) {
                            break;
                        }
                    }


                }
                binary[i] = str + Integer.toBinaryString(number[i]);
            } else {
                binary[i] = Integer.toBinaryString(number[i]);
            }

            str = "";

        }

        return binary;
    }

    private String[] binaryOpration(String[] binaryData) {
        String[] binary = new String[8];
        String[] binary16 = new String[8];
        String[] binary8 = new String[8];
        String str = "";
        String str16 = "";
        String str8 = "";
        int beginIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < 8; i++) {
            beginIndex = 0;
            endIndex = 0;
            for (int p = 0; p < 16; p++) {
                endIndex++;
                endIndex++;

                if (p % 2 == 1) {
                    str16 += XNOR(NAND2(binaryData[i].substring(beginIndex, endIndex)));
                } else {
                    str16 += XNOR(binaryData[i].substring(beginIndex, endIndex));
                }
                beginIndex = endIndex;

            }


            binary16[i] = str16;
            str16 = "";

        }

        beginIndex = 0;
        endIndex = 0;
        for (int i = 0; i < 8; i++) {
            beginIndex = 0;
            endIndex = 0;
            for (int p = 0; p < 8; p++) {
                endIndex++;
                endIndex++;
                if (p == 0) {
                    str8 += XNOR((binary16[i].substring(beginIndex, endIndex)));
                } else if (p == 1) {
                    str8 += XNOR((binary16[i].substring(beginIndex, endIndex)));
                } else {
                    if (p % 2 == 0) {
                        str8 += "1";
                    } else {
                        str8 += (XNOR(NAND2(binary16[i].substring(beginIndex, endIndex))));
                    }

                }


                beginIndex = endIndex;
            }


            binary[i] = str8;
            str8 = "";
        }



        return binary;
    }

    private String[] balance(String binary[]) {
        Result = "";
        String[] ascii = new String[8];
        int dig = 0;
        int numDec = 0;

        for (int i = 0; i < binary.length; i++) {
            numDec = Integer.parseInt(binary[i], 2);

            if (i > 0)
                numDec = numDec / (i);

            while (true) {
                dig = digits(String.valueOf(numDec));
                if (numDec > 122) {
                    numDec = numDec - dig;
                } else if (numDec < 48) {
                    numDec = numDec + dig;
                } else if (numDec > 57 && numDec < 60) {
                    numDec = numDec - dig;
                } else if (numDec > 60 && numDec < 64) {
                    numDec = numDec + dig;
                } else if (numDec > 90 && numDec < 97) {
                    numDec = numDec + dig;
                } else {
                    break;
                }
            }
            dig = 0;

            ascii[i] = String.valueOf((char) numDec);
            Result += String.valueOf((char) numDec);
        }




        return ascii;
    }


    private int digits(String number) {
        int numSUM = 0;
        for (char chNum: number.toCharArray()){
            numSUM += Integer.valueOf(String.valueOf(chNum));
        }


        return numSUM;
    }

    private int digitsR(String number) {
        int numberDig = 0;
        for (int i = number.length(); i < 1; i--) {
            numberDig = Character.digit(number.charAt(i), 10);
            break;

        }
        return numberDig;
    }

    private String revers(String bit) {
        if (bit.equals("1"))
            return "0";
        else if (bit.equals("0"))
            return "1";
        else
            //print
            return null;
    }

    private String NAND(String binary) {
        if (binary.equals("11")) {
            return "0";
        } else if (binary.equals("01") || binary.equals("10") || binary.equals("00")) {
            return "1";
        } else {
            //print("error,ivalid binary data .");
        }
        return null;
    }

    private String NAND2(String binary) {
        if (binary.equals("11")) {
            return "00";
        } else if (binary.equals("00")) {
            return "11";
        } else if (binary.equals("10")) {
            return "01";
        } else if (binary.equals("01")) {
            return "10";
        }
        return null;
    }

    private String XNOR(String binary) {
        if (binary.equals("00") || binary.equals("11")) {
            return "1";
        } else if (binary.equals("01") || binary.equals("10")) {
            return "0";
        } else {
            //print("error,ivalid binary data .");
        }
        return null;
    }

    private String OR(String binary) {
        if (binary.equals("00")) {
            return "0";
        } else if (binary.equals("01") || binary.equals("10") || binary.equals("11")) {
            return "1";
        } else {
            //print("error,ivalid binary data .");
        }
        return null;
    }

    private String AND(String binary) {
        if (binary.equals("11")) {
            return "1";
        } else if (binary.equals("01") || binary.equals("10") || binary.equals("00")) {
            return "0";
        } else {
            //print("error,ivalid binary data .");
        }
        return null;
    }

    private String StrCombin(String data) {
        String dL = "";
        String dR = "";

        if (data.length() < 1024) {
            int chI1 = 0;
            int chI2 = 0;

            chI1 = (int) data.toCharArray()[0];
            if (data.length() > 1) {
                chI2 = (int) data.toCharArray()[1];
            }

            for (int i = 0; i < chI1; i++) {
                dL += i+(char) i;
                dL += (char) i;
            }

            if (chI2>0) {
                for (int i = chI2; i >= 0; i--) {

                    if (i%2 == 0){
                        dR += i;
                        dR += (char) i;
                        dR += i;
                    }else{
                        dR += (char) i;
                        dR += i+i;
                        dR += (char) i;
                    }
                }
            }else{
                for (int i = chI1; i >= 0; i--) {

                    if (i%2 == 0){
                        dR += (char) i;
                        dR += i;
                    }else{
                        dR += (char) i;
                        dR += i+i;
                        dR += (char) i;
                    }
                }
            }
            data = dR + dR.length() + data + dR.length() + dL ;
        }

        return data;
    }
}
