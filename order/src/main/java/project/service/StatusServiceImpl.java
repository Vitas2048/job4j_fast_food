package project.service;

import lombok.AllArgsConstructor;
import model.Status;
import org.springframework.stereotype.Service;
import project.repository.StatusRepository;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private StatusRepository statusRepository;

    @Override
    public Status checkStatus(int id) {
        return statusRepository.findById(id).get();
    }
}
