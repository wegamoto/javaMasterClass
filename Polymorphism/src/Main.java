import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

//        Movie movie = new Adventure("Star Wars");
//        movie.watchMovie();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter movie type (A, C, S, or Q to quit): ");
            String type = scanner.nextLine();
            if ("Qq".contains(type)) {
                break;
            }
            System.out.println("Enter movie title: ");
            String title = scanner.nextLine();
            System.out.println("Enter movie genre (or blank for none): ");
            String genre = scanner.nextLine();
            Movie movie = Movie.getMovie(type, title, genre);
            movie.watchMovie();
        }

    }
}