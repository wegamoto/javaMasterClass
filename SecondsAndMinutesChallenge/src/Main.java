//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static  final String INVALID_VALUE_MESSAGE = "Invalid value";

    public static void main(String[] args) {

        System.out.println(getDurationString(65,45));
        System.out.println(getDurationString(3945L));
        System.out.println(getDurationString(-40));
        System.out.println(getDurationString(65,9));
    }

    private static String getDurationString(long minutes, long seconds) {
        if((minutes < 0) || (seconds <0) || (seconds > 59)) {
            return INVALID_VALUE_MESSAGE;
        }

        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        String hoursString = hours + "h";
        if(hours < 10) {
            hoursString = "0" + hoursString;
        }

        String minutesString = remainingMinutes + "h";
        if(remainingMinutes < 10) {
            minutesString = "0" + minutesString;
        }

        String secondsString = seconds + "h";
        if(seconds < 10) {
            secondsString = "0" + secondsString;
        }

        return hoursString + "h " + remainingMinutes + "m " + seconds + "s";
    }

    private static String getDurationString(long seconds) {
        if(seconds < 0) {
            return INVALID_VALUE_MESSAGE;
        }

        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        return getDurationString(minutes, remainingSeconds);
    }
}