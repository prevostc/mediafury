package com.prevostc.mediafury.controller;

import com.prevostc.mediafury.config.ApiSecurityConfiguration;
import com.prevostc.mediafury.config.AuthenticationEntryPoint;
import com.prevostc.mediafury.config.CorsConfiguration;
import com.prevostc.mediafury.model.Movie;
import com.prevostc.mediafury.repository.MovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
@Import({ ApiSecurityConfiguration.class, AuthenticationEntryPoint.class })
public class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MovieRepository movieRepository;

    @Test
    public void listAll_shouldBeAuthenticated() throws Exception {

        //Arrange
        Page<Movie> res = new PageImpl<>(Arrays.asList(
            new Movie("Kung", "Fury"),
            new Movie("Kung 2", "Fury")
        ));
        when(movieRepository.findAll(any(PageRequest.class))).thenReturn(res);

        //Act
        ResultActions result = mvc.perform(get("/admin/movies").with(anonymous()));

        //Assert
        result.andExpect(status().is(401));
    }


    @Test
    public void listAll_worksIfAuthenticated() throws Exception {

        //Arrange
        Page<Movie> res = new PageImpl<>(Arrays.asList(
            new Movie("Kung", "Fury"),
            new Movie("Kung 2", "Fury")
        ));
        when(movieRepository.findAll(any(PageRequest.class))).thenReturn(res);

        //Act
        ResultActions result = mvc.perform(
            get("/admin/movies").with(user("user").password("").roles("USER", "ADMIN"))
        );

        //Assert
        result.andExpect(status().is(200));
    }
}
