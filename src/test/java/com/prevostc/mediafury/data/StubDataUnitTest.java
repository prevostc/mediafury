package com.prevostc.mediafury.data;

import com.prevostc.mediafury.MediafuryApp;
import com.prevostc.mediafury.service.CategoryService;
import com.prevostc.mediafury.service.MoviePersonService;
import com.prevostc.mediafury.service.MovieService;
import com.prevostc.mediafury.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediafuryApp.class)
public class StubDataUnitTest {

    private StubData stubData;

    @Autowired
    private MovieService movieService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PersonService personService;

    @Autowired
    private MoviePersonService moviePersonService;

    @Before
    public void setup() {
        stubData = new StubData(movieService, categoryService, personService, moviePersonService);
    }

    @Test
    @Transactional
    public void testdataIsFetchedFromJson() throws Exception {
        stubData.run();
    }

}
