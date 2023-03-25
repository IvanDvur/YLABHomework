package io.ylab.intensive.lesson04.movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Movie {
    private Integer year;
    private Integer length;
    private String title;
    private String subject;
    private String actors;
    private String actress;
    private String director;
    private Integer popularity;
    private Boolean awards;

    public Movie(Integer year, Integer length, String title, String subject, String actors,
                 String actress, String director, Integer popularity, Boolean awards) {
        this.year = year;
        this.length = length;
        this.title = title;
        this.subject = subject;
        this.actors = actors;
        this.actress = actress;
        this.director = director;
        this.popularity = popularity;
        this.awards = awards;
    }

    public List<Object> getParameters(){
        List<Object> stringParams = new ArrayList<>();
        Collections.addAll(stringParams,year,length,title,subject,actors,actress,director,popularity,awards);
        return stringParams;
    }

}
