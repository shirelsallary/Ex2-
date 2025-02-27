package assignments.ex2;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Add your documentation below:

//Class Ex2Sheet:
//    Attributes:
//        - cells: 2D Array of Cell  // Represents the spreadsheet as a grid of cells
//
//    Constructor Ex2Sheet(rows: Integer, cols: Integer):
//        - cells = new 2D array of Cell with dimensions rows x cols
//        - Initialize each cell in the grid as a new SCell with empty content
//
//    Method getCell(x: Integer, y: Integer) -> Cell:
//        // Returns the cell at position (x, y)
//        return cells[x][y]
//
//    Method setCell(x: Integer, y: Integer, cell: Cell):
//        // Sets the cell at position (x, y) to the given cell
//        cells[x][y] = cell
//
//    Method getData(x: Integer, y: Integer) -> String:
//        // Returns the data (content) of the cell at position (x, y)
//        return cells[x][y].getData()
//
//    Method setData(x: Integer, y: Integer, data: String):
//        // Sets the data (content) of the cell at position (x, y)
//        cells[x][y].setData(data)
//
//    Method evaluateFormula(x: Integer, y: Integer) -> Double:
//        // Evaluates the formula in the cell at position (x, y) if it contains a formula
//        if cells[x][y].getType() == FORM:
//            return computeForm(cells[x][y].getData())
//        else:
//            // Return 0 if it's not a formula or there's an error
//            return 0.0
//
//    Method computeForm(form: String) -> Double:
//        // Computes a formula in the form of a string
//        if form starts with "=":
//            - Remove "=" from the formula
//        if formula contains cell references (e.g., A1, B2):
//            - Resolve the cell references to their actual values using getData or evaluateFormula
//        if form contains operations (e.g., "+", "-", "*", "/"):
//            - Split the formula by operators and evaluate each part recursively
//        Return the final computed result
//
//    Method displaySheet():
//        // Displays the current state of the entire sheet
//        for each row in cells:
//            for each cell in row:
//                Print cell.getData()
//
//    Method validateCoordinates(x: Integer, y: Integer) -> Boolean:
//        // Checks if the coordinates (x, y) are valid within the bounds of the sheet
//        if x >= 0 and x < rows and y >= 0 and y < cols:
//            return true
//        else:
//            return false


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




    public int setCellType(String cell, int t) {
        int x = cell.charAt(0) - 'A'; // Convert 'A' to 0, 'B' to 1, etc.
        int y = 0;
        if(isIn(x, y)) {
            if (cell.length() == 2) {
                y = cell.charAt(1) - '0'; // Correctly convert '3' to 3
            } else if (cell.length() == 3) {
                y = Integer.parseInt(cell.substring(1)); // Handle multi-digit row numbers
            }

            table[x][y].setType(t); // Assuming table[x][y] is valid and has a setType method
            return x;
        }
return 0;

    }


    public SCell get(int x, int y) {
        return table[x][y];
    }


    public String getCellData(int x, int y) {
        return table[x][y].toString();
    }


    @Override
    public SCell get(String cords) {
        return get(cords.charAt(0)-'A', Integer.parseInt(cords.substring(1)));
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
        int[][] dd = depth(); // Ensure depth is computed with the current sheet
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                Cell cell = get(x, y);
                if (cell != null && cell.getType() == Ex2Utils.FORM) { // If the cell contains a formula
                    if (cell instanceof SCell sCell) {
                        String formula = sCell.getData();

                        // Check if the formula has already been evaluated and its value is valid
                        if (!formula.startsWith("=") && !formula.equals(Ex2Utils.ERR_FORM)) {
                            // Skip re-evaluating the formula and updating the cell's value
                            continue;
                        }
                        // Evaluate the formula and update the cell's value
                        String computedValue = String.valueOf(computeForm(formula));
                        if (computedValue.equals(Ex2Utils.ERR_FORM)) {
                            // Set the cell data to "ERROR_FORM" and type to ERR_FORM_FORMAT
                            cell.setData(Ex2Utils.ERR_FORM);
                            cell.setType(Ex2Utils.ERR_FORM_FORMAT);
                        } else {
                            // Otherwise, update the cell with the computed value
                            cell.setData(computedValue);
                            cell.setType(Ex2Utils.FORM);  // Set the type to FORM after evaluation
                        }
                    }
                }
            }
        }
    }


//    public String SendTosuitableComute(int x, int y) {
//        String cor=numToChar(x)+"y";
//        if(table[x][y].isText(cor)||table[x][y].isText(cor))
//            return table[x][y].getData();
//        else {
//            return String.valueOf(computeForm(cor));
//        }
//    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && xx < width() && yy >= 0 && yy < height();
    }


    public boolean isIn(String cords) {
        int x = cords.charAt(0) - 'A';
        try {
          boolean i=  Character.isDigit(Integer.parseInt(cords.substring(1)));
          int y= Integer.parseInt(cords.substring(1));
          return isIn(x,y);
        }
        catch(Exception e) {
            return false;
        }
    }


    // Loads the sheet from a file
    @Override
    public void load(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null && row < height()) {
            String[] values = line.split(",");
            for (int col = 0; col < values.length && col < width(); col++) {
                set(col, row, values[col].trim());
            }
            row++;
        }
        reader.close();
    }

    // Saves the sheet to a file
    @Override
    public void save(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y > 0) writer.write(",");
                writer.write(value(x, y)); // Write the value of each cell
            }
            writer.newLine();
        }
        writer.close();
    }


    public String eval(int x, int y) {
depth();
        if(get(x,y).getType()==Ex2Utils.FORM) {
            return String.valueOf(computeForm(get(x,y).getData()));
        }
        if(get(x,y).getType()==Ex2Utils.NUMBER||get(x,y).getType()==Ex2Utils.TEXT)
        {return get(x,y).getData();}
       if(get(x,y).getType()==Ex2Utils.ERR_FORM_FORMAT)
       {return Ex2Utils.ERR_FORM;}
       if (get(x,y).getType()==Ex2Utils.ERR_CYCLE_FORM)
       {return Ex2Utils.ERR_CYCLE;}
      return null;

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
                    ans[i][j] = 0;
                }else {
                    ans[i][j] = -1;
                }
            }
        }

        int depth = 1;  // Start from depth 1 since 0 is for direct values
        int count = 0;
        int max = width() * height();
        boolean flagC = true;
        String name;
        List<String> list = new ArrayList<>();

        while (count < max && flagC) {
            flagC = false;
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    // Only process cells that haven't been assigned a depth yet
                    if (ans[x][y] == -1) {
                        name = String.valueOf(numToChar(x)+String.valueOf(y));
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
            setCellType(name, Ex2Utils.ERR_CYCLE_FORM);
            return false;
        }

        // Check for circular dependencies
        for (String str1 : cord) {
            if (dependCells.contains(str1)) {
                setCellType(name, Ex2Utils.ERR_CYCLE_FORM);
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
            if (!isIn(x, y)) {
                return false;
            }
            if(get(x,y).getType()==Ex2Utils.TEXT) {
                setCellType(name, Ex2Utils.ERR_FORM_FORMAT);
                return false;
            }

            String neform = table[x][y].getData();
            List<String> newCord = new ArrayList<>(cord);
            if (!canBeComputedNow(element, newCord, neform)) {
                setCellType(name, Ex2Utils.ERR_FORM_FORMAT);
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

        if(form.charAt(0)=='=') form=form.substring(1);

        if (form.indexOf("(") != -1)
        {
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
                        String first= (form.substring(0, openIndex));
                        String second = String.valueOf(form.substring(i + 1, form.length()));
                        form = first + sograym + second; //New string with the new value of brakes
                        form= String.valueOf(computeForm(form)); //doing it until there is no more brakes
                    }
                }
            }
        }
        if (form.contains("+")) {//Calculate connection
            int ind1 = form.indexOf("+");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1+1, form.length()));
            return result1 + result2;
        }
        if (form.contains("-")) {//calculate subtraction
            int ind1 = form.indexOf("-");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1+1, form.length()));
            return result1 - result2;
        }
        if (form.contains("/")) { //Calculate division
            int ind1 = form.indexOf("/");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1+1, form.length()));
            return result1 / result2;
        }
        if (form.contains("*")) {//calculate multiplication
            int ind1 = form.indexOf("*");
            double result1 = (int) computeForm(form.substring(0, ind1));
            double result2 = (int) computeForm(form.substring(ind1+1, form.length()));
            return result1 * result2;
        }

try{
        return Double.parseDouble(form);}
catch (NumberFormatException e){
    if(isIn(form)){
    return computeForm(get(form).getData());}
}
return 0;
    }



    private String resolveCellReferences(String formula) {
        StringBuilder resolvedFormula = new StringBuilder();
        int i = 0;

        while (i < formula.length()) {
            char c = formula.charAt(i);

            if (Character.isLetter(c)) {
                // Parse cell reference
                int letterIndex = getLetterPosition(c);
                int numberStart = i + 1;
                while (numberStart < formula.length() && Character.isDigit(formula.charAt(numberStart))) {
                    numberStart++;
                }
                int row = Integer.parseInt(formula.substring(i + 1, numberStart));
                if (letterIndex < 0 || row < 0 || letterIndex >= width || row >= height) {
                    return "Err_Form"; // Out-of-bounds reference
                }
                String cellValue = eval(letterIndex, row);
                if (cellValue.equals("Err_Form")) {
                    return "Err_Form"; // Invalid reference
                }
                resolvedFormula.append(cellValue); // Append resolved value
                i = numberStart; // Move to next part of the formula
            } else {
                resolvedFormula.append(c);
                i++;
            }
        }

        return resolvedFormula.toString();
    }

    private  int getLetterPosition(char letter) {
        char lowerCaseLetter = Character.toLowerCase(letter);
        return lowerCaseLetter - 'a' ;
    }


}




