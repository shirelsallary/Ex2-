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


    public Ex2Sheet(int x, int y) {//גודל המערך
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL,i,j);
            }
        }
        eval();
    }
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }
    //computeform-if form
    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        if (isIn(x, y)) {
            Cell c = get(x, y);
            if (c != null) {
                try {
                    double num = Double.parseDouble(c.toString());

                    // Format the number as a string
                    String formatted = formatNumber(num);

                    // If the formatted string is longer than 8 characters, truncate it
                    if (formatted.length() > 8) {
                        formatted = formatted.substring(0, 8);
                    }

                    ans = formatted;
                } catch (NumberFormatException e) {
                    ans = c.toString();
                }
            }
        }
        return ans;
    }

    public void setCellType(String cell,int t)
    {
        int x=cell.charAt(0)-'A';
        int y=0;
        if(cell.length()==2){
             y=cell.charAt(1);
        }
        if(cell.length()==3){
            y= Integer.parseInt(cell.substring(1));
        }
        table[x][y].setType(t);
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }


    @Override
    public Cell get(String cords) {
        Cell ans = null;
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
        SCell c = new SCell(s);
        table[x][y] = c;
        // Add your code here

        /////////////////////
    }
    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here

        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        //initiate all the places with -1
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
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
                    name= String.valueOf(numToInt(x)+y);
                    if (canBeComputedNow(value(x,y),list,name)) {
                        ans[x][y] = depth;
                        count++;
                        flagC = true;
                    }
                }
            }
            depth++;
        }

        return ans;
    }

    private char numToInt(int x){
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
    private boolean canBeComputedNow (String name, List<String> cord, String format) {

        if(isTextS(format)) {
            setCellType(name,-2);
            return false;
        }
        //canot be compute
        if(isNumberS(format)) {return true;}
        if (!isFormS(format)) {return false;}
        if (!containsLetters(name)) {return true;}
        List<String> pointer1=cord;
        List<String> dependCells=extractCoordinates(format);
        List<String> pointer2=dependCells;
            for (String str1 : pointer1) {//str1 takes on the value of each element in pointer1 one by one.
                if (pointer2.contains(str1))//if it cycles
                {
                    setCellType(name,-1);
                    return false;
                }
                if (pointer2.contains(name)) {//if it cycles
                    setCellType(name,-1);
                    return false;
                }
            }
            pointer2=dependCells;
            for(int i=0; i<dependCells.size(); i++)
            {
                String element = pointer2.get(i);
                int x=element.charAt(0)-'A';
                int y=0;
               if(element.length()==2){
                    y=element.charAt(1);
                }
                if(element.length()==3){
                    y= Integer.parseInt(element.substring(1));
                }
                String neform=table[x][y].getData();
                canBeComputedNow(element, pointer1,neform);
            }
            return true;
    }
    private boolean containsLetters(String s) {
        for(int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if(Character.isLetter(c)) {return true;}
        }
        return false;
    }
    // Function to extract coordinates from a string
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
        if ((!isNumberS(text)) && (text.charAt(0) != '=')) {
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
                // Handle decimal numbers or cell references
                boolean hasDecimal = false;
                while (i < text.length() &&
                        (Character.isDigit(text.charAt(i)) ||
                                Character.isLetter(text.charAt(i)) ||
                                text.charAt(i) == '.')) {
                    if (text.charAt(i) == '.') {
                        if (hasDecimal) return false;  // Can't have two decimals
                        hasDecimal = true;
                    }
                    i++;
                }
                i--;  // Adjust for the loop increment
                expectingNumber = false;
                continue;
            }

            return false;  // Invalid character
        }

        return !expectingNumber;  // Should not end expecting a number
    }
}
