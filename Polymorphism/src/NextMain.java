public class NextMain {

    public static void main(String[] args) {
        Movie movie = Movie.getMovie("A", "Jaws");
        movie.watchMovie();

        Adventure jaws = (Adventure) Movie.getMovie("C", "Jaws");
        jaws.watchMovie();

        Object movie2 = Movie.getMovie("C", "Airplane");
        Comedy movie3 = (Comedy) movie2;
        movie3.watchComedy();

//        var airplane : Movie = Movie.getMovie("C", "Airplane");
//        airplane.watchMovie();

        var plane = new Comedy("Airplane");
        plane.watchMovie();

        Object unknownObject = Movie.getMovie("C", "Airplane");
        if (unknownObject.getClass().getSimpleName() == "Comedy") {
            Comedy comedy = (Comedy) unknownObject;
            comedy.watchMovie();
        } else if (unknownObject instanceof Adventure) {
            ((Adventure) unknownObject).watchMovie();
        } else if (unknownObject instanceof ScienceFiction syfy) {
            syfy.watchScienceFiction();
        }
    }
}
