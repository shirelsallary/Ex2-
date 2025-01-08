import assignments.ex2.CellEntry;
import assignments.ex2.Ex2Sheet;
import assignments.ex2.Ex2Utils;
import assignments.ex2.SCell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSheet
{
    Ex2Sheet sheet = new Ex2Sheet();
    SCell cell1 = new SCell(Ex2Utils.EMPTY_CELL);
    CellEntry index=new CellEntry(0,0);
    @Test
    public void testdepth()
    {

        sheet.set(0,0,"5");
        sheet.set(0,1,"9");
        assertEquals(0,sheet.depth()[0][0]);



    }
}
