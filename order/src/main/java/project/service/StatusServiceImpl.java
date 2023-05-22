package project.service;

import lombok.AllArgsConstructor;
import model.*;
import org.springframework.stereotype.Service;
import project.repository.StatusRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private StatusRepository statusRepository;

    @Override
    public Status getStatusById(int id) {
        return statusRepository.findById(id).get();
    }

    @Override
    public List<Status> getAllStatuses() {
       return statusRepository.findAll();
    }
}
