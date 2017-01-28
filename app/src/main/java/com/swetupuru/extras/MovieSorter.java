package com.swetupuru.extras;

import com.swetupuru.pojo.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by SWETABH on 1/2/2016.
 */
public class MovieSorter {

    public void sortMoviesByName(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }

    public void sortMoviesByDate(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getReleaseDateTheater().compareTo(rhs.getReleaseDateTheater());
            }
        });
    }

    public void sortMoviesByRatings(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                int ratingLhs= lhs.getAudienceScore();
                int ratingRhs=rhs.getAudienceScore();
                if(ratingLhs<ratingLhs)
                    return 1;
                else if(ratingLhs>ratingRhs)
                    return -1;
                else
                    return 0;
            }
        });
    }
}
