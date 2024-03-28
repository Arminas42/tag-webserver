package com.kuaprojects.rental.Rent;

import com.kuaprojects.rental.Trailer.TrailerNotFoundException;
import com.kuaprojects.rental.Trailer.Trailer;
import com.kuaprojects.rental.Trailer.TrailerRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final TrailerRepository trailerRepository;

    public RentServiceImpl(RentRepository rentRepository, TrailerRepository trailerRepository) {
        this.rentRepository = rentRepository;
        this.trailerRepository = trailerRepository;
    }


    @Override
    public Rent createRent(Long trailerId, String driverCarLicensePlate, String driverFullName) {
        Trailer trailer = trailerRepository.findById(trailerId)
                .orElseThrow(() -> new TrailerNotFoundException(trailerId));
        Rent rent =
            Rent.builder()
                .driverCarLicensePlate(driverCarLicensePlate)
                .driverFullName(driverFullName)
                .rentedTrailer(trailer)
                .rentStartTimestamp(LocalDateTime.now())
                .status(RentStatus.IN_PROCESS)
                .build();

        return rentRepository.save(rent);
    }

    @Override
    public List<Rent> getAllRent() {
        return rentRepository.findAll();
    }

    @Override
    public Rent findRent(Long id) {
        return rentRepository.findById(id)
                .orElseThrow(() -> new RentNotFoundException(id));
    }

    @Override
    public Rent endRent(Long id) {
        var rentToUpdate = rentRepository.findById(id).orElseThrow(() -> new RentNotFoundException(id));
        rentToUpdate.setRentEndTimestamp(LocalDateTime.now());
        rentToUpdate.setStatus(RentStatus.ENDED);
        return rentRepository.save(rentToUpdate);
    }
}
