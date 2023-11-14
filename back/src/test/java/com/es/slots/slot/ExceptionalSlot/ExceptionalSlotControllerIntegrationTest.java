package com.es.slots.slot.ExceptionalSlot;

import com.es.slots.slot.dtos.requests.ExceptionalSlotRequestDTO;
import com.es.slots.slot.dtos.responses.ExceptionalSlotResponseDTO;
import com.es.slots.slot.dtos.responses.OverlappingSlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.mapper.ExceptionalSlotMapper;
import com.es.slots.slot.services.ExceptionalSlotService;
import com.es.slots.slot.services.dtos.OverlappingSlotDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
 import org.springframework.security.test.context.support.WithMockUser;
 import org.springframework.test.context.junit4.SpringRunner;
 import org.springframework.test.web.servlet.MockMvc;
 import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
 import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 
 @SpringBootTest
public class ExceptionalSlotControllerIntegrationTest {
    
  
    @Autowired
     private WebApplicationContext context;
 
     private MockMvc mockMvc;

     @MockBean
    private ExceptionalSlotMapper exceptionalSlotMapper;
    
    @MockBean
    private OverlappingSlotDtoService overlappingSlotDtoService;

 
     @BeforeEach
     void setup() {
         mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
     }
 
     @MockBean
     private ExceptionalSlotService exceptionalSlotService;

 
     
     @WithMockUser(username = "spring", authorities = {"ADMIN"})
     @Test
     void testCreateSlot() throws Exception {
        //   Given.
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        ExceptionalSlotRequestDTO exceptionalSlotRequestDTO = new ExceptionalSlotRequestDTO();
        OverlappingSlotResponseDTO overlappingSlotResponseDTO = new OverlappingSlotResponseDTO();

        
        when(exceptionalSlotService.create(any(ExceptionalSlotRequestDTO.class))).thenReturn(exceptionalSlot);
        when(overlappingSlotDtoService.getOverlappingSlotResponseDTO(any(ExceptionalSlot.class))).thenReturn(overlappingSlotResponseDTO);
        System.out.println(exceptionalSlot.getCapacity());
                // When.
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/slot/exceptional")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(exceptionalSlotRequestDTO)));

         result.andExpect(status().isCreated())
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(asJsonString(overlappingSlotResponseDTO)));

     } 

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test

    void testGetSlots() throws Exception{
    ExceptionalSlotResponseDTO exceptionalSlotResponseDTO = new ExceptionalSlotResponseDTO();
    List<ExceptionalSlot> exceptionalSlotResponseDTOList = new ArrayList<>();
    
    when(exceptionalSlotService.getAll()).thenReturn(exceptionalSlotResponseDTOList);
    when(exceptionalSlotMapper.buildResponseFromExceptionalSlot(any())).thenReturn(exceptionalSlotResponseDTO);


    ResultActions result = mockMvc.perform(get("/slot/exceptional").contentType(MediaType.APPLICATION_JSON));

 //       Then.
      result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(asJsonString(exceptionalSlotResponseDTOList)));
  }


    
    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test

    void testGetSlotById () throws Exception {

        ExceptionalSlot exceptionalSlot = new ExceptionalSlot(null);

        ExceptionalSlotResponseDTO exceptionalSlotResponseDTO = new ExceptionalSlotResponseDTO(null);
        when(exceptionalSlotService.getOneSlotByPublicId("null")).thenReturn(exceptionalSlot);
        when (exceptionalSlotMapper.buildResponseFromExceptionalSlot(exceptionalSlot)).thenReturn(exceptionalSlotResponseDTO);


       //    When.
       ResultActions result = mockMvc.perform(get("/slot/exceptional/{id}", "null").contentType(MediaType.APPLICATION_JSON));
 
       //           Then.
        result                .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(asJsonString(exceptionalSlotResponseDTO)));
            }
           
    
    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test

    void testUpdateSlot () throws Exception {

        ExceptionalSlotRequestDTO exceptionalSlotRequestDTO = new ExceptionalSlotRequestDTO();
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        OverlappingSlotResponseDTO overlappingSlotResponseDTOs = new OverlappingSlotResponseDTO();

        
        when(exceptionalSlotService.update("null", exceptionalSlotRequestDTO)).thenReturn(exceptionalSlot);
       // when(overlappingSlotDtoService.getOverlappingSlotResponseDTO(any())).thenReturn(overlappingSlotResponseDTOs);
        when(overlappingSlotDtoService.getOverlappingSlotResponseDTO(any(ExceptionalSlot.class))).thenReturn(overlappingSlotResponseDTOs);

        ResultActions result = mockMvc.perform(patch("/slot/exceptional/{id}", "null").contentType(MediaType.APPLICATION_JSON).content(asJsonString(exceptionalSlotRequestDTO)));
 
 
        //     Then.
        result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(asJsonString(overlappingSlotResponseDTOs)));
         
        
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test

    void testDeleteSlot () throws Exception {


        Mockito.doNothing().when(exceptionalSlotService).deleteSlotByPublicId("null");
        //           When.
        ResultActions result = mockMvc.perform(delete("/slot/exceptional/{id}", "null"));
         
        //          Then.
        result.andExpect(status().isOk());
        
    
    
    }
    

     private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new JsonMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
        }
        
    
    