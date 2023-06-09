package delivery.service;

import delivery.repository.StatusRepository;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private StatusRepository statusRepository;

    @Override
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    @Override
    public Status findById(int id) {
        return statusRepository.findById(id).get();
    }
}
