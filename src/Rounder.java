public class Rounder {

    public int toWhole(double d){
        String round = String.valueOf(Math.rint(d));
        int index = round.indexOf(".");
        //if(index == -1) return (int) d;
        round = round.substring(0, index);
        return Integer.valueOf(round);
    }

}
