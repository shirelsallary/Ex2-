package assignments.ex2;
// Add your documentation below:

public class SCell implements Cell {
    private String line;
    private int type;
    private CellEntry cordinata;

    public SCell(String s) {
        setData(s);
        setType();
    }

    public SCell(String s, int x, int y) {
        line = s;
        this.cordinata = new CellEntry(x, y);
        setType();
    }

    public SCell() {
    }

    @Override
    public int getOrder() {
        // Add your code here

        return 0;
        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        line = s.trim(); // Trim input string
        if (isNumber(s)) {
            setType(Ex2Utils.NUMBER);
        } else if (isText(s)) {
            setType(Ex2Utils.TEXT);
        } else if (s.startsWith("=")) {
            String formulaResult = String.valueOf(computeForm(s));
            if (formulaResult.equals(Ex2Utils.ERR_FORM)) {
                setType(Ex2Utils.ERR_FORM_FORMAT);
            } else {
                setType(Ex2Utils.FORM);
            }
            line = formulaResult;
        } else {
            setType(Ex2Utils.TEXT);
        }
    }

    @Override
    public String getData() {
        return line;
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


    }

    private int whichtype() {
        if ((isNumber(line)))
            return 2;
        if (isText(line))
            return 1;
        if (isForm(line))
            return 3;
        return -1;
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
