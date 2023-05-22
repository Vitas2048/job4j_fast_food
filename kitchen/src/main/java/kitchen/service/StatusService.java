package kitchen.service;

import model.*;

import java.util.List;

public interface StatusService {

    List<Status> getAll();

    Status findById(int id);
}
