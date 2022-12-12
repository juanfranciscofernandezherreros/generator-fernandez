package com.bme.clp.bck.resources.q.infrastructure.spring.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"integration-test"})
@SpringBootTest(classes = {WebConfig.class})
class WebConfigITTest {

    @Autowired
    private WebConfig config;

//    @Test
    void checkConfigIsThere() {
        assertNotNull(config, () -> "the config should be up");
    }
}
