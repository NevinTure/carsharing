package carsharing.dao;

import carsharing.jdbc.H2JDBCConnection;
import carsharing.model.Car;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    private final H2JDBCConnection connection;

    public CarDAO(H2JDBCConnection connection) {
        this.connection = connection;
    }

    public void createCar(String carName, int companyId) {
        connection.executeUpdate(
                "insert into CAR(name, company_id) values(?, ?)", carName, companyId);
    }

    public List<Car> getAllCars() {
        ResultSet rs = connection.executeQuery("select * from CAR");
        return getCarList(rs);
    }

    public Car getCarById(int id) {
        ResultSet rs = connection.executeQuery("select * from CAR where id = ?", id);
        Car car = new Car();
        try {
            rs.next();
            car.setId(rs.getInt("id"));
            car.setName(rs.getString("name"));
            car.setCompanyId(rs.getInt("company_id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return car;
    }

    public List<Car> getCarsByCompanyId(int companyId) {
        ResultSet rs = connection.executeQuery("select * from CAR where company_id = ?", companyId);
        return getCarList(rs);
    }

    public List<Car> getFreeCarsByCompanyId(int companyId) {
        ResultSet rs;
        if(!isAnyCarRented()) {
            rs = connection.executeQuery("select car.id, car.name, car.company_id from CAR" +
                    " join CUSTOMER as cus ON car.company_id = ? and cus.rented_car_id != car.id", companyId);
            return getCarList(rs);
        } else {
            return getCarsByCompanyId(companyId);
        }
    }

    private List<Car> getCarList(ResultSet rs) {
        List<Car> carList = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int companyId = rs.getInt(3);
                carList.add(new Car(id, name, companyId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carList;
    }

    private boolean isAnyCarRented() {
        return getCarList(connection.executeQuery("select * from CUSTOMER where rented_car_id is not null")).isEmpty();

    }
}
