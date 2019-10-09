import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/***
 * Simulates the operations of a Salon
 *
 * @author Do Tran
 */
public class Salon {
    //Attributes
    /***
     * Indicates name of the salon
     */
    String name;

    /***
     * Indicates a number of seats in the salon
     * This is a resource shared among threads
     */
    private static int SEATS_IN_WAITING_AREA = 5;

    /***
     * Indicates a number of running threads
     */
    //private static int NO_OF_HAIRDRESSORS = 3;
    private static int NO_OF_HAIRDRESSORS = 10; // for test 2 of task 6

    /***
     * Creates a means to control the number of running threads
     */
    //private static Semaphore hairdressors = new Semaphore(NO_OF_HAIRDRESSORS);
    private static Semaphore1 hairdressors = new Semaphore1(NO_OF_HAIRDRESSORS);

    /***
     * Creates a means to control access to shared resources
     */
    private static ReentrantLock seatLock = new ReentrantLock();

    /***
     * Creates a simulation of a salon
     * @param name name of salon
     */
    public Salon(String name) {
        this.name = name;
    }

    /***
     * Retrieves name of salon
     * @return name of salon
     */
    public String getName() {
        return name;
    }

    /***
     * Sets new name for the salon
     * @param name new name for the salon
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Simulates a service for customer
     * @param customer customer being served
     */
    public void serveCustomer(Customer customer){
        System.out.println("Customer " + customer.getCustomerID() + " is entering " + getName());
        giveHaircut(customer);
    }

    /***
     * Simulates hair cutting process
     * @param customer customer being served
     */
    private void giveHaircut(Customer customer){
        System.out.println(getName() + " is serving customer " + customer.getCustomerID());
        try {
            Thread.sleep(2000); // time for cutting hair
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getName() + " is finished serving the customer "
                + customer.getCustomerID());
    }

    /***
     * Simulates a service for customer
     * This methods involves controlling running threads,
     * hence, implementing synchronization ensuring methods (Semaphore, ReentrantLock)
     * @param customer customer being served
     */
    public void serveCustomerSync(Customer customer) {
        seatLock.lock(); // get the exclusive access to modify shared resource
        System.out.println("Customer " + customer.getCustomerID()
                + " is entering " + getName());
        try {
            if (SEATS_IN_WAITING_AREA > 0) { //seat available
                SEATS_IN_WAITING_AREA--;
                System.out.println("Customer " + customer.getCustomerID()
                        + " is in the waiting area");
                customer.setSeated(true);
            } else { // no seat available
                System.out.println("Customer " + customer.getCustomerID()
                        + " is leaving"); //customer leaves
                Thread.interrupted(); // stop the threat
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            seatLock.unlock();
        }

        //leaving customers cannot get haircut
        if(customer.isSeated()) {
            try {
                hairdressors.acquire(); //get 1 of 3 permission to get haircut
                seatLock.lock();
                SEATS_IN_WAITING_AREA++;
                seatLock.unlock();
                giveHaircut(customer);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } finally {
                hairdressors.release();
            }
        }
    }
}
