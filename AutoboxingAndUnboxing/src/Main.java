import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


public class Main {
    public static void main(String[] args) {

        String[] strArray = new String[10];
        int[] intArray = new int[10];

        ArrayList<String> strArrayList = new ArrayList<String>();
        strArrayList.add("Tim");

//        ArrayList<int> intArrayList = new ArrayList<int>();
        ArrayList<IntClass> intClassArrayList = new ArrayList<IntClass>();
        intClassArrayList.add(new IntClass(54));
        Integer integer = new Integer(54);
        Double doubleValue = new Double(12.25);

        ArrayList<Integer> intArrayList = new ArrayList<Integer>();
//        for (int i=0; i<10; i++) {
//            intArrayList.add(Integer.valueOf(i));
//        }
//
//        for (int i=0; i<10; i++) {
//           System.out.println(i + " --> " + intArrayList.get(i).intValue());
//        }

        Integer myIntValue = 56; // valueOf(56)
        int myInt = myIntValue;

        ArrayList<Double> myDoubleValues = new ArrayList<Double>();
        for(double dbl =0.0; dbl<10.0; dbl +=0.5) {
            myDoubleValues.add(Double.valueOf(dbl));
        }

        for(int i=0; i<myDoubleValues.size(); i++) {
            double value = myDoubleValues.get(i).doubleValue();
            System.out.println(i + " -- " + value);
        }
    }
}