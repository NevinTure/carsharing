package carsharing.daoIplementation;

import carsharing.dao.CompanyDao;
import carsharing.jdbc.H2JDBCConnection;
import carsharing.model.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class companyDaoImpl implements CompanyDao {

    private final H2JDBCConnection connection;

    public companyDaoImpl(H2JDBCConnection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Company company) {
        connection.executeUpdate(
                "insert into company(name) values(?)", company.getName());
    }

    @Override
    public Company get(int id) {
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

    @Override
    public void update(Company company) {
        connection.executeUpdate("update company set name = ? where id = ?",
                company.getName(),
                company.getId());
    }

    public List<Company> getAll() {
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
