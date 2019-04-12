package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
		// You must find a way to build inverted index before you do an actual search.
		// Inverted index would looks like this:
		// -------------------------------
		// |  Term      | Movie Ids      |
		// -------------------------------
		// |  Star      |  5, 8, 1       |
		// |  War       |  5, 2          |
		// |  Trek      |  1, 8          |
		// -------------------------------
		// When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
		// there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
		// Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
		// from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
		// you have to return can be union or intersection of those 2 sets of ids.
		// By the way, in this assignment, you must use intersection so that it left for just movie id 5.

		return invertedIndex(queryText);
	}

	private List<Movie> invertedIndex(String queryText) {

		Map<String, HashSet<Long>> invertedIndex = new HashMap<>();
		for(Movie movie : movieRepository.findAll()) {
			String[] words = movie.getName().split(" ");
			for(String word : words){
				if(!invertedIndex.containsKey(word)) {
					invertedIndex.put(word, new HashSet<>());
				}
				invertedIndex.get(word).add(movie.getId());
			}
		}

		// Split query text
		List<String> queryTexts = Arrays.asList(WordUtils.capitalizeFully(queryText).split(" "));
		List<Movie> movieResult = new ArrayList<>();
		HashSet<Long> movieIdSet = new HashSet<>();

		// If query word not match
		if(invertedIndex.get(queryTexts.get(0)) == null) {
			return new ArrayList<>();
		}

		for(String word : queryTexts) {
			if(movieIdSet.isEmpty()) movieIdSet = invertedIndex.get(word);
			movieIdSet.retainAll(invertedIndex.get(word));
		}

		for(long id : movieIdSet){
			movieResult.add(movieRepository.findById(id));
		}
		return movieResult;
	}
}
