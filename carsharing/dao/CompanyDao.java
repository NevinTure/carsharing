package carsharing.dao;

import carsharing.model.Company;

import java.util.List;

public interface CompanyDao {

    void create(Company company);
    Company get(int id);
    void update(Company company);

    List<Company> getAll();
}
