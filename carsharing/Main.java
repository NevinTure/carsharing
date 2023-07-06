package carsharing;

import carsharing.dao.CarDAO;
import carsharing.dao.CompanyDAO;
import carsharing.dao.CustomerDAO;
import carsharing.jdbc.H2JDBCConnection;
import carsharing.view.Menu;


public class Main {

    public static void main(String[] args) {
        H2JDBCConnection connection = new H2JDBCConnection();
        connection.executeUpdate(
                "create table if not exists COMPANY( " +
                        "id int primary key auto_increment," +
                        "name varchar unique not null);"
        );
        connection.executeUpdate(
                "create table if not exists CAR( " +
                "id int primary key auto_increment," +
                "name varchar unique not null," +
                "company_id int not null," +
                "foreign key (company_id) references COMPANY(id)" +
                "on delete cascade on update cascade);"
        );
        connection.executeUpdate(
                "create table if not exists CUSTOMER(" +
                "id int primary key auto_increment," +
                "name varchar unique not null," +
                "rented_car_id int default null," +
                "foreign key (rented_car_id) references CAR(id)" +
                "on delete set null on update cascade);"
        );

        CompanyDAO companyDAO = new CompanyDAO(connection);
        CarDAO carDAO = new CarDAO(connection);
        CustomerDAO customerDAO = new CustomerDAO(connection);
        Menu menu = new Menu(companyDAO, carDAO, customerDAO);
        menu.startMainMenu();

    }
}