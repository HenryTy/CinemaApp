package ty.henry.cinemaapp.service;

import oracle.jdbc.OracleDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.dto.HallForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.error.EntityNotExistException;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.persistence.HallRepository;
import ty.henry.cinemaapp.persistence.ShowingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HallService {

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private ShowingRepository showingRepository;

    public Iterable<Hall> findAllHalls() {
        return hallRepository.findAll(Sort.by("name"));
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

    public boolean deleteHall(Integer id) {
        try {
            hallRepository.deleteById(id);
        } catch (Exception ex) {
            Throwable rootCause = ex;
            while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
                rootCause = rootCause.getCause();
            }
            if(rootCause instanceof OracleDatabaseException &&
                    ((OracleDatabaseException) rootCause).getOracleErrorNumber() == 20000) {
                return false;
            }
            else {
                throw ex;
            }
        }
        return true;
    }

    public boolean hasHallFutureShowings(Hall hall) {
        List<Showing> futureShowingsInHall = showingRepository.findAllByHallAndShowingDateAfter(hall, LocalDateTime.now());
        return futureShowingsInHall.size() > 0;
    }

    private void fillHallWithFormData(Hall hall, HallForm hallForm) {
        hall.setName(hallForm.getName());
        hall.setRowCount(hallForm.getRowCount());
        hall.setSeatsInRow(hallForm.getSeatsInRow());
    }
}
