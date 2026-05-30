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
                .andExpect(jsonPath("$.dateOfHold").exists())
                .andExpect(jsonPath("$.dateOfCheckout").isEmpty())
                .andExpect(jsonPath("$.holdStatus", equalTo("HOLDING")));
    }

    @Test
    void checkoutBookRestCall() throws Exception {
        mockMvc.perform(post("/borrow/holds/018dc74a-4830-75cf-a194-5e9815727b02/checkout")
                        .with(jwt().jwt(jwt -> jwt.claim("email", "john.wick@continental.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("018dc74a-4830-75cf-a194-5e9815727b02")))
                .andExpect(jsonPath("$.patronId", equalTo("john.wick@continental.com")))
                .andExpect(jsonPath("$.dateOfCheckout").isNotEmpty())
                .andExpect(jsonPath("$.holdStatus", equalTo("ACTIVE")));
    }

    @Test
    void checkinBookRestCall() throws Exception {
        mockMvc.perform(post("/borrow/holds/018dc74a-9c4e-743f-916f-e152f190d13e/checkin")
                        .with(jwt().jwt(jwt -> jwt.claim("email", "john.wick@continental.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("018dc74a-9c4e-743f-916f-e152f190d13e")))
                .andExpect(jsonPath("$.bookBarcode", equalTo("55667788")))
                .andExpect(jsonPath("$.patronId", equalTo("john.wick@continental.com")))
                .andExpect(jsonPath("$.dateOfCheckin").isNotEmpty())
                .andExpect(jsonPath("$.holdStatus", equalTo("RETURNED")));
    }
}