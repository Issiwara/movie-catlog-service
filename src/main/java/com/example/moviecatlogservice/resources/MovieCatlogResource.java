package com.example.moviecatlogservice.resources;

import com.example.moviecatlogservice.models.CatalogItem;
import com.example.moviecatlogservice.models.Movie;
import com.example.moviecatlogservice.models.Rating;
import com.example.moviecatlogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatlogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {



        //get all rated movie id

        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId , UserRating.class);
        //call movie info service microservice

        return ratings.getUserRating().stream().map( rating -> {

                    //For each id , -> call movie info service and get details
                    Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

                    //put all together
                    return new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating());
                })
        .collect(Collectors.toList());







    }
}




/* Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8082/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();
                    */
