package kitchen.service;

import model.Status;

import java.util.List;

public interface StatusService {

    List<Status> getAll();

    Status findById(int id);
}
