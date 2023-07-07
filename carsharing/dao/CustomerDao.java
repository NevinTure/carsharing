package carsharing.dao;

import carsharing.model.Customer;

import java.util.List;

public interface CustomerDao {

    void create(Customer customer);
    Customer get(int id);
    void update(Customer customer);

    List<Customer> getAll();
}
