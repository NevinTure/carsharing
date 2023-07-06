package carsharing.view;

import carsharing.dao.CarDAO;
import carsharing.dao.CompanyDAO;
import carsharing.dao.CustomerDAO;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private final CompanyDAO companyDAO;
    private final CarDAO carDAO;
    private final CustomerDAO customerDAO;
    private final Scanner scanner;


    public Menu(CompanyDAO companyDAO, CarDAO carDAO, CustomerDAO customerDAO) {
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
        scanner = new Scanner(System.in);
    }

    public void startMainMenu() {
        String mainMenuOption = "";
        while(!mainMenuOption.equals("0")) {
            System.out.println("""
                    1. Log in as a manager
                    2. Log in as a customer
                    3. Create a customer
                    0. Exit""");
            mainMenuOption = scanner.nextLine();
            System.out.println();
            switch (mainMenuOption) {
                case "1" -> startManagerMenu();
                case "2" -> showCustomerList();
                case "3" -> showCreateCustomer();
            }
        }
        scanner.close();
    }

    private void startManagerMenu() {
        String managerMenuOption = "";
        while (!managerMenuOption.equals("0")) {
            System.out.println("""
                    1. Company list
                    2. Create a company
                    0. Back""");
            managerMenuOption = scanner.nextLine();
            System.out.println();
            switch (managerMenuOption) {
                case "1" -> chooseCompanyToOpen();
                case "2" -> showCreateCompany();
            }
        }
    }

    private void showCreateCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerDAO.createCustomerByName(name);
        System.out.println("The customer was added!\n");
    }

    private void chooseCompanyToOpen() {
        List<Company> companyList = companyDAO.getAllCompanies();
        int companyId = showCompanyList(companyList);
        if(companyId == 0) {
            return;
        }
        startCompanyMenu(companyList.get(companyId - 1));
    }

    private int showCompanyList(List<Company> companyList) {
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!\n");
            return 0;
        }
        System.out.println("Choose a company:");
        companyList.forEach(v -> System.out.printf("%d. %s%n", v.getId(), v.getName()));
        System.out.println("0. Back");
        int companyId = Integer.parseInt(scanner.nextLine());
        System.out.println();
        return companyId;
    }

    private void showCreateCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        companyDAO.createCompanyByName(name);
        System.out.println("The company was created!\n");
    }

    private void startCompanyMenu(Company company) {
        String companyMenuOption = "";
        System.out.printf("'%s' company:%n", company.getName());
        while(!companyMenuOption.equals("0")) {
            System.out.println("""
                    1. Car list
                    2. Create a car
                    0. Back""");
            companyMenuOption = scanner.nextLine();
            System.out.println();
            switch (companyMenuOption) {
                case "1" -> showCarList(
                    carDAO.getCarsByCompanyId(company.getId()),
                    "Car list:"
                );
                case "2" -> showCreateCar(company.getId());
            }
        }
    }

    private void showCarList(List<Car> carList, String label) {
        if(carList.isEmpty()) {
            System.out.println("The car list is empty!\n");
            return;
        }
        System.out.println(label);
        for(int i = 0; i < carList.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, carList.get(i).getName());
        }
    }

    private void showCreateCar(int companyId) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carDAO.createCar(carName, companyId);
        System.out.println("The car was added!\n");
    }

    private void showCustomerList() {
        List<Customer> customerList = customerDAO.getAllCustomers();
        if(customerList.isEmpty()) {
            System.out.println("The customer list is empty!\n");
            return;
        }
        System.out.println("Customer list:");
        customerList.forEach(v -> System.out.printf("%d. %s%n", v.getId(), v.getName()));
        System.out.println("0. Back");
        int customerId = Integer.parseInt(scanner.nextLine());
        System.out.println();
        if(customerId == 0) {
            return;
        }
        startCustomerMenu(customerId);
    }

    private void startCustomerMenu(int id) {
        Customer customer;
        String customerMenuOption = "";
        while(!customerMenuOption.equals("0")) {
            customer = customerDAO.getCustomerById(id);
            System.out.println("""
                    1. Rent a car
                    2. Return a rented car
                    3. My rented car
                    0. Back""");
            customerMenuOption = scanner.nextLine();
            System.out.println();
            switch (customerMenuOption) {
                case "1" -> rentACar(customer);
                case "2" -> returnACar(customer);
                case "3" -> showCustomerCar(customer);
            }
        }
    }

    private void rentACar(Customer customer) {
        if(customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!\n");
            return;
        }
        List<Company> companyList = companyDAO.getAllCompanies();
        int companyId = showCompanyList(companyList);
        if(companyId == 0) {
            return;
        }
        List<Car> carList = carDAO.getFreeCarsByCompanyId(companyId);
        if(carList.isEmpty()) {
            System.out.printf("No available cars in the '%s' company%n%n",
                    companyList.get(companyId - 1).getName());
            return;
        }
        showCarList(carList, "Choose a car:");
        System.out.println("0. Back");
        int index = Integer.parseInt(scanner.nextLine());
        System.out.println();
        if(index == 0) {
            return;
        }
        customerDAO.setRentedCarIdByCustomerId(customer.getId(), carList.get(index - 1).getId());
        System.out.printf("You rented '%s'%n%n", carList.get(index - 1).getName());
    }

    private void returnACar(Customer customer) {
        if(customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }
        customerDAO.returnACar(customer.getRentedCarId());
        System.out.println("You've returned a rented car!\n");
    }

    private void showCustomerCar(Customer customer) {
        if(customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!\n");
            return;
        }
        Car car = carDAO.getCarById(customer.getRentedCarId());
        Company company = companyDAO.getCompanyById(car.getCompanyId());
        System.out.println("Your rented car:");
        System.out.println(car.getName());
        System.out.println("Company:");
        System.out.println(company.getName());
        System.out.println();
    }
}
