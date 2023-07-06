package carsharing.dao;

import carsharing.jdbc.H2JDBCConnection;
import carsharing.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAO {

    private final H2JDBCConnection connection;

    public CustomerDAO(H2JDBCConnection connection) {
        this.connection = connection;
    }

    public void createCustomerByName(String name) {
        connection.executeUpdate("insert into CUSTOMER(name) values(?)", name);
    }

    public Customer getCustomerById(int id) {
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
    public List<Customer> getAllCustomers() {
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

    public void setRentedCarIdByCustomerId(int customerId , int carId) {
        connection.executeUpdate(
                "update CUSTOMER set rented_car_id = ? where id = ?",
                carId,
                customerId
        );
    }

    public boolean isCarAlreadyRentedById(int id) {
        try {
            return connection.executeQuery(
                    "select * from CUSTOMER where rented_car_id = ?",
                    id).next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void returnACar(int id) {
        connection.executeUpdate("update CUSTOMER set rented_car_id = null where id = ?", id);
    }
}
