package tools;

public class Locate {
    LLabel label;

    public Locate(LLabel l) {
        label = l;

    }

    public void change(LLabel l) {
        label = l;
    }
    public LLabel getlabel(){
        return label;
    }
}
