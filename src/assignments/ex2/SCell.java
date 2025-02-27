package assignments.ex2;


//Class SCell:
//    Attributes:
//        - line: String  // Stores the original data or formula of the cell
//        - type: Integer // Stores the type of the cell (e.g., number, text, formula)
//        - cordinata: CellEntry // Stores the coordinates of the cell
//
//    Constructor SCell(s: String):
//        - line = trim(s)
//        - setType()
//
//    Constructor SCell(s: String, x: Integer, y: Integer):
//        - line = trim(s)
//        - cordinata = new CellEntry(x, y)
//        - setType()
//
//    Method getOrder() -> Integer:
//        // Return the order of the cell (Not implemented)
//        return 0
//
//    Method toString() -> String:
//        return getData()
//
//    Method setData(s: String):
//        - line = trim(s)
//        - if s is a number:
//            setType(NUMBER)
//        - else if s is text:
//            setType(TEXT)
//        - else if s starts with "=" (formula):
//            - if formula is valid:
//                setType(FORM)
//            - else:
//                setType(ERR_FORM_FORMAT)
//
//    Method getData() -> String:
//        return line
//
//    Method getType() -> Integer:
//        return type
//
//    Method setOrder(t: Integer):
//        // Set the order of the cell (Not implemented)
//
//    Method setType(t: Integer):
//        type = t
//
//    Method setType():
//        if line is not empty:
//            if line is text:
//                type = TEXT
//            else if line is number:
//                type = NUMBER
//            else if line is a valid formula:
//                type = FORM
//            else:
//                type = ERR_FORM_FORMAT
//
//    Method setText(s: String):
//        line = s
//
//    Method getText() -> String:
//        return line
//
//    Method getIsCordinataIsValid(x: Integer, y: Integer) -> Boolean:
//        return cordinata.isValid()
//
//    Method isNumber(num: String) -> Boolean:
//        try:
//            - if num can be parsed to a double:
//                return true
//        catch NumberFormatException:
//            return false
//
//    Method isText(text: String) -> Boolean:
//        if text is not a number and text does not start with "=":
//            return true
//        return false
//
//    Method isForm(str: String) -> Boolean:
//        if str starts with "=":
//            - Remove "=" from the beginning
//            - Check if the formula has balanced parentheses
//            - Check if the formula contains valid characters (numbers, operators, cell references)
//            return true if all checks pass
//        return false
//
//    Method allowedSign(sign: Character) -> Boolean:
//        return true if sign is one of '+', '-', '*', '/', or a valid character (like a letter)
//
//    Method computeForm(form: String) -> Double:
//        if form starts with "=":
//            - Remove "=" from the formula
//        if formula contains parentheses:
//            - Find matching parentheses and compute the expression inside recursively
//        if form contains an operation (e.g., "+", "-", "*", "/"):
//            - Split the formula at the operator and compute left and right sides recursively
//            return the result of the operation
//        if form is a number:
//            return the number as a double
//        return result of the computed expression


public class SCell implements Cell {
    private String line;
    private int type;
    private CellEntry cordinata;

    public SCell(String s) {
        this.line = s.trim();  // Store the original string
        setType();
    }

    public SCell(String s, int x, int y) {
        this.line = s.trim();  // Store the original string
        this.cordinata = new CellEntry(x, y);
        setType();
    }


    @Override
    public int getOrder() {
        // Add your code here

        return 0;
        // ///////////////////
    }

    @Override
    public String toString() {
        return getData();  // Return the original string
    }

    @Override
    public void setData(String s) {
        this.line = s.trim();  // Always store the original string

        if (isNumber(s)) {
            setType(Ex2Utils.NUMBER);
        } else if (isText(s)) {
            setType(Ex2Utils.TEXT);
        } else if (s.startsWith("=")) {
            // Don't overwrite the original formula string
            String formulaResult = String.valueOf(computeForm(s));
            if (formulaResult.equals(Ex2Utils.ERR_FORM)) {
                setType(Ex2Utils.ERR_FORM_FORMAT);
            } else {
                setType(Ex2Utils.FORM);
            }
        } else {
            setType(Ex2Utils.TEXT);
        }
    }


    @Override
    public String getData() {
        return line;  // Return the original string
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here

    }

    @Override
    public void setType(int t) {
        type = t;
    }

    public void setType() {
        if(line!="") {
            if (isText(line))
                type = Ex2Utils.TEXT;
            else if (isNumber(line)) {
                type = Ex2Utils.NUMBER;
            } else if (isForm(line)) {
                type = Ex2Utils.FORM;
            }else {
                type = Ex2Utils.ERR_FORM_FORMAT;
            }
        }

    }

    public void setText(String s) {
        line = s;
    }
    public String getText() {
        return line;
    }

    public boolean getIsCordinataIsValid(int x, int y)
    {
        return cordinata.isValid();
    }

    public boolean isNumber(String num) {
        boolean ans;        // Try parsing the string to  double
        try {

            Double.parseDouble(num);
            ans = true; // If parsing succeeds, it's a valid number
        } catch (NumberFormatException e) {
            ans = false; // If parsing fails, it's not a valid number
        }
        return ans;
    }

    public boolean isText(String text) {
        if (text == null) return false;
        if ((!isNumber(text)) && (text.charAt(0) != '=')) {
            return true;
        }
        return false;
    }



    public boolean isForm(String str) {
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



//    public boolean isForm(String str) {
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

    //return if tha char is allowed
    public boolean allowedSign(char sign) {
        if (sign == '+') return true;
        if (sign == '-') return true;
        if (sign == '*') return true;
        if (sign == '/') return true;
        if (sign == '.') return true;
        return Character.isLetter(sign);

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
