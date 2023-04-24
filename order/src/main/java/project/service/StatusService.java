package project.service;

import model.*;

import java.util.List;

public interface StatusService {

    Status getStatusById(int id);

    List<Status> getAllStatuses();

}
