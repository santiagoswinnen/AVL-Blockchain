import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  @author Santiago Swinnen
 */

public class Terminal {


    /**
     * Reads user input from terminal
     * @param args array of Strings (main program arguments).
     */
    public void start(String[] args){
        try {
            if (args[0].equals("zeros")) {
                char[] number = args[1].toCharArray();
                boolean isNumber = validateNumber(number);
                if (isNumber) {
                    int zeros = Integer.parseInt(args[1]);
                    if (zeros > 16 || zeros < 4) {
                        System.out.println("Invalid amount of zeros");
                    } else {
                        run(zeros);
                    }
                } else {
                    System.out.println("Error: Invalid number");
                }
            } else {
                System.out.println("Error: Invalid argument");
            }
        } catch( ArrayIndexOutOfBoundsException e) {
            System.out.println("No zeros passed as argument");
        }
    }


    /**
     * Receives a character array and returns whether it represents an integer or not.
     * @param s char array.
     * @return boolean value. True if the given string represents an integer, false if not.
     */
    private boolean validateNumber(char[] s) {
        boolean isNumber = true;
        int index;
        for(index = 0; index < s.length && isNumber; index++) {
            if(!Character.isDigit(s[index])) {
                isNumber = false;
            }
        }
        return isNumber;
    }

    /**
     * Main loop method. Waits for user input until "exit" is entered. If this happens the program ends.
     * @param zeros amount of zeros needed at the start of block chain hashcode to be considered valid
     */
    public void run(int zeros) {
        BlockChain bc = new BlockChain(zeros,this);
        System.out.println("Block chain has been successfully created");
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        String input;
        while(exit == false) {
            visualReport(bc);
            input = scanner.nextLine();
            if(input.equals("exit")){
                exit = true;
            } else {
                char[] chars = input.toCharArray();
                readInstruction(input, bc);
            }
        }

    }

    /**
     * Reads user input word by word. In case an instruction is read it is executed, otherwise no action is performed
     * @param instruction string input to be analized
     * @param bc current block chain
     */

    public void readInstruction(String instruction, BlockChain bc) {
        char[] chars = instruction.toCharArray();
        int i = 0;
        String action = getStringUntilChar(chars, i, ' ');
        i += action.length() + 1;
        /*
         * if the first word is "validate" and no other argument is read,
         * then perform the action validate on the block chain and return
         */
        if (action.equals("validate") && (i - 1) == instruction.length()) {
            System.out.println(bc.validateChain());
        } else if (action.equals("add") || action.equals("remove") || action.equals("lookup")) {
            String number = getStringUntilChar(chars, i, ' ');
            i += number.length();
            if (i == instruction.length()) {
                boolean isNumber = validateNumber(number.toCharArray());
                if (isNumber) {
                    bc.operate(action, Integer.parseInt(number));
                } else {
                    System.out.println("Invalid number, try again please.");
                }
            } else {
                System.out.println("Invalid action, try again please.");
            }
        } else if (action.equals("modify")) {
            String number = getStringUntilChar(chars, i, ' ');
            int num = -1;
            try {
                num = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number entry");
            } if(num >= 0) {
                i += (number.length() + 1);
                if(chars[i] == '[') {
                    i++;
                    String path = getStringUntilChar(chars, i, ']');
                    i+= path.length();
                    if(chars[i] == ']' && i+1 == instruction.length()) {
                        StringBuilder data = new StringBuilder();
                        if(readFromPath(path, data)) {
                            try {
                                bc.modify(num, data.toString());
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Invalid index for current block chain, try again please");
                            }
                        }
                    } else {
                        System.out.println("Invalid arguments, try again please");
                    }
                } else {
                    System.out.println("Invalid path expression, try again please");
                }
            }
        } else {
            System.out.println("Invalid action, try again please.");
        }
    }

    public boolean readFromPath(String path, StringBuilder data){
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                data.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Given path is wrong.");
            System.out.println("modify N [path]");
            return false;
        }
        return true;
    }
    private static String getStringUntilChar(char[] arr, int i, char end) {
        String ret = new String();
        while(i < arr.length && arr[i] != end) {
            ret = ret + arr[i];
            i++;
        }
        return ret;
    }


    /**
     * Prints message passed as parameter.
     * @param msg
     */
    public void printMessage(String msg){
        System.out.println(msg);
    }

    public void printDashedLine() {
        System.out.println("--------------------------------------------------------------");
        System.out.println();
    }

    public void visualReport(BlockChain bc){
        printDashedLine();
        System.out.println("BlockChain:");
        System.out.println(bc.toString());
        System.out.println();
        System.out.println("AVLTree: ");
        System.out.println();
        bc.showInsider();
        printDashedLine();
        System.out.println("Command :");
    }

}
