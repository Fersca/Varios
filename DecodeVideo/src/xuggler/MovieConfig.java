package xuggler;

import java.util.ArrayList;

public class MovieConfig {

	private static ArrayList<Movie> movies = cargarMovies();

	private static ArrayList<Movie> cargarMovies() {

		ArrayList<Movie> movies = new ArrayList<Movie>();
		
		Movie m1 = new Movie();
		m1.name="Monster";
		m1.alto=45;
		m1.ancho=83;
		m1.colorFile="/home/fersca/pelis/monster2/mivi/monster.ser";
		m1.directory="/home/fersca/pelis/monster2/mivi/";
		
		movies.add(m1);
		
		return movies;
		
	}
	
	public static Movie getMovie(String name){
		
		for (Movie movie : movies) {
			if (movie.name.equals(name))
				return movie;
		}
		
		return null;
	}
	
}
