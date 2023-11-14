package com.es.slots.slot.controllers;


import com.es.slots.slot.dtos.requests.ExceptionalSlotRequestDTO;
import com.es.slots.slot.dtos.responses.OverlappingSlotResponseDTO;
import com.es.slots.slot.dtos.responses.ExceptionalSlotResponseDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import com.es.slots.slot.mapper.ExceptionalSlotMapper;
import com.es.slots.slot.services.ExceptionalSlotService;
import com.es.slots.slot.services.dtos.OverlappingSlotDtoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("slot/exceptional")
@AllArgsConstructor
public class ExceptionalSlotController {

    private final ExceptionalSlotService exceptionalSlotService;
    private final ExceptionalSlotMapper exceptionalSlotMapper;
    private final OverlappingSlotDtoService overlappingSlotDtoService;


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping()
    public ResponseEntity<List<ExceptionalSlotResponseDTO>> getAllExceptionalSlots() {
        List<ExceptionalSlotResponseDTO> exceptionalSlotResponseDTOList = exceptionalSlotService.getAll()
                .stream()
                .map(exceptionalSlotMapper::buildResponseFromExceptionalSlot)
                .toList();
        return new ResponseEntity<>(exceptionalSlotResponseDTOList, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping("{id}")
    public ResponseEntity<ExceptionalSlotResponseDTO> getSlotById(@PathVariable("id") String publicId) throws SlotNotFoundException {
        ExceptionalSlot exceptionalSlot = exceptionalSlotService.getOneSlotByPublicId(publicId);
        ExceptionalSlotResponseDTO exceptionalSlotResponseDTO = exceptionalSlotMapper.buildResponseFromExceptionalSlot(exceptionalSlot);
        return new ResponseEntity<>(exceptionalSlotResponseDTO, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<OverlappingSlotResponseDTO> create(@RequestBody ExceptionalSlotRequestDTO exceptionalSlotRequestDto) throws SlotValidityFailureException {
        ExceptionalSlot exceptionalSlot = exceptionalSlotService.create(exceptionalSlotRequestDto);
        OverlappingSlotResponseDTO overlappingSlotResponseDTO = overlappingSlotDtoService.getOverlappingSlotResponseDTO(exceptionalSlot);
        return new ResponseEntity<>(overlappingSlotResponseDTO, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("{id}")
    public ResponseEntity<OverlappingSlotResponseDTO> update(@PathVariable("id") String publicId, @RequestBody ExceptionalSlotRequestDTO exceptionalSlotRequestDto) throws SlotNotFoundException, SlotValidityFailureException {
        ExceptionalSlot exceptionalSlot = exceptionalSlotService.update(publicId, exceptionalSlotRequestDto);
        OverlappingSlotResponseDTO overlappingSlotResponseDTO = overlappingSlotDtoService.getOverlappingSlotResponseDTO(exceptionalSlot);
        return new ResponseEntity<>(overlappingSlotResponseDTO, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String publicId) throws SlotNotFoundException {
        exceptionalSlotService.deleteSlotByPublicId(publicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
