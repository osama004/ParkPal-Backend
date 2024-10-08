package at.technikum.parkpalbackend.controller;

import at.technikum.parkpalbackend.dto.CountryDto;
import at.technikum.parkpalbackend.mapper.CountryMapper;
import at.technikum.parkpalbackend.model.Country;
import at.technikum.parkpalbackend.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@CrossOrigin
public class CountryController {
    private final CountryService countryService;

    private final CountryMapper countryMapper;

    public CountryController(CountryService countryService, CountryMapper countryMapper) {
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CountryDto addCountries(@RequestBody @Valid CountryDto countryDto){
        Country country = countryMapper.toEntity(countryDto);
        country = countryService.save(country);
        return countryMapper.toDto(country);
    }

    @GetMapping()
    public List<CountryDto> getAllCountries() {
        List<Country> allCountries = countryService.findAllCountries();
        return allCountries.stream().map(country -> countryMapper.toDto(country)).toList();
    }

    @GetMapping("/{countryId}")
    public CountryDto getCountryById(@PathVariable @Valid String countryId){
        Country country = countryService.findCountryByCountryId(countryId);
        return countryMapper.toDto(country);
    }

    @PutMapping("/{countryId}")
    public CountryDto updateCountry(@PathVariable String countryId,
                                    @RequestBody @Valid CountryDto updatedCountryDto){
        Country updatedCountry = countryMapper.toEntity(updatedCountryDto);
        updatedCountry = countryService.updateCountry(countryId, updatedCountry);
        return countryMapper.toDto(updatedCountry);
    }

    @DeleteMapping("/{countryId}")
    @ResponseStatus(HttpStatus.OK)
    public CountryDto deleteCountryByCountryId(@PathVariable @Valid String countryId){
        countryService.deleteCountryByCountryId(countryId);
        return null;
    }
}
