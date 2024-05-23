package example.borrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ApplicationModuleTest
class CirculationDeskControllerIT {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:borrow.sql");
    }

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void placeHoldRestCall() throws Exception {
        mockMvc.perform(post("/borrow/holds")
                        .with(jwt().jwt(jwt -> jwt.claim("email", "john.wick@continental.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "barcode": "64321704"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.bookBarcode", equalTo("64321704")))
                .andExpect(jsonPath("$.patronId", equalTo("john.wick@continental.com")))
                .andExpect(jsonPath("$.dateOfHold").exists());
    }

    @Test
    void checkoutBookRestCall() throws Exception {
        mockMvc.perform(post("/borrow/holds/018dc74a-4830-75cf-a194-5e9815727b02/checkout")
                        .with(jwt().jwt(jwt -> jwt.claim("email", "john.wick@continental.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holdId", equalTo("018dc74a-4830-75cf-a194-5e9815727b02")))
                .andExpect(jsonPath("$.patronId", equalTo("john.wick@continental.com")))
                .andExpect(jsonPath("$.dateOfCheckout").exists());
    }
}
