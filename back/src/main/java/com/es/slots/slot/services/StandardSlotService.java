package com.es.slots.slot.services;

import com.es.slots.slot.dtos.requests.StandardSlotRequestDTO;
import com.es.slots.slot.entities.StandardSlot;
import com.es.slots.slot.exceptions.customs.SlotNotFoundException;
import com.es.slots.slot.exceptions.customs.SlotValidityFailureException;
import com.es.slots.slot.mapper.StandardSlotMapper;
import com.es.slots.slot.repositories.StandardSlotRepository;
import com.es.slots.slot.validators.SlotValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StandardSlotService {

    private final StandardSlotRepository standardSlotRepository;
    private final StandardSlotMapper standardSlotMapper;
    private final SlotValidator slotValidator;


    /**
     * Get all StandardSlots
     * @return The list of all StandardSlots
     */
    public List<StandardSlot> getAll() {
        return standardSlotRepository.findAll();
    }


    /**
     * Get one StandardSlot by public id
     * @param slotPublicId The public id of the StandardSlot to get
     * @return The StandardSlot with the public id
     * @throws SlotNotFoundException If the StandardSlot is not found
     */
    public StandardSlot getOneSlotByPublicId(String slotPublicId) throws SlotNotFoundException {
        Optional<StandardSlot> optionalStandardSlot = standardSlotRepository.findByPublicId(slotPublicId);
        if (optionalStandardSlot.isEmpty()) {
            throw new SlotNotFoundException(slotPublicId);
        }
        return optionalStandardSlot.get();
    }


    /**
     * Create a new instance of StandardSlot with request's data
     * @param standardSlotRequestDTO Data requests
     * @return a new StandardSlot
     */
    public StandardSlot create(StandardSlotRequestDTO standardSlotRequestDTO) throws SlotValidityFailureException {
        slotValidator.checkValidityOfSlotRequest(standardSlotRequestDTO);
        StandardSlot standardSlot = standardSlotMapper.updateStandardSlotFromRequest(new StandardSlot(), standardSlotRequestDTO);
        return standardSlotRepository.save(standardSlot);
    }


    /**
     * Update a StandardSlot
     * @param slotPublicId The public id of the StandardSlot to update
     * @param standardSlotRequestDTO The data to update
     * @return The updated StandardSlot
     * @throws SlotValidityFailureException If the data are not valid
     * @throws SlotNotFoundException If the StandardSlot is not found
     */
    public StandardSlot update(String slotPublicId, StandardSlotRequestDTO standardSlotRequestDTO) throws SlotValidityFailureException, SlotNotFoundException {
        slotValidator.checkValidityOfSlotRequest(standardSlotRequestDTO);
        StandardSlot standardSlot = standardSlotMapper.updateStandardSlotFromRequest(this.getOneSlotByPublicId(slotPublicId), standardSlotRequestDTO);
        standardSlotRepository.save(standardSlot);
        return standardSlot;
    }


    /**
     * Delete a StandardSlot
     * @param publicId The public id of the StandardSlot to delete
     * @throws SlotNotFoundException If the StandardSlot is not found
     */
    public void deleteSlotByPublicId(String publicId) throws SlotNotFoundException {
        StandardSlot standardSlot = this.getOneSlotByPublicId(publicId);
        standardSlotRepository.delete(standardSlot);
    }


    /**
     * Get all StandardSlots by day
     * @param dayOfWeek The day of the StandardSlots to get
     * @return The list of StandardSlots with the day
     */
    public List<StandardSlot> getAllByDay(DayOfWeek dayOfWeek) {
        return standardSlotRepository.findAllByDayOfWeek(dayOfWeek);
    }

}
