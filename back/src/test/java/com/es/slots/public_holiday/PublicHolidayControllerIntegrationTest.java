package com.es.slots.public_holiday;

import com.es.slots.public_holiday.dto.requests.PublicHolidayRequestDTO;
import com.es.slots.public_holiday.dto.responses.PublicHolidayResponseDTO;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayAlreadyExistException;
import com.es.slots.public_holiday.exceptions.customs.PublicHolidayNotFoundException;
import com.es.slots.public_holiday.services.PublicHolidayService;
import com.es.slots.util.PublicHolidayUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PublicHolidayControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private PublicHolidayService publicHolidayService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testCreatePublicHoliday() throws Exception {
        // Given.
        PublicHolidayRequestDTO requestDto = PublicHolidayUtil.createPublicHolidayRequestDTO(LocalDate.now(), "Holiday");
        PublicHolidayResponseDTO expectedResponseDto = PublicHolidayUtil.createPublicHolidayResponseDTO(LocalDate.now(), "Holiday");
        when(publicHolidayService.create(any(PublicHolidayRequestDTO.class))).thenReturn(expectedResponseDto);

        // When.
        ResultActions result = mockMvc.perform(post("/public-holiday").contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestDto)));

        // Then.
        result.andExpect(status().isCreated()).andExpect(content().json(asJsonString(expectedResponseDto)));
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testGetPublicHolidayById() throws Exception {
        // Given.
        PublicHolidayResponseDTO expectedResponseDto = PublicHolidayUtil.createPublicHolidayResponseDTO(LocalDate.now(), "Holiday");
        String publicId = expectedResponseDto.getPublicId();
        when(publicHolidayService.getPublicHolidayById(publicId)).thenReturn(expectedResponseDto);

        // When.
        ResultActions result = mockMvc.perform(get("/public-holiday/{id}", publicId).contentType(MediaType.APPLICATION_JSON));

        // Then.
        result.andExpect(status().isOk()).andExpect(content().json(asJsonString(expectedResponseDto)));
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testGetAllPublicHolidays() throws Exception {
        // Given.
        PublicHolidayResponseDTO expectedResponseDto1 = PublicHolidayUtil.createPublicHolidayResponseDTO(LocalDate.now(), "Holiday");
        PublicHolidayResponseDTO expectedResponseDto2 = PublicHolidayUtil.createPublicHolidayResponseDTO(LocalDate.now().plusDays(1), "Holiday bis");
        List<PublicHolidayResponseDTO> expectedResponseDtoList = List.of(expectedResponseDto1, expectedResponseDto2);
        when(publicHolidayService.getPublicHolidays()).thenReturn(expectedResponseDtoList);

        // When.
        ResultActions result = mockMvc.perform(get("/public-holiday").contentType(MediaType.APPLICATION_JSON));

        // Then.
        result.andExpect(status().isOk()).andExpect(content().json(asJsonString(expectedResponseDtoList)));
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testRemovePublicHoliday() throws Exception {
        // Given.
        PublicHolidayRequestDTO requestDto = PublicHolidayUtil.createPublicHolidayRequestDTO(LocalDate.now(), "Holiday");
        PublicHolidayResponseDTO expectedResponseDto = PublicHolidayUtil.createPublicHolidayResponseDTO(LocalDate.now(), "Holiday");
        when(publicHolidayService.create(any(PublicHolidayRequestDTO.class))).thenReturn(expectedResponseDto);

        // When.
        ResultActions result = mockMvc.perform(post("/public-holiday").contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestDto)));

        // Then.
        result.andExpect(status().isCreated()).andExpect(content().json(asJsonString(expectedResponseDto)));
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testUpdatePublicHoliday() throws Exception {
        // Given.
        PublicHolidayRequestDTO requestDto = PublicHolidayUtil.createPublicHolidayRequestDTO(LocalDate.now(), "Holiday");
        PublicHolidayResponseDTO expectedResponseDto = PublicHolidayUtil.createPublicHolidayResponseDTO(LocalDate.now(), "Holiday");
        when(publicHolidayService.update(any(String.class), any(PublicHolidayRequestDTO.class))).thenReturn(expectedResponseDto);

        // When.
        ResultActions result = mockMvc.perform(patch("/public-holiday/{id}", expectedResponseDto.getPublicId()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestDto)));

        // Then.
        result.andExpect(status().isOk()).andExpect(content().json(asJsonString(expectedResponseDto)));
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testRemovePublicHolidayById() throws Exception {
        // Given.
        String publicId = "d5bdc7ae-3a1b-4f4f-8b57-e751475ca4f1";

        // When.
        ResultActions result = mockMvc.perform(delete("/public-holiday/{id}", publicId).contentType(MediaType.APPLICATION_JSON));

        // Then.
        result.andExpect(status().isOk());
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testGetSlotByIdNotFound() throws Exception {
        // Given a publicId that doesn't exist in the system
        String publicId = "non_existent_id";
        when(publicHolidayService.getPublicHolidayById(publicId)).thenThrow(PublicHolidayNotFoundException.class);

        // When.
        ResultActions result = mockMvc.perform(get("/public-holiday/{id}", publicId).contentType(MediaType.APPLICATION_JSON));

        // Then.
        result.andExpect(status().isNotFound());
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testCreateBadRequest() throws Exception {
        // Given.
        PublicHolidayRequestDTO requestDto = PublicHolidayUtil.createPublicHolidayRequestDTO(LocalDate.now(), "Holiday");
        when(publicHolidayService.create(any(PublicHolidayRequestDTO.class))).thenThrow(PublicHolidayAlreadyExistException.class);

        // When.
        ResultActions result = mockMvc.perform(post("/public-holiday").contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestDto)));

        // Then.
        result.andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "spring", authorities = {"ADMIN"})
    @Test
    void testCreateBadRequestWhenDateIsAPublicHoliday() throws Exception {
        // Given.
        PublicHolidayRequestDTO requestDto = PublicHolidayUtil.createPublicHolidayRequestDTO(LocalDate.now(), "Holiday");
        when(publicHolidayService.create(any(PublicHolidayRequestDTO.class))).thenThrow(PublicHolidayAlreadyExistException.class);

        // When.
        ResultActions result = mockMvc.perform(post("/public-holiday").contentType(MediaType.APPLICATION_JSON).content(asJsonString(requestDto)));

        // Then.
        result.andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
