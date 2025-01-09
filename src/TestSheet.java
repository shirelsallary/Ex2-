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
        Ex2Sheet sheet = new Ex2Sheet(5,5);
        sheet.set(0,0,"3");
        sheet.set(0,1,"=A0+A0");
        List<String> l = new ArrayList<>();
        assertTrue(sheet.canBeComputedNow("A0",l,"3"));
        SCell s=sheet.get(0,1);
        String s1=s.getData();
        l.clear();
        assertTrue(sheet.canBeComputedNow("A1",l,s1));
        sheet.set(0,2,"=6+4");
        sheet.set(0,3,"hi");
        l.clear();
         assertTrue(sheet.canBeComputedNow("A2",l,sheet.get(0,2).getData()));
         l.clear();
       assertFalse(sheet.canBeComputedNow("A3",l,sheet.get(0,3).getData()));
       l.clear();
       sheet.set(1,0,"=A3");
       assertFalse(sheet.canBeComputedNow("b0",l,sheet.get(1,0).getData()));
       l.clear();
       sheet.set(1,1,"=A0");
       assertTrue(sheet.canBeComputedNow("b1",l,sheet.get(1,1).getData()));
       l.clear();
       sheet.set(1,2,"=B2");
       assertFalse(sheet.canBeComputedNow("b2",l,sheet.get(1,2).getData()));
       l.clear();
       sheet.set(1,3,"=S3");
       assertFalse(sheet.canBeComputedNow("b3",l,sheet.get(1,3).getData()));
       l.clear();
       sheet.set(2,0,"=C3");
       assertFalse(sheet.canBeComputedNow("c0",l,sheet.get(2,0).getData()));
       l.clear();
       sheet.set(2,1,"=C2");
       assertFalse(sheet.canBeComputedNow("c1",l,sheet.get(2,1).getData()));
       l.clear();
       sheet.set(2,2,"=C0");
       assertFalse(sheet.canBeComputedNow("c2",l,sheet.get(2,2).getData()));
       l.clear();
       sheet.set(2,3,"=C1");
       assertFalse(sheet.canBeComputedNow("c3",l,sheet.get(2,3).getData()));
       l.clear();
       sheet.set(3,0,"=C0+C1");
       assertFalse(sheet.canBeComputedNow("D0",l,sheet.get(3,0).getData()));
       l.clear();
       sheet.set(3,1,"=C0+3+5");
       assertFalse(sheet.canBeComputedNow("d1",l,sheet.get(3,1).getData()));
       l.clear();
       sheet.set(3,2,"=C0+5");
       assertFalse(sheet.canBeComputedNow("d2",l,sheet.get(3,2).getData()));
       l.clear();
       sheet.set(3,3,"=ou+8");
       assertFalse(sheet.canBeComputedNow("d3",l,sheet.get(3,3).getData()));


    }

    @Test
    public void setSCellType()
    {
        Ex2Sheet sheet = new Ex2Sheet(5,5);
        sheet.set(0,3,"hi");
        assertEquals(sheet.setCellType("A3",-2),0);
    }
    @Test
    public void get()
    {
        Ex2Sheet sheets = new Ex2Sheet(2,2);
        sheets.set(0,0,"3");
        assertEquals(sheets.get(0,0).getData(),"3");
       sheets.set(0,1,"=A0");
        assertEquals(sheets.get(0,1).getData(),"=A0");

    }
//    @Test
//    public void testdepth()
//    {
//        sheet.set(0,0,"5");
//        sheet.set(0,1,"9");
//        assertEquals(0,sheet.depth()[0][0]);
//    }
}
