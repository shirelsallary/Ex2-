package assignments.ex2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestScell {
    @Test
    public void testisNumber() {
        SCell cell = new SCell();
        cell.setData("1");
        assertTrue(cell.isNumber(cell.getData()));
        cell.setData("-1");
        assertTrue(cell.isNumber(cell.getData()));
        cell.setData("1.9");
        assertTrue(cell.isNumber(cell.getData()));
        cell.setData("-1.9");
        assertTrue(cell.isNumber(cell.getData()));
        cell.setData("@@");
        assertFalse(cell.isNumber(cell.getData()));
        cell.setData("-5-");
        assertFalse(cell.isNumber(cell.getData()));
        cell.setData("3....");
        assertFalse(cell.isNumber(cell.getData()));
        cell.setData("0.-");
        assertFalse(cell.isNumber(cell.getData()));

    }


    @Test
    public void testcomputeForm() {
        SCell cell = new SCell();
        cell.setData("1");
        assertEquals(cell.computeForm(cell.getData()),"1");
        cell.setData("(1+2)*3/(4+4)");
        assertEquals(cell.computeForm(cell.getData()), "1.125");
        cell.setData("(1+2)+(2*(1+2))");
        assertEquals(cell.computeForm(cell.getData()),"9");
        cell.setData("((1+(2+2)*(1+2)))");
        assertEquals(cell.computeForm(cell.getData()), "13");

    }
    @Test
    public void testisForm() {
        SCell cell = new SCell();
        cell.setData("=1");
        assertTrue(cell.isForm(cell.getData()));
        cell.setData("=(1+2)*3/(A4+4)");
        assertTrue(cell.isForm(cell.getData()));
        cell.setData("=1.125");
        assertTrue(cell.isForm(cell.getData()));
        cell.setData("=)(");
        assertFalse(cell.isForm(cell.getData()));
        cell.setData("=1+2)*3/(4+4)");
        assertFalse(cell.isForm(cell.getData()));
        cell.setData("1.125");
        assertFalse(cell.isForm(cell.getData()));
        cell.setData("=(*1+2)");
        assertFalse(cell.isForm(cell.getData()));
        cell.setData("=#@$");
        assertFalse(cell.isForm(cell.getData()));
        cell.setData("=#");
        assertFalse(cell.isForm(cell.getData()));
        cell.setData("=");
        assertFalse(cell.isForm(cell.getData()));
        cell.setData("=(1+9)#)");
        assertFalse(cell.isForm(cell.getData()));

    }
}
