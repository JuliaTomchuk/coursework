package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.dto.destination.DestinationDetailsDto;
import org.tms.travel_agency.dto.destination.DestinationLightDto;
import org.tms.travel_agency.exception.DuplicateDestinationException;
import org.tms.travel_agency.exception.NoSuchDestinationException;
import org.tms.travel_agency.mapper.DestinationMapper;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.DestinationService;
import org.tms.travel_agency.validator.impl.DestinationValidator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository repository;
    private final DestinationMapper mapper;
    private final DestinationValidator validator;

    @Override
    @Transactional
    public DestinationDetailsDto save(DestinationDetailsDto destinationDetailsDto) {
        if(!validator.isUnique(destinationDetailsDto)){
            throw new DuplicateDestinationException("Destination with name " + destinationDetailsDto.getName()+" already exist");
        }
        Destination destination = mapper.convert(destinationDetailsDto);
        Destination save = repository.save(destination);
        return mapper.convert(save);
    }

    @Override
    public List<DestinationLightDto> getAll() {
        List<Destination> all = repository.findAll();
        return mapper.convert(all);
    }

    @Override
    public DestinationDetailsDto getById(UUID id) {
        Destination destination = repository.findById(id).orElseThrow(()->new NoSuchDestinationException("No destination with id: " +id));
        return mapper.convert(destination);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresentOrElse((destination)->repository.delete(destination),()-> new NoSuchDestinationException("no destination with id " + id));
    }

    @Override
    @Transactional
    public DestinationDetailsDto update(DestinationDetailsDto destinationDetailsDto) {
        Destination destination = repository.findByNameIgnoreCase(destinationDetailsDto.getName()).orElseThrow(() -> new NoSuchDestinationException("no destination with name:" + destinationDetailsDto.getName()));
        Destination updated = mapper.update(destinationDetailsDto, destination);
        Destination saved = repository.save(updated);
        return mapper.convert(saved);
    }



}
