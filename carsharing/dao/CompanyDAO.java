package carsharing.dao;

import carsharing.jdbc.H2JDBCConnection;
import carsharing.model.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {

    private final H2JDBCConnection connection;

    public CompanyDAO(H2JDBCConnection connection) {
        this.connection = connection;
    }

    public void createCompanyByName(String companyName) {
        connection.executeUpdate(
                "insert into company(name) values(?)", companyName);
    }
    public Company getCompanyById(int id) {
        ResultSet rs = connection.executeQuery("select * from COMPANY where id = ?", id);
        Company company = new Company();
        try {
            rs.next();
            company.setId(rs.getInt("id"));
            company.setName(rs.getString("name"));
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return company;
    }

    public List<Company> getAllCompanies() {
        List<Company> companyList = new ArrayList<>();
        ResultSet rs = connection.executeQuery("select * from COMPANY");
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                companyList.add(new Company(id, name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return companyList;
    }
}
