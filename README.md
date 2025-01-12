Overview
This project includes two main classes: SCell and Ex2Sheet. The SCell class represents a single cell in a spreadsheet, capable of holding and processing different types of data such as numbers, text, and formulas. The Ex2Sheet class represents a spreadsheet itself, managing a grid of SCell instances, allowing for data storage, formula evaluation, and cell manipulation.

SCell Class
The SCell class implements the Cell interface and represents a single cell within a spreadsheet. Each cell can store different types of data, which can be numbers, text, or formulas. The class handles the validation of the data type and provides methods to retrieve, modify, and evaluate the cell's data.

Key Features:
Data Types: The cell can store:
Numbers
Text
Formulas (prefixed with =)
Data Validation: The class ensures that the data adheres to correct types (e.g., numeric values, text, or valid formulas).
Formula Computation: Formulas are parsed and evaluated recursively, handling operations like addition, subtraction, multiplication, and division.
Cell Coordinates: Optionally, SCell can store its coordinates (row and column) for validation and referencing purposes.
Methods:
setData(String data): Sets the data of the cell. Based on the content, it automatically determines if the data is a number, text, or formula.
getData(): Returns the data stored in the cell.
getType(): Retrieves the type of data stored (e.g., text, number, or formula).
setType(int type): Sets the data type of the cell.
computeForm(String formula): Evaluates a formula expression containing basic arithmetic and cell references.
isNumber(String data): Checks if the given string represents a valid number.
isText(String data): Checks if the given string represents a valid text.
isForm(String data): Validates if the string is a proper formula.
Ex2Sheet Class
The Ex2Sheet class represents a spreadsheet consisting of a grid of SCell instances. It manages the entire sheet, allowing for manipulation of individual cells and evaluation of formulas. The class allows users to set and get cell data, evaluate formulas, and display the current state of the sheet.

Key Features:
Spreadsheet Grid: The sheet consists of a 2D array of cells (represented by SCell).
Cell Manipulation: Users can set and get the data for specific cells.
Formula Evaluation: Supports evaluating formulas containing basic arithmetic operations and cell references.
Display Sheet: Prints the entire sheet’s data to the console for visualization.
Coordinate Validation: Ensures that cell coordinates are within the valid range of the sheet.
Methods:
getCell(int x, int y): Retrieves the cell at position (x, y).
setCell(int x, int y, Cell cell): Sets the cell at position (x, y) to the given cell.
getData(int x, int y): Returns the data stored in the cell at position (x, y).
setData(int x, int y, String data): Sets the data in the cell at position (x, y) to the specified value.
evaluateFormula(int x, int y): If the cell contains a formula, evaluates it and returns the result.
computeForm(String formula): Evaluates a formula string, resolving any cell references and performing arithmetic operations.
displaySheet(): Displays the current state of the sheet, printing the data of all cells.
validateCoordinates(int x, int y): Checks if the given coordinates (x, y) are within the valid bounds of the sheet.
Usage Example
java
Copy code
// Create a spreadsheet of size 3x3
Ex2Sheet sheet = new Ex2Sheet(3, 3);

// Set data for cell A1
sheet.setData(0, 0, "5");

// Set data for cell A2 with a formula
sheet.setData(1, 0, "=A1 + 3");

// Get data from cell A2 (will evaluate the formula)
String cellData = sheet.getData(1, 0);  // Output will be "8"

// Display the entire sheet
sheet.displaySheet();

// Check if coordinates are valid
boolean isValid = sheet.validateCoordinates(1, 2);  // Returns true if (1, 2) is within the bounds
Formula Handling
The SCell class supports basic formulas, such as:
=A1 + B2
=A1 * 5
=A2 / B1
The formulas are parsed and computed by the computeForm method, which supports arithmetic operations and recursive evaluation of nested formulas.
