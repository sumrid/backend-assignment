package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class

		String queryTextCap = WordUtils.capitalizeFully(queryText);

		MoviesResponse result = movieDataService.fetchAll();
		List<Movie> movies = new ArrayList<>();

		for(MovieData movieData : result){
			List<String> name = Arrays.asList(movieData.getTitle().split(" "));
			if(name.contains(queryTextCap)){
				Movie movie = new Movie(movieData.getTitle(), movieData.getCast());
				movies.add(movie);
			}
		}

		return movies;
	}
}
