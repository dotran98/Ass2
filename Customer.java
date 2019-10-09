/***
 * Simulates a customer with basic operations.
 *
 * @author Do Tran
 */
public class Customer {
    // Attributes
    /***
     * Indicates ID of customer
     */
    int customerID;

    /***
     * Indicates name of customer
     */
    String customerName;

    /***
     * Indicates whether customer has seated in the waiting room
     */
    private boolean seated = false;

    /***
     * Creates an Customer simulation with ID and name
     * @param customerID ID of customer
     * @param customerName name of customer
     */
    public Customer(int customerID, String customerName){
        this.customerID = customerID;
        this.customerName = customerName;
    }

    /***
     * Creates an Customer simulation with ID and default name
     * @param customerID
     */
    public Customer(int customerID){
        this.customerID = customerID;
        this.customerName = "N/A";
    }

    /***
     * Retrieves ID of customer
     * @return ID of customer
     */
    public int getCustomerID() {
        return customerID;
    }

    /***
     * Sets ID for customer
     * @param customerID new ID for customer
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /***
     * Retrieves name of customer
     * @return name of customer
     */
    public String getCustomerName() {
        return customerName;
    }

    /***
     * Sets new name for customer
     * @param customerName new name for customer
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /***
     * Queries if customer has been seated
     * @return  true if customer is sitting in the waiting room
     * otherwise, false
     */
    public boolean isSeated() {
        return seated;
    }

    /***
     * Set the status of being seated for a customer
     * @param seated new status
     */
    public void setSeated(boolean seated) {
        this.seated = seated;
    }

    /***
     * Simulates cutting hair process
     * @param s the salon cutting hair for customer
     */
    public void getHaircut(Salon s){
        s.serveCustomer(this);
    }
}
