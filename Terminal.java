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
        if(args[0].equals("zeros")) {
            char[] number = args[1].toCharArray();
            boolean isNumber = validateNumber(number);
            if(isNumber){
                int zeros = Integer.parseInt(args[1]);
                if(zeros>16 || zeros <4) {
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
        BlockChain<Integer> bc = new BlockChain<>(zeros);
        System.out.println("Block chain has been successfully created");
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        String input;
        while(exit == false) {
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
        int i;
        String action= new String();
        for(i = 0; i < chars.length && chars[i] != ' '; i++) {
            char current = chars[i];
            action = action + String.valueOf(current);
        }
        i++;

        /*
         * if the first word is "validate" and no other argument is read,
         * then perform the action validate on the block chain and return
         */

        if(action.equals("validate")) {
            if(action.equals(instruction)) {
                boolean c = bc.validateChain();
                System.out.println(c);
                return;
            }
        } else if(action.equals("add") || action.equals("remove") || action.equals("lookup")) {
            String number = new String();
            for(int j = 0; i < chars.length && chars[i] != ' '; i++, j++) {
                number = number + chars[i];
            }
            boolean isNumber = validateNumber(number.toCharArray());
            if(isNumber) {
                String num = number.toString();
                bc.add(action,Integer.parseInt(num));
            } else {
                System.out.println("Invalid number, try again please");
            }
        } else if(action.equals("modify")) {
            /* Modify case missing. To be done soon!! */
        } else {
            System.out.println("Invalid action, try again please");
        }

    }


}
