package com.es.slots.slot.controllers;


import com.es.slots.slot.dtos.requests.StandardSlotRequestDTO;
import com.es.slots.slot.dtos.responses.OverlappingSlotResponseDTO;
import com.es.slots.slot.dtos.responses.StandardSlotResponseDTO;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import com.es.slots.slot.mapper.StandardSlotMapper;
import com.es.slots.slot.services.dtos.OverlappingSlotDtoService;
import com.es.slots.slot.services.StandardSlotService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("slot/standard")
@AllArgsConstructor
public class StandardSlotController {

    private final StandardSlotService standardSlotService;
    private final StandardSlotMapper standardSlotMapper;
    private final OverlappingSlotDtoService overlappingSlotDtoService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping()
    public ResponseEntity<List<StandardSlotResponseDTO>> getAllStandardSlots() {
        List<StandardSlotResponseDTO> standardSlotResponseDTOList = standardSlotService.getAll()
                .stream()
                .map(standardSlotMapper::buildResponseFromStandardSlot)
                .toList();
        return new ResponseEntity<>(standardSlotResponseDTOList, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping("{id}")
    public ResponseEntity<StandardSlotResponseDTO> getOneStandardSlotByPublicId(@PathVariable("id") String publicId)
            throws SlotNotFoundException {
        StandardSlot standardSlot = standardSlotService.getOneSlotByPublicId(publicId);
        StandardSlotResponseDTO standardSlotResponseDTO = standardSlotMapper.buildResponseFromStandardSlot(standardSlot);
        return new ResponseEntity<>(standardSlotResponseDTO, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<OverlappingSlotResponseDTO> createStandardSlot(@RequestBody @Valid StandardSlotRequestDTO standardSlotRequestDTO)
            throws SlotValidityFailureException {
        StandardSlot standardSlot = standardSlotService.create(standardSlotRequestDTO);
        OverlappingSlotResponseDTO overlappingSlotResponseDTO = overlappingSlotDtoService.getOverlappingSlotResponseDTO(standardSlot);
        return new ResponseEntity<>(overlappingSlotResponseDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("{id}")
    public ResponseEntity<OverlappingSlotResponseDTO> updateStandardSlot(@PathVariable("id") String publicId,
                                                                         @Valid @RequestBody StandardSlotRequestDTO standardSlotRequestDto)
            throws SlotNotFoundException, SlotValidityFailureException {
        StandardSlot standardSlot = standardSlotService.update(publicId, standardSlotRequestDto);
        OverlappingSlotResponseDTO overlappingSlotResponseDTO = overlappingSlotDtoService.getOverlappingSlotResponseDTO(standardSlot);
        return new ResponseEntity<>(overlappingSlotResponseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteStandardSlot(@PathVariable("id") String publicId) throws SlotNotFoundException {
        standardSlotService.deleteSlotByPublicId(publicId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
