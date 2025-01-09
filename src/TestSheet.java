import assignments.ex2.CellEntry;
import assignments.ex2.Ex2Sheet;
import assignments.ex2.Ex2Utils;
import assignments.ex2.SCell;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSheet
{


    @Test
    public  void testCanBeComputedNow()
    {
        Ex2Sheet sheet = new Ex2Sheet(3,3);
        sheet.set(0,0,"3");
        sheet.set(0,1,"=A0");
        List<String> l = new ArrayList<>();
        assertTrue(sheet.canBeComputedNow("A0",l,"3"));
        String s=sheet.get(1,0).toString();
        assertTrue(sheet.canBeComputedNow("A1",l,s));

    }
//    @Test
//    public void testdepth()
//    {
//        sheet.set(0,0,"5");
//        sheet.set(0,1,"9");
//        assertEquals(0,sheet.depth()[0][0]);
//    }
}
