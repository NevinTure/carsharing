package carsharing;

import carsharing.daoIplementation.carDaoImpl;
import carsharing.daoIplementation.companyDaoImpl;
import carsharing.daoIplementation.customerDaoImpl;
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

        companyDaoImpl companyDaoImpl = new companyDaoImpl(connection);
        carDaoImpl carDaoImpl = new carDaoImpl(connection);
        customerDaoImpl customerDaoImpl = new customerDaoImpl(connection);
        Menu menu = new Menu(companyDaoImpl, carDaoImpl, customerDaoImpl);
        menu.startMainMenu();

    }
}