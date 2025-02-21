public class Review {
    private String customerName;
    private String reviewText;

    public Review(String customerName, String reviewText) {
        this.customerName = customerName;
        this.reviewText = reviewText;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void printReview() {
        System.out.println("Customer: " + customerName);
        System.out.println("Review: " + reviewText);
        System.out.println();
    }
}
