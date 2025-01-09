package assignments.ex2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private SCell[][] table;
    private int width;
    private int height;


    public Ex2Sheet(int x, int y) {//גודל המערך
        width = x;
        height = y;
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL,i,j);
            }
        }
        //eval();
    }
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }
    //computeform-if form
    @Override

    public String value(int x, int y) {
       return table[x][y].getData();
    }


    public int setCellType(String cell, int t) {
        int x = cell.charAt(0) - 'A'; // Convert 'A' to 0, 'B' to 1, etc.
        int y = 0;

        if (cell.length() == 2) {
            y = cell.charAt(1) - '0'; // Correctly convert '3' to 3
        } else if (cell.length() == 3) {
            y = Integer.parseInt(cell.substring(1)); // Handle multi-digit row numbers
        }

        table[x][y].setType(t); // Assuming table[x][y] is valid and has a setType method
        return x;
    }


    public SCell get(int x, int y) {

        return table[x][y];
    }


    public String getCellData(int x, int y) {
        return table[x][y].toString();
    }


    @Override
    public SCell get(String cords) {
        SCell ans = null;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        if (isIn(x, y)) {
            SCell c = new SCell(s,x,y);
            table[x][y]=c;
        }
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here

        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && xx < width() && yy >= 0 && yy < height();
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        if(get(x,y)!=null) {ans = get(x,y).toString();}
        // Add your code here

        /////////////////////
        return ans;
    }

    public String value(String n)
    {
        int x=n.charAt(0)-'A';
        int y=0;
        if(n.length()==2){
            y=n.charAt(1);
        }
        else if(n.length()==3){
            y= Integer.parseInt(n.substring(1));
        }

        y= Integer.parseInt(n.substring(1));
        return value(x, y);
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        //initiate all the places with -1
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if((table[i][j].getType()==Ex2Utils.TEXT)||(table[i][j].getType()==Ex2Utils.NUMBER)) {
                    ans[i][j] =0;

                }else {
                    ans[i][j] = -1;
                }
                ans[i][j] = -1;
            }
        }

        int depth = 0;
        int count = 0;
        int max = width() * height();
        boolean flagC = true;
        String name="";
        List<String> list = new ArrayList<>();
        while (count < max && flagC) {
            flagC = false;
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    name="";
                    name= String.valueOf(numToChar(x)+String.valueOf(y));
                    if(ans[x][y]!=0) {
                        list.clear();
                        if (canBeComputedNow(name, list, value(x, y))) {
                            ans[x][y] = depth;
                            count++;
                            flagC = true;
                        }
                    }
                }
            }
            depth++;
        }

        return ans;
    }

    public boolean canBeComputedNow(String name, List<String> cord, String format) {
        // Check for empty format
        if (format == null || format.trim().isEmpty()) {
            return false;
        }

        // If it's text, set error type and return false
        if (isTextS(format)) {
            setCellType(name, -2);
            return false;
        }

        // If it's a number, it can be computed
        if (isNumberS(format)) {
            return true;
        }

        // If it's not a formula, it cannot be computed
        if (!isFormS(format)) {
            return false;
        }

        // If formula contains no cell references, it can be computed
        if (!containsLetters(format)) {
            return true;
        }

        // Get dependent cells
        List<String> dependCells = extractCoordinates(format);

        // Check for self-reference
        if (dependCells.contains(name)) {
            setCellType(name, -1);
            return false;
        }

        // Check for circular dependencies
        for (String str1 : cord) {
            if (dependCells.contains(str1)) {
                setCellType(name, -1);
                return false;
            }
        }

        // Recursively check all dependent cells
        cord.addAll(dependCells);
        for (String element : dependCells) {
            int x = element.charAt(0) - 'A';
            int y;

            if (element.length() == 2) {
                y = Character.getNumericValue(element.charAt(1));
            } else if (element.length() == 3) {
                y = Integer.parseInt(element.substring(1));
            } else {
                return false; // Invalid cell reference format
            }

            // Check if coordinates are within table bounds
            if (x < 0 || x >= table.length || y < 0 || y >= table[0].length) {
                return false;
            }

            String neform = table[x][y].getData();
            List<String> newCord = new ArrayList<>(cord);
            if (!canBeComputedNow(element, newCord, neform)) {
                return false;
            }
        }

        return true;
    }

    private boolean containsLetters(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> extractCoordinates(String input) {
        // Create a Set to store unique coordinates
        Set<String> coordinatesSet = new HashSet<>();

        // Define the regex pattern to match cell coordinates (e.g., "a2", "A4")
        String regex = "\\b[a-zA-Z][0-9]+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Find all matches and add them to the Set (Set automatically removes duplicates)
        while (matcher.find()) {
            coordinatesSet.add(matcher.group());
        }

        // Convert the Set back to a List (to return a List)
        return new ArrayList<>(coordinatesSet);
    }


    private String formatNumber(double num) {
        String strNum = String.valueOf(num);

        // if the number contains a decimal point split it into integer and decimal parts and add ".0"
        if (strNum.contains(".")) {
            int dotIndex = strNum.indexOf(".");
            String integerPart = strNum.substring(0, dotIndex);
            String decimalPart = strNum.substring(dotIndex + 1);

            // if the decimal part is empty, add ".0"
            if (decimalPart.isEmpty()) {
                return integerPart + ".0";
            }

            // else, add the decimal part to the integer part
            return integerPart + "." + decimalPart;
        }

        // if the number doesn't contain a decimal point, add ".0"
        return strNum + ".0";
    }

    public boolean isNumberS(String num) {
        boolean ans;        // Try parsing the string to  double
        try {

            Double.parseDouble(num);
            ans = true; // If parsing succeeds, it's a valid number
        } catch (NumberFormatException e) {
            ans = false; // If parsing fails, it's not a valid number
        }
        return ans;
    }

    public boolean isTextS(String text) {
        if (text == null) return false;
        if ((!isNumberS(text)) && (text.charAt(0) !='=')) {
            return true;
        }
        return false;
    }


    public boolean isFormS(String str) {
        // 1. Basic validation
        if (str == null || !str.startsWith("=")) return false;

        String text = str.substring(1);  // Remove '='
        if (text.isEmpty()) return false;

        // Special case: if it's just a number after '='
        try {
            Double.parseDouble(text);
            return true;  // Valid if it's just a number
        } catch (NumberFormatException e) {
            // Continue checking if it's not just a simple number
        }

        // 2. Check parentheses balance and validity
        int bracketCount = 0;
        for (char c : text.toCharArray()) {
            if (c == '(') bracketCount++;
            if (c == ')') bracketCount--;
            if (bracketCount < 0) return false;
        }
        if (bracketCount != 0) return false;

        // 3. Check each character and operation
        boolean expectingNumber = true;  // true if we expect a number/letter, false if we expect an operator
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == ' ') continue;  // Skip spaces

            if (c == '(') {
                if (!expectingNumber) return false;
                continue;
            }

            if (c == ')') {
                if (expectingNumber) return false;
                expectingNumber = false;
                continue;
            }

            if ("+-*/".indexOf(c) != -1) {  // Is operator
                if (expectingNumber) return false;
                expectingNumber = true;
                continue;
            }

            if (Character.isDigit(c) || Character.isLetter(c) || c == '.') {
                if (!expectingNumber) return false;

                boolean hasDecimal = false;
                boolean hasLetter = false;
                boolean hasDigit = false;

                // Validate sequence of digits/letters
                while (i < text.length() &&
                        (Character.isDigit(text.charAt(i)) ||
                                Character.isLetter(text.charAt(i)) ||
                                text.charAt(i) == '.')) {
                    char current = text.charAt(i);

                    if (current == '.') {
                        if (hasDecimal || hasLetter) return false;  // Can't have two decimals or a letter in a number
                        hasDecimal = true;
                    } else if (Character.isLetter(current)) {
                        if (hasDecimal || hasDigit) return false;  // Can't have letters in the middle of a number
                        hasLetter = true;
                    } else if (Character.isDigit(current)) {
                        hasDigit = true;
                    }

                    i++;
                }

                // Cell reference must be a letter followed by digits (e.g., A3)
                if (hasLetter && !hasDigit) return false;

                i--;  // Adjust for the loop increment
                expectingNumber = false;
                continue;
            }

            return false;  // Invalid character
        }

        return !expectingNumber;  // Should not end expecting a number
    }


    private boolean isCellReference(String text) {
        if (text.length() < 2) return false;

        // First character should be a letter (case insensitive)
        char firstChar = Character.toUpperCase(text.charAt(0));
        if (firstChar < 'A' || firstChar > 'Z') return false;

        // Rest should be a valid number
        String numberPart = text.substring(1);
        try {
            int number = Integer.parseInt(numberPart);
            return number >= 0;  // Ensure it's a non-negative number
        } catch (NumberFormatException e) {
            return false;
        }
    }


public String getLineFromScell(String element)
{
    int x=element.charAt(0)-'A';
    int y=0;
    if(element.length()==2){
        y=element.charAt(1);
    }
    else if (element.length()==3){
        y= Integer.parseInt(element.substring(1));
    }
     return table[x][y].getData();
}


    private char numToChar(int x){
        if(x==0) return 'A';
        if(x==1) return 'B';
        if(x==2) return 'C';
        if(x==3) return 'D';
        if(x==4) return 'E';
        if(x==5) return 'F';
        if(x==6) return 'G';
        if(x==7) return 'H';
        if(x==8) return 'I';
        if(x==9) return 'J';
        if(x==10) return 'K';
        if(x==11) return 'L';
        if(x==12) return 'M';
        if(x==13) return 'N';
        if(x==14) return 'O';
        if(x==15) return 'P';
        if(x==16) return 'Q';
        if(x==17) return 'R';
        if(x==18) return 'S';
        if(x==19) return 'T';
        if(x==20) return 'U';
        if(x==21) return 'V';
        if(x==22) return 'W';
        if(x==23) return 'X';
        if(x==24) return 'Y';
        if(x==25) return 'Z';
        return '0';
    }



    public double computeForm(String form) {
        if (form.charAt(0) == '=') form = form.substring(1);

        if (form.indexOf("(") != -1) {
            int openIndex = form.indexOf("(");
            int temp = 0;
            for (int i = openIndex; i < form.length(); i++) { //Count the brackets
                if (form.charAt(i) == '(') {
                    temp++;
                }

                if (form.charAt(i) == ')') {
                    temp--;
                    if (temp == 0) {
                        double sograym = computeForm(form.substring(openIndex + 1, i)); //Solve the formula inside the brackets
                        String first = (form.substring(0, openIndex));
                        String second = String.valueOf(form.substring(i + 1, form.length()));
                        form = first + sograym + second; //New string with the new value of brakes
                        form = String.valueOf(computeForm(form)); //doing it until there is no more brakes
                    }
                }
            }
        }
        if (form.contains("+")) {//Calculate connection
            int ind1 = form.indexOf("+");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1 + 1, form.length()));
            return result1 + result2;
        }
        if (form.contains("-")) {//calculate subtraction
            int ind1 = form.indexOf("-");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1 + 1, form.length()));
            return result1 - result2;
        }
        if (form.contains("/")) { //Calculate division
            int ind1 = form.indexOf("/");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1 + 1, form.length()));
            return result1 / result2;
        }
        if (form.contains("*")) {//calculate multiplication
            int ind1 = form.indexOf("*");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1 + 1, form.length()));
            return result1 * result2;
        }


        return Double.parseDouble(form);

    }





}



//    private boolean isCellReference(String text) {
//        if (text.length() < 2) return false;
//
//        // First character should be a letter (case insensitive)
//        char firstChar = Character.toUpperCase(text.charAt(0));
//        if (firstChar < 'A' || firstChar > 'Z') return false;
//
//        // Rest should be a valid number
//        String numberPart = text.substring(1).toUpperCase(); // Convert the rest to uppercase in case it contains letters
//        try {
//            int number = Integer.parseInt(numberPart);
//            return number >= 0;  // Ensure it's a non-negative number
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }

//    private boolean isCellReference(String text) {
//        if (text.length() < 2) return false;
//
//        // First character should be a letter (case insensitive)
//        char firstChar = Character.toUpperCase(text.charAt(0));
//        if (firstChar < 'A' || firstChar > 'Z') return false;
//
//        // Rest should be a valid number
//        String numberPart = text.substring(1);
//        try {
//            int number = Integer.parseInt(numberPart);
//            return number >= 0;  // Ensure it's a non-negative number
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }

//
//    public boolean isFormS(String str) {
//        // 1. Basic validation
//        if (str == null || !str.startsWith("=")) return false;
//
//        String text = str.substring(1);  // Remove '='
//        if (text.isEmpty()) return false;
//
//        // Special case: if it's just a number after '='
//        try {
//            Double.parseDouble(text);
//            return true;  // Valid if it's just a number
//        } catch (NumberFormatException e) {
//            // Continue checking if it's not just a simple number
//        }
//
//        // 2. Check parentheses balance and validity
//        int bracketCount = 0;
//        for (char c : text.toCharArray()) {
//            if (c == '(') bracketCount++;
//            if (c == ')') bracketCount--;
//            if (bracketCount < 0) return false;
//        }
//        if (bracketCount != 0) return false;
//
//        // 3. Check each character and operation
//        boolean expectingNumber = true;  // true if we expect a number/letter, false if we expect an operator
//        for (int i = 0; i < text.length(); i++) {
//            char c = text.charAt(i);
//
//            if (c == ' ') continue;  // Skip spaces
//
//            if (c == '(') {
//                if (!expectingNumber) return false;
//                continue;
//            }
//
//            if (c == ')') {
//                if (expectingNumber) return false;
//                expectingNumber = false;
//                continue;
//            }
//
//            if ("+-*/".indexOf(c) != -1) {  // Is operator
//                if (expectingNumber) return false;
//                expectingNumber = true;
//                continue;
//            }
//
//            if (Character.isDigit(c) || Character.isLetter(c) || c == '.') {
//                if (!expectingNumber) return false;
//                // Handle decimal numbers or cell references
//                boolean hasDecimal = false;
//                while (i < text.length() &&
//                        (Character.isDigit(text.charAt(i)) ||
//                                Character.isLetter(text.charAt(i)) ||
//                                text.charAt(i) == '.')) {
//                    if (text.charAt(i) == '.') {
//                        if (hasDecimal) return false;  // Can't have two decimals
//                        hasDecimal = true;
//                    }
//                    i++;
//                }
//                i--;  // Adjust for the loop increment
//                expectingNumber = false;
//                continue;
//            }
//
//            return false;  // Invalid character
//        }
//
//        return !expectingNumber;  // Should not end expecting a number
//    }

//public boolean getCanBeComputedNow(String element)
//{
//    List<String> l=new ArrayList<>();
//    int x=element.charAt(0)-'A';
//    int y=0;
//    if(element.length()==2){
//        y=element.charAt(1);
//    }
//    else if (element.length()==3){
//        y= Integer.parseInt(element.substring(1));
//    }
//
//    return canBeComputedNow(element,l,table[x][y].getData());
//
//}

//
//   public boolean canBeComputedNow (String name, List<String> cord, String format) {
//
//       if (format ==" ")
//       {
//           return false;
//       }
//
//        if(isTextS(format))
//        {
//            setCellType(name,-2);//set type for an error form
//            return false;
//        }
//        //canot be compute
//        if(isNumberS(format)) {return true;}
//        if (!isFormS(format)) {return false;}
//        if (!containsLetters(format)) {return true;}//valid format
//        List<String> pointer1=cord;//exist list pointer
//        List<String> dependCells=extractCoordinates(format);//the list of the cells that the current cell(index=name) are depend on
//        List<String> pointer2=dependCells;//his pointer
//            for (String str1 : pointer1) {//str1 takes on the value of each element in pointer1 one by one.
//                if (pointer2.contains(str1))//if it cycles
//                {
//                    setCellType(name,-1);// set for an error cycle
//                    return false;
//                }
//                if (pointer2.contains(name)) {//if it contains himself
//                    setCellType(name,-1);
//                    return false;
//                }
//            }
//            pointer2=dependCells;
//            cord.addAll(dependCells);
//            for(int i=0; i<dependCells.size(); i++)
//            {
//                String element = pointer2.get(i);
//                int x=element.charAt(0)-'A';
//                int y=0;
//               if(element.length()==2){
//                    y=Integer.parseInt(String.valueOf(element.charAt(1)));
//                }
//                 else if (element.length()==3){
//                    y= Integer.parseInt(element.substring(1));
//                }
//                String neform=table[x][y].getData();
//                List<String> newCord = new ArrayList<>(cord); // Create a copy of cord
//                newCord.add(pointer2.get(i)); // Add the new element to the copy
//                canBeComputedNow(element, newCord,neform);
//            }
//            return true;
//    }
//    private boolean containsLetters(String s) {
//        for(int i=0;i<s.length();i++) {
//            char c = s.charAt(i);
//            if(Character.isLetter(c)) {return true;}
//        }
//        return false;
//    }
// Function to extract coordinates from a string
