package com.es.slots.slot.ExceptionalSlot;


import com.es.slots.authentication.services.AuthenticationUserService;
import com.es.slots.pick_up_order.entities.PickUpOrder;
import com.es.slots.pick_up_order.exceptions.customs.PickUpOrderNotFoundException;
import com.es.slots.pick_up_order.repositories.PickUpOrderRepository;
import com.es.slots.slot.dtos.requests.ExceptionalSlotRequestDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import com.es.slots.slot.repositories.ExceptionalSlotRepository;
import com.es.slots.slot.services.ExceptionalSlotService;
import com.es.slots.user.entities.Admin;
import com.es.slots.user.entities.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;



/**
 * Test en tant que clients des services de ExceptionalSlotService
 */

@SpringBootTest
public class ExceptionalSlotServiceUnitTest {

    @Autowired
    private ExceptionalSlotService exceptionalSlotService;

    
    @MockBean
    private AuthenticationUserService authenticationUserService;

    @MockBean
    private ExceptionalSlotRepository exceptionalSlotRepository;

    private List<ExceptionalSlot> exceptionalSlots;

    @BeforeEach
    public void setUp() {
        exceptionalSlots = new ArrayList<>();
        for (int i = 0; i< 10; i++) {
            ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
            exceptionalSlots.add(exceptionalSlot);

        }
    Mockito.when(exceptionalSlotRepository.findAll()).thenReturn(exceptionalSlots);
    }

    @Test
    void getAllExceptionalSlots() {
        
        List<ExceptionalSlot> results = exceptionalSlotService.getAll();
        Assertions.assertEquals (exceptionalSlots.size(), results.size());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findAll();
    }

    @Test

    void getExceptionalSlotById () throws SlotNotFoundException{
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.of(exceptionalSlot));
        ExceptionalSlot result = exceptionalSlotService.getOneSlotByPublicId("publicId");
        Assertions.assertEquals(exceptionalSlot, result);
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(anyString());
    }
    
    @Test 
    void getExceptionalSlotById_ShouldThrowExceptionalSlotNotFound(){
        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(SlotNotFoundException.class, () -> exceptionalSlotService.getOneSlotByPublicId("publicId"));
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(anyString());
    }

    @Test 
    void updateExceptionalSlot() throws SlotNotFoundException, SlotValidityFailureException{
        ExceptionalSlot exceptionalSlot = new ExceptionalSlot();
        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.of(exceptionalSlot));
        Mockito.when(exceptionalSlotRepository.save(any(ExceptionalSlot.class))).thenReturn(exceptionalSlot);
        
        ExceptionalSlotRequestDTO exceptionalSlotRequestDTO = new ExceptionalSlotRequestDTO();
        exceptionalSlotRequestDTO.setStartHour(LocalTime.of(18, 0));
        exceptionalSlotRequestDTO.setEndHour(LocalTime.of(19, 0));
        exceptionalSlotRequestDTO.setCapacity(3);
        exceptionalSlotRequestDTO.setDate(LocalDate.now());
        
        ExceptionalSlot result = exceptionalSlotService.update("publicId", exceptionalSlotRequestDTO);
        Assertions.assertEquals(exceptionalSlot, result);
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(anyString());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).save(any(ExceptionalSlot.class));
    }

    @Test
    void updateExceptionalSlot_ShouldThrowException() throws SlotNotFoundException, SlotValidityFailureException {
        
        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.empty());
        
        ExceptionalSlotRequestDTO exceptionalSlotRequestDTO = new ExceptionalSlotRequestDTO();
        exceptionalSlotRequestDTO.setStartHour(LocalTime.of(18, 0));
        exceptionalSlotRequestDTO.setEndHour(LocalTime.of(19, 0));
        exceptionalSlotRequestDTO.setCapacity(3);
        exceptionalSlotRequestDTO.setDate(LocalDate.now());
        
        Assertions.assertThrows(SlotNotFoundException.class, () -> exceptionalSlotService.update("publicId", exceptionalSlotRequestDTO));
        
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId("publicId");
        
        Mockito.verify(exceptionalSlotRepository, Mockito.times(0)).save(any(ExceptionalSlot.class));
        
    }
    
    @Test
    void deleteExceptionalSlot () throws SlotNotFoundException {

        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.of(exceptionalSlots.get(0)));
        Mockito.doAnswer(answer -> {
            exceptionalSlots.remove(0);
            return null;
        }).when(exceptionalSlotRepository).delete(any());

        Assertions.assertEquals(10, exceptionalSlots.size());
        exceptionalSlotService.deleteSlotByPublicId("publicId");
        Assertions.assertEquals(9, exceptionalSlots.size());


    }

    @Test
    void deleteExceptionalSlot_shouldThrowException() throws SlotNotFoundException {
        Mockito.when(exceptionalSlotRepository.findByPublicId(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(SlotNotFoundException.class, () -> exceptionalSlotService.deleteSlotByPublicId("publicId"));
        Mockito.verify(exceptionalSlotRepository, Mockito.times(1)).findByPublicId(anyString());
        Mockito.verify(exceptionalSlotRepository, Mockito.times(0)).delete(any());
    }

    
}
