package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dao.repository.CountryRepository;
import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import com.sipl.ticket.core.mapper.CountryMapper;
import com.sipl.ticket.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public CountryResponseDto createCountry(CountryRequestDto requestDto) {
        log.info("Creating country: {}", requestDto.getCountryName());

        if (countryRepository.existsByCountryNameIgnoreCase(requestDto.getCountryName())) {
            throw new IllegalArgumentException("Country already exists");
        }

        Country country = countryMapper.toEntity(requestDto);
        Country savedCountry = countryRepository.save(country);

        log.info("Country created with ID: {}", savedCountry.getCountryId());
        return countryMapper.toDto(savedCountry);
    }

    @Override
    public CountryResponseDto updateCountry(Long countryId, CountryRequestDto requestDto) {
        log.info("Updating country with ID: {}", countryId);

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));

        countryMapper.updateEntityFromDto(requestDto, country);
        return countryMapper.toDto(countryRepository.save(country));
    }

    @Override
    public CountryResponseDto getCountryById(Long countryId) {
        log.info("Fetching country with ID: {}", countryId);

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));

        return countryMapper.toDto(country);
    }

    @Override
    public List<CountryResponseDto> getAllCountries() {
        log.info("Fetching all countries");

        return countryRepository.findAll()
                .stream()
                .map(countryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCountry(Long countryId) {
        log.info("Deleting country with ID: {}", countryId);

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));

        countryRepository.delete(country);
    }
}
