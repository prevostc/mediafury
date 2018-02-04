package com.prevostc.mediafury.data;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.prevostc.mediafury.domain.enumeration.PersonRole;
import com.prevostc.mediafury.service.CategoryService;
import com.prevostc.mediafury.service.MoviePersonService;
import com.prevostc.mediafury.service.MovieService;
import com.prevostc.mediafury.service.PersonService;
import com.prevostc.mediafury.service.dto.CategoryDTO;
import com.prevostc.mediafury.service.dto.MovieDTO;
import com.prevostc.mediafury.service.dto.MoviePersonDTO;
import com.prevostc.mediafury.service.dto.PersonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class StubData implements CommandLineRunner {

    private final MovieService movieService;
    private final CategoryService categoryService;
    private final PersonService personService;
    private final MoviePersonService moviePersonService;
    private final Logger log = LoggerFactory.getLogger(StubData.class);

    StubData(MovieService movieService, CategoryService categoryService, PersonService personService, MoviePersonService moviePersonService) {
        this.movieService = movieService;
        this.categoryService = categoryService;
        this.personService = personService;
        this.moviePersonService = moviePersonService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();
        File jsonFile = new ClassPathResource("data/movies.json.dist").getFile();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        SimpleModule module =
            new SimpleModule("CustomMovieDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(MovieData.class, new CustomMovieDeserializer());
        mapper.registerModule(module);

        MovieData[] movies = mapper.readValue(jsonFile, MovieData[].class);
        for (MovieData movieData : movies) {
            if (movieData == null) {
                continue;
            }
            try {
                // save movie to get ID
                MovieDTO movieDTO = movieService.save(movieData.getMovieDTO());

                // save all categories to get the ID
                Set<CategoryDTO> categoryDTOs = movieData.getCategoryDTOs().stream()
                    .map(categoryService::save)
                    .collect(Collectors.toSet());
                movieDTO.setCategories(categoryDTOs);

                // save all persons
                Set<MoviePersonDTO> moviePersonDTOs = movieData.getMoviePersonStubDataDTOs().stream()
                    .map(moviePersonStubDataDTO -> new MoviePersonDTO(movieDTO, personService.save(moviePersonStubDataDTO.personDTO), moviePersonStubDataDTO.getRole()))
                    .map(moviePersonService::save)
                    .collect(Collectors.toSet());
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.error("Failed to save movie json '{}'", e);
                } else {
                    log.error("Failed to save movie json '{}': {}", e.getMessage());
                }
                throw e;
            }
        }
    }

    static class CustomMovieDeserializer extends StdDeserializer<MovieData> {

        static final String NOT_APPLICABLE = "N/A";
        static final String PERSON_SPLIT_PATTERN = "( ?\\(.+?\\))?, | ?\\(.+?\\)$";

        private final Logger log = LoggerFactory.getLogger(StubData.class);

        CustomMovieDeserializer() {
            this(null);
        }

        CustomMovieDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public MovieData deserialize(JsonParser parser, DeserializationContext deserializer) {
            MovieDTO movieDTO = new MovieDTO();
            ObjectCodec codec = parser.getCodec();
            try {
                JsonNode node = codec.readTree(parser);
                // set movie fields
                movieDTO.setElo(1000);
                movieDTO.setTitle(node.get("Title").asText());
                String poster = node.get("Poster").asText();
                movieDTO.setImageUrl(
                    poster.equals(CustomMovieDeserializer.NOT_APPLICABLE)
                        ? "http://via.placeholder.com/300x445?text=N/A"
                        : poster
                );
                movieDTO.setYear(Integer.parseInt(node.get("Year").asText().substring(0, 4)));

                // fetch all categories
                String[] categories = node.get("Genre").asText().split(", ");
                Set<CategoryDTO> categoryDTOs = Stream.of(categories)
                    .map(CategoryDTO::new)
                    .collect(Collectors.toSet())
                ;

                // fetch persons in custom DTO object
                Set<MoviePersonStubDataDTO> moviePersonStubDataDTOs = Stream.concat(
                    Stream.concat(
                        parsePersonField(node,"Director", PersonRole.DIRECTOR),
                        parsePersonField(node,"Writer", PersonRole.WRITER)
                    ),
                    parsePersonField(node,"Actors", PersonRole.ACTOR)
                ).collect(Collectors.toSet());

                return new MovieData(movieDTO, moviePersonStubDataDTOs, categoryDTOs);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.error("Failed to parse movies json '{}'", e);
                } else {
                    log.error("Failed to parse movies json '{}': {}", e.getMessage());
                }

                // @todo find a way to use Optional<MovieDTO> without getting "cannot select from parameterized type" when doing `module.addDeserializer(Optional<MovieDTO>.class, new CustomMovieDeserializer());
                return null;
            }
        }

        /**
         * Extract person info from text field
         * @param node Json node containing the field
         * @param field Field name to parse
         * @param role Role to attribute for these persons
         * @return A DTO stream
         */
        private Stream<MoviePersonStubDataDTO> parsePersonField(JsonNode node, String field, PersonRole role) {
            String persons = node.get(field).asText();
            if (persons.equals(CustomMovieDeserializer.NOT_APPLICABLE)) {
                return Stream.empty();
            }
            return Stream.of(persons.split(CustomMovieDeserializer.PERSON_SPLIT_PATTERN))
                .map(PersonDTO::new)
                .map(personDTO -> new MoviePersonStubDataDTO(role, personDTO));
        }
    }

    /**
     * Parse json into this class before saving it
     */
    static class MovieData {
        private MovieDTO movieDTO;
        private Set<MoviePersonStubDataDTO> moviePersonStubDataDTOs;
        private Set<CategoryDTO> categoryDTOs;

        MovieData(MovieDTO movieDTO, Set<MoviePersonStubDataDTO> moviePersonStubDataDTOs, Set<CategoryDTO> categoryDTOs) {
            this.movieDTO = movieDTO;
            this.moviePersonStubDataDTOs = moviePersonStubDataDTOs;
            this.categoryDTOs = categoryDTOs;
        }

        MovieDTO getMovieDTO() {
            return movieDTO;
        }

        Set<MoviePersonStubDataDTO> getMoviePersonStubDataDTOs() {
            return moviePersonStubDataDTOs;
        }

        Set<CategoryDTO> getCategoryDTOs() {
            return categoryDTOs;
        }
    }

    /**
     * Hold relationship between person and role for a movie
     */
    static class MoviePersonStubDataDTO {
        private PersonRole role;
        private PersonDTO personDTO;

        MoviePersonStubDataDTO(PersonRole role, PersonDTO personDTO) {
            this.role = role;
            this.personDTO = personDTO;
        }

        public PersonRole getRole() {
            return role;
        }

        public PersonDTO getPersonDTO() {
            return personDTO;
        }
    }
}
