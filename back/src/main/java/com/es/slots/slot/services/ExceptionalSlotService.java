package com.es.slots.slot.services;

import com.es.slots.slot.dtos.requests.ExceptionalSlotRequestDTO;
import com.es.slots.slot.entities.ExceptionalSlot;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import com.es.slots.slot.mapper.ExceptionalSlotMapper;
import com.es.slots.slot.repositories.ExceptionalSlotRepository;
import com.es.slots.slot.validators.SlotValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExceptionalSlotService {

    private final ExceptionalSlotRepository exceptionalSlotRepository;
    private final ExceptionalSlotMapper exceptionalSlotMapper;
    private final SlotValidator slotValidator;


    /**
     * Get all ExceptionalSlots
     * @return The list of all ExceptionalSlots
     */
    public List<ExceptionalSlot> getAll() {
        return exceptionalSlotRepository.findAll();
    }


    /**
     * Get one ExceptionalSlot by public id
     * @param slotPublicId The public id of the ExceptionalSlot to get
     * @return The ExceptionalSlot with the public id
     * @throws SlotNotFoundException If the ExceptionalSlot is not found
     */
    public ExceptionalSlot getOneSlotByPublicId(String slotPublicId) throws SlotNotFoundException {
        Optional<ExceptionalSlot> optionalExceptionalSlot = exceptionalSlotRepository.findByPublicId(slotPublicId);
        if (optionalExceptionalSlot.isEmpty()) {
            throw new SlotNotFoundException(slotPublicId);
        }
        return optionalExceptionalSlot.get();
    }


    /**
     * Create a new ExceptionalSlot
     * @param exceptionalSlotRequestDto The data to create the ExceptionalSlot
     * @return The created ExceptionalSlot
     * @throws SlotValidityFailureException If the ExceptionalSlot is not valid
     */
    public ExceptionalSlot create(ExceptionalSlotRequestDTO exceptionalSlotRequestDto) throws SlotValidityFailureException {
        slotValidator.checkValidityOfSlotRequest(exceptionalSlotRequestDto);
        ExceptionalSlot exceptionalSlot = exceptionalSlotMapper.buildExceptionalSlotFromRequest(new ExceptionalSlot(), exceptionalSlotRequestDto);
        return exceptionalSlotRepository.save(exceptionalSlot);
    }


    /**
     * Update a ExceptionalSlot by public id
     * @param slotPublicId The public id of the ExceptionalSlot to update
     * @param exceptionalSlotRequestDto The data to update
     * @return The updated ExceptionalSlot
     * @throws SlotNotFoundException If the ExceptionalSlot is not found
     * @throws SlotValidityFailureException If the ExceptionalSlot is not valid
     */
    public ExceptionalSlot update(String slotPublicId, ExceptionalSlotRequestDTO exceptionalSlotRequestDto) throws SlotNotFoundException, SlotValidityFailureException {
        slotValidator.checkValidityOfSlotRequest(exceptionalSlotRequestDto);
        ExceptionalSlot exceptionalSlot = exceptionalSlotMapper.buildExceptionalSlotFromRequest(this.getOneSlotByPublicId(slotPublicId), exceptionalSlotRequestDto);
        return exceptionalSlotRepository.save(exceptionalSlot);
    }


    /**
     * Delete a ExceptionalSlot by public id
     * @param publicId The public id of the ExceptionalSlot to delete
     * @throws SlotNotFoundException If the ExceptionalSlot is not found
     */
    public void deleteSlotByPublicId(String publicId) throws SlotNotFoundException {
        ExceptionalSlot exceptionalSlot = this.getOneSlotByPublicId(publicId);
        exceptionalSlotRepository.delete(exceptionalSlot);
    }


    /**
     * Get all ExceptionalSlots by date
     * @param date The date of the ExceptionalSlots to get
     * @return The list of ExceptionalSlots with the date
     */
    public List<ExceptionalSlot> getAllByDate(LocalDate date) {
        return exceptionalSlotRepository.findAllByDayDate(date);
    }

}
