package com.es.slots.slot.StandardSlot;
//package com.es.slots.slot;
//
//import com.es.slots.slot.dtos.requests.SlotRequestDTO;
//import com.es.slots.slot.dtos.responses.SlotDTO;
//import com.es.slots.slot.dtos.responses.SlotResponseDTO;
//import com.es.slots.slot.entities.Slot;
//import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
//import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
//import com.es.slots.slot.services.SlotService;
//import com.es.slots.util.SlotUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//class SlotControllerIntegrationTest {
//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//    }
//
//    @MockBean
//    private SlotService slotService;
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testCreateSlot() throws Exception {
//        // Given.
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        SlotResponseDTO expectedSlotResponseDto = SlotUtil.createSlotResponseDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        when(slotService.create(any(SlotRequestDTO.class))).thenReturn(expectedSlotResponseDto);
//
//        // When.
//        ResultActions result = mockMvc.perform(post("/slot").contentType(MediaType.APPLICATION_JSON).content(asJsonString(slotRequestDto)));
//
//        // Then.
//        result.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(asJsonString(expectedSlotResponseDto)));
//    }
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testGetSlotById() throws Exception {
//        // Given.
//        String publicId = "d5bdc7ae-3a1b-4f4f-8b57-e751475ca4f1";
//        SlotResponseDTO expectedSlotResponseDto = SlotUtil.createSlotResponseDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        expectedSlotResponseDto.getSlotDto().setPublicId(publicId);
//        when(slotService.getSlotResponseDtoByPublicId(publicId)).thenReturn(expectedSlotResponseDto);
//
//        // When.
//        ResultActions result = mockMvc.perform(get("/slot/{id}", publicId).contentType(MediaType.APPLICATION_JSON));
//
//        // Then.
//        result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(asJsonString(expectedSlotResponseDto)));
//    }
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testGetSlots() throws Exception {
//        // Given.
//        SlotDTO slotDto = SlotUtil.createSlotDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        Slot overlappingSlot = SlotUtil.createStandardSlot(LocalTime.of(10, 0), LocalTime.of(12, 0), LocalDate.now(), 5);
//        // Here the slotDto and the overlappingSlot are overlapping.
//        SlotResponseDTO slotResponseDto1 = SlotUtil.createSlotResponseDTO(slotDto, List.of(SlotUtil.createSlotDTO(overlappingSlot)));
//
//        SlotResponseDTO slotResponseDto2 = SlotUtil.createSlotResponseDTO(LocalTime.of(15, 0), LocalTime.of(17, 0), LocalDate.now(), 19, SlotType.STANDARD);
//        List<SlotResponseDTO> expectedSlotResponseDtos = List.of(slotResponseDto1, slotResponseDto2);
//        when(slotService.getSlots()).thenReturn(expectedSlotResponseDtos);
//
//        // When.
//        ResultActions result = mockMvc.perform(get("/slot").contentType(MediaType.APPLICATION_JSON));
//
//        // Then.
//        result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(asJsonString(expectedSlotResponseDtos)));
//    }
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testGetSlotByIdNotFound() throws Exception {
//        // Given a publicId that doesn't exist in the system
//        String publicId = "non_existent_id";
//        when(slotService.getSlotResponseDtoByPublicId(publicId)).thenThrow(SlotNotFoundException.class);
//
//        // When.
//        ResultActions result = mockMvc.perform(get("/slot/{id}", publicId).contentType(MediaType.APPLICATION_JSON));
//
//        // Then.
//        result.andExpect(status().isNotFound());
//    }
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testCreateBadRequest() throws Exception {
//        // Given an invalid SlotRequestDTO (eg: startHour > endHour)
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(9, 0), LocalTime.of(8, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        when(slotService.create(slotRequestDto)).thenThrow(SlotValidityFailureException.class);
//
//        // When.
//        ResultActions result = mockMvc.perform(post("/slot").contentType(MediaType.APPLICATION_JSON).content(asJsonString(slotRequestDto)));
//
//        // Then.
//        result.andExpect(status().isBadRequest());
//    }
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testUpdateSlot() throws Exception {
//        // Given.
//        String publicId = "5332b2b1-d185-49a0-858e-d2c9797429a4";
//
//        SlotDTO slot = SlotUtil.createSlotDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 3, SlotType.STANDARD);
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(10, 0), LocalTime.of(11, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        slot.setPublicId(publicId);
//
//        SlotUtil.updateSlot(slot, slotRequestDto);
//
//        SlotResponseDTO expectedSlotResponseDto = SlotUtil.createSlotResponseDTO(slot);
//
//        when(slotService.update(publicId, slotRequestDto)).thenReturn(expectedSlotResponseDto);
//
//        // When.
//        ResultActions result = mockMvc.perform(patch("/slot/{id}", publicId).contentType(MediaType.APPLICATION_JSON).content(asJsonString(slotRequestDto)));
//
//
//        // Then.
//        result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(asJsonString(expectedSlotResponseDto)));
//
//
//    }
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testUpdateSlotFailed() throws Exception {
//
//        String publicId = "5332b2b1-d185-49a0-858e-d2c9797429a4";
//
//        SlotDTO slot = SlotUtil.createSlotDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 3, SlotType.STANDARD);
//        SlotRequestDTO slotRequestDto = SlotUtil.createSlotRequestDTO(LocalTime.of(10, 0), LocalTime.of(11, 0), LocalDate.now(), 10, SlotType.STANDARD);
//        slot.setPublicId("another_public_id");
//
//        slot = SlotUtil.updateSlot(slot, slotRequestDto);
//
//
//        when(slotService.update(publicId, slotRequestDto)).thenThrow(SlotNotFoundException.class);
//
//        //When
//        ResultActions result = mockMvc.perform(patch("/slot/{id}", publicId).contentType(MediaType.APPLICATION_JSON).content(asJsonString(slotRequestDto)));
//
//
//        // Then
//        result.andExpect(status().isNotFound());
//
//    }
//
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testDeleteSlot() throws Exception {
//        // Given.
//        String publicId = "bf0e99f3-80d8-40c7-979f-e6dba84e6dfd";
//        SlotDTO slot = SlotUtil.createSlotDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 3, SlotType.STANDARD);
//        slot.setPublicId(publicId);
//        when(slotService.delete(publicId)).thenReturn(publicId);
//
//        // When.
//        ResultActions result = mockMvc.perform(delete("/slot/{id}", publicId));
//
//        // Then.
//        result.andExpect(status().isOk());
//    }
//
//    @WithMockUser(username = "spring", authorities = {"ADMIN"})
//    @Test
//    void testDeleteSlotFailed() throws Exception {
//        // Given.
//        String publicId = "7197ae68-af01-47f0-88eb-607ef4dfd2d7";
//
//        // Create a slot for the test
//        SlotDTO slot = SlotUtil.createSlotDTO(LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.now(), 3, SlotType.STANDARD);
//        slot.setPublicId("empty");
//
//        when(slotService.delete(publicId)).thenThrow(SlotNotFoundException.class);
//
//        // When.
//        ResultActions result = mockMvc.perform(delete("/slot/{id}", publicId));
//
//        // Then.
//        result.andExpect(status().isNotFound());
//    }
//
//
//    private static String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper objectMapper = new ObjectMapper();
//            // Register the JavaTimeModule to handle Java 8 date/time types, including LocalTime
//            objectMapper.registerModule(new JavaTimeModule());
//
//            // Customize the serialization of LocalDate and LocalTime to use string representations
//            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//            objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
//
//            return objectMapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//}
