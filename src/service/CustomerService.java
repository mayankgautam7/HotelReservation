package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;

public class CustomerService {
    private static CustomerService reference  = new CustomerService();
    private HashMap<String, Customer> customerMap = new HashMap<>();
    public static CustomerService getInstance()
    {
        return reference;
    }
    public void addCustomer(String firstName, String lastName, String email)
    {
        Customer customer;
        try{
            customer = new Customer(firstName, lastName, email);
            if(customerMap.containsKey(email))
            {
                throw new IllegalArgumentException("Account already exists, enter a different email");
            }
            customerMap.put(email, customer);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
    public Customer getCustomer(String customerEmail)
    {
        if(customerMap == null || !customerMap.containsKey(customerEmail)) {
            throw new IllegalArgumentException("This customer does not exist, Please try again or Create a new Account");
        }
        Customer customer = customerMap.get(customerEmail);

        return customer;
    }
    public Collection<Customer> getAllCustomers()
    {
        Collection<Customer> customers = customerMap.values();
        return customers;
    }
}
