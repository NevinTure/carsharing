package carsharing.daoIplementation;

import carsharing.dao.CustomerDao;
import carsharing.jdbc.H2JDBCConnection;
import carsharing.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class customerDaoImpl implements CustomerDao {

    private final H2JDBCConnection connection;

    public customerDaoImpl(H2JDBCConnection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Customer customer) {
        connection.executeUpdate("insert into CUSTOMER(name) values(?)", customer.getName());
    }

    @Override
    public Customer get(int id) {
        ResultSet rs = connection.executeQuery("SELECT * FROM CUSTOMER WHERE id = ?", id);
        Customer customer = new Customer();
        try {
            rs.next();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setRentedCarId(rs.getInt("rented_car_id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public List<Customer> getAll() {
        ResultSet rs = connection.executeQuery("select * from CUSTOMER");
        List<Customer> customerList = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Optional<Object> temp = Optional.ofNullable(rs.getObject("rented_car_id"));
                Integer rentedCarId = (Integer) temp.orElse(0);
                customerList.add(new Customer(id, name, rentedCarId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    @Override
    public void update(Customer customer) {
        connection.executeUpdate("update CUSTOMER set name = ?, rented_car_id = ? where id = ?",
                customer.getName(),
                customer.getRentedCarId(),
                customer.getId());
    }
}
