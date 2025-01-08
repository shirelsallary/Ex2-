package assignments.ex2;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private char x;
    private int y;
    private String cord;


    @Override
    public boolean isValid() {
        if (x==' ') {
            return false;
        }

        if(!Character.isLetter(x)) return false;
        if(y<0||y>99)return false;
        return true;
    }
    public CellEntry(char x, int y) {
        this.x = x;
        this.y = y;
        cord=x+String.valueOf(y);
    }
    public CellEntry(int x, int y) {
        this.x=(char) ('A' + x);
        this.y= y;
        cord=x+String.valueOf(y);
    }
    public CellEntry() {
        x=' ';

    }

    @Override
    public int getX() {
        return x-'A';
    }

    @Override
    public int getY() {return y;}

}
