/***
 * Simulates service to customer
 *
 * @author Do Tran
 */
public class CustomerService extends Thread {
    /***
     * Simulates customers receiving service
     */
    Customer customer;

    /***
     * Simulates salon offering service
     */
    Salon salon;

    /***
     * Retrieving customer receiving service
     * @return customer object
     */
    public Customer getCustomer() {
        return customer;
    }

    /***
     * Sets new customer
     * @param customer new customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /***
     * Retrieving salon offering service
     * @return
     */
    public Salon getSalon() {
        return salon;
    }

    /***
     * Sets new salon
     * @param salon new salon
     */
    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    /***
     * Creates a simulation of service
     * @param customer customer receiving service
     * @param salon salon offering service
     */
    public CustomerService(Customer customer, Salon salon){
        this.customer = customer;
        this.salon = salon;
    }

    /***
     * Creates a simulation of service
     * @param customer customer receiving service
     */
    public CustomerService(Customer customer){
        this.customer = customer;
        this.salon = new Salon("Salon-A");
    }

    @Override
    public void run(){
        //salon.serveCustomer(customer);
        salon.serveCustomerSync(customer);
    }
}
