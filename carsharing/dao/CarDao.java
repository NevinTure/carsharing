package carsharing.dao;

import carsharing.model.Car;
import carsharing.model.Company;

import java.util.List;

public interface CarDao {

    void create(Car car);
    Car get(int id);
    void update(Car car);

    List<Car> getAll();
}
