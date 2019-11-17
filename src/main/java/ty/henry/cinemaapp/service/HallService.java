package ty.henry.cinemaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.dto.HallForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.error.EntityNotExistException;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.persistence.HallRepository;

@Service
public class HallService {

    @Autowired
    private HallRepository hallRepository;

    public Iterable<Hall> findAllHalls() {
        return hallRepository.findAll();
    }

    public Hall findHallById(Integer id) throws EntityNotExistException {
        return hallRepository.findById(id).orElseThrow(EntityNotExistException::new);
    }

    public Hall addNewHall(HallForm hallForm) throws EntityAlreadyExistsException {
        if(hallRepository.existsByName(hallForm.getName())) {
            throw new EntityAlreadyExistsException();
        }

        Hall hall = new Hall();
        fillHallWithFormData(hall, hallForm);
        return hallRepository.save(hall);
    }

    public Hall editHall(Integer id, HallForm hallForm) throws EntityNotExistException,
            EntityAlreadyExistsException {
        Hall hall = hallRepository.findById(id).orElseThrow(EntityNotExistException::new);
        if(hallRepository.existsByNameAndIdNot(hallForm.getName(), id)) {
            throw new EntityAlreadyExistsException();
        }
        fillHallWithFormData(hall, hallForm);
        return hallRepository.save(hall);
    }

    public void deleteHall(Integer id) {
        hallRepository.deleteById(id);
    }

    private void fillHallWithFormData(Hall hall, HallForm hallForm) {
        hall.setName(hallForm.getName());
        hall.setRowCount(hallForm.getRowCount());
        hall.setSeatsInRow(hallForm.getSeatsInRow());
    }
}
