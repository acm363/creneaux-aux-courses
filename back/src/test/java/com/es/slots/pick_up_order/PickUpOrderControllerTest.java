package com.es.slots.pick_up_order;

import com.es.slots.pick_up_order.dto.requests.PickUpOrderRequest;
import com.es.slots.pick_up_order.dto.response.PickUpOrderResponse;
import com.es.slots.pick_up_order.mapper.PickUpOrderMapper;
import com.es.slots.pick_up_order.services.PickUpOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PickUpOrderControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PickUpOrderService pickUpOrderService;

    @MockBean
    private PickUpOrderMapper pickUpOrderMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void testGetAllPickUpOrdersByUser_AsAdmin() throws Exception {
        this.testGetAllPickUpOrdersByUser();
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void getPickUpOrderByIdAndUser_AsAdmin() throws Exception {
        this.testGetPickUpOrderByIdAndUser();
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void createPickUpOrder_AsAdmin() throws Exception {
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/pick-up-order")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(pickUpOrderRequest)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void updatePickUpOrder_AsAdmin() throws Exception {
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        mockMvc.perform(MockMvcRequestBuilders.patch("/pick-up-order/ceci-est-un-public-id")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(pickUpOrderRequest)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void removePickUpOrder_AsAdmin() throws Exception {
        this.removePickUpOrder();
    }


    // TESTS AS CLIENT
    @WithMockUser(username = "client", authorities = {"CLIENT"})
    @Test
    public void testGetAllPickUpOrdersByUser_AsClient() throws Exception {
        this.testGetAllPickUpOrdersByUser();
    }

    @WithMockUser(username = "client", authorities = {"CLIENT"})
    @Test
    void getPickUpOrderByIdAndUser_AsClient() throws Exception {
        this.testGetPickUpOrderByIdAndUser();
    }

    @WithMockUser(username = "client", authorities = {"CLIENT"})
    @Test
    void createPickUpOrder_AsClient() throws Exception {
        this.createPickUpOrder();
    }

    @WithMockUser(username = "client", authorities = {"CLIENT"})
    @Test
    void updatePickUpOrder_AsClient() throws Exception {
        this.updatePickUpOrder();
    }

    @WithMockUser(username = "client", authorities = {"CLIENT"})
    @Test
    void removePickUpOrder_AsClient() throws Exception {
        this.removePickUpOrder();
    }


    // TESTS AS ANONYMOUS
    @WithMockUser(username = "anonymous")
    @Test
    public void testGetAllPickUpOrdersByUser_AsAnonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pick-up-order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "anonymous")
    @Test
    void getPickUpOrderByIdAndUser_AsAnonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pick-up-order/ceci-est-un-public-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "anonymous")
    @Test
    void createPickUpOrder_AsAnonymous() throws Exception {
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/pick-up-order")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(pickUpOrderRequest)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "anonymous")
    @Test
    void updatePickUpOrder_AsAnonymous() throws Exception {
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        mockMvc.perform(MockMvcRequestBuilders.patch("/pick-up-order/ceci-est-un-public-id")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(pickUpOrderRequest)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "anonymous")
    @Test
    void removePickUpOrder_AsAnonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/pick-up-order/ceci-est-un-public-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    // ======================================================================= //

    /**
     * Write object as JsonString
     * @param obj object to convert
     * @return jsonString
     */
    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Test of testGetAllPickUpOrdersByUser() method
     * @throws Exception if failure of MockMVC
     */
    private void testGetAllPickUpOrdersByUser() throws Exception {
        List<PickUpOrderResponse> pickUpOrderResponses = new ArrayList<>();

        Mockito.when(pickUpOrderService.getAllPickUpOrdersByUser()).thenReturn(null);
        Mockito.when(pickUpOrderMapper.buildPickUpOrderResponseDTOList(any())).thenReturn(pickUpOrderResponses);

        // Perform the GET request and assert the result using MockMVC
        mockMvc.perform(MockMvcRequestBuilders.get("/pick-up-order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(pickUpOrderResponses)));

        // You can add more assertions as needed
        Mockito.verify(pickUpOrderService, Mockito.times(1)).getAllPickUpOrdersByUser();
        Mockito.verify(pickUpOrderMapper, Mockito.times(1)).buildPickUpOrderResponseDTOList(any());
    }

    /**
     * Test of testGetPickUpOrderByIdAndUser() method
     * @throws Exception if failure of MockMVC
     */
    private void testGetPickUpOrderByIdAndUser() throws Exception {
        PickUpOrderResponse pickUpOrderResponse = new PickUpOrderResponse();
        Mockito.when(pickUpOrderMapper.buildPickUpOrderResponseDTO(any())).thenReturn(null);
        Mockito.when(pickUpOrderMapper.buildPickUpOrderResponseDTO(any())).thenReturn(pickUpOrderResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/pick-up-order/ceci-est-un-public-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(pickUpOrderResponse)));

        Mockito.verify(pickUpOrderService, Mockito.times(1)).getPickUpOrderByIdAndUser(any());
        Mockito.verify(pickUpOrderMapper, Mockito.times(1)).buildPickUpOrderResponseDTO(any());
    }



    private void createPickUpOrder() throws Exception {
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest();
        PickUpOrderResponse pickUpOrderResponse = new PickUpOrderResponse();

        Mockito.when(pickUpOrderService.createNewPickUpOrder(any())).thenReturn(null);
        Mockito.when(pickUpOrderMapper.buildPickUpOrderResponseDTO(any())).thenReturn(pickUpOrderResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/pick-up-order")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(pickUpOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(pickUpOrderResponse)));

        Mockito.verify(pickUpOrderService, Mockito.times(1)).createNewPickUpOrder(any());
        Mockito.verify(pickUpOrderMapper, Mockito.times(1)).buildPickUpOrderResponseDTO(any());
    }


    private void updatePickUpOrder() throws Exception {
        PickUpOrderRequest pickUpOrderRequest = new PickUpOrderRequest(LocalDate.now(), "uuid");
        Mockito.doNothing().when(pickUpOrderService).updatePickUpOrder(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.patch("/pick-up-order/ceci-est-un-public-id")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(pickUpOrderRequest)))
                .andExpect(status().isOk());

        Mockito.verify(pickUpOrderService, Mockito.times(1)).updatePickUpOrder(any(), any());
    }


    /**
     * Test of removePickUpOrder()
     * @throws Exception if the mockMVC fail
     */
    private void removePickUpOrder() throws Exception {
        Mockito.doNothing().when(pickUpOrderService).removePickUpOrder(any());
        mockMvc.perform(MockMvcRequestBuilders.delete("/pick-up-order/ceci-est-un-public-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}