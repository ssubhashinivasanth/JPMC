package jp;
public class Sale {

    // public field log to store messages and product details
    public SaleLog log;

    // Adjustment of product price is handled within this object e.g. add 20p, subtract 20p, etc.
    private price adjustPrice;

    // This holds the product details
    private Product product;


    // Constructor
    public Sale() {
        log = new SaleLog();
    }

    // Process notices and appends product information to the relevant product in the
    // SaleLog.
    // @return [Boolean] false on empty string and invalid message and true on the rest.
    public boolean processNotification(String saleNotice) {

        // MessageParser helps to parse the incoming messages and obtain product sale information.
        parser messageParser;

        // Process the given message
        messageParser = new parser(saleNotice);

        // Get the product type e.g 'apple'
        String productType = messageParser.getProductType();

        // Check if product type is empty return false and do nothing.
        if (productType.isEmpty()) {
            return false;
        }

        //Returns an existing product else returns a new Product object
        this.product = log.getProduct(productType);

        // Prepare the product details for adjustment
        this.adjustPrice = new price(product);

        // Set the product details from the parsed message
        this.product.setProductQuantity(messageParser.getProductQuantity());
        this.product.setTotalQuantity(messageParser.getProductQuantity());
        this.product.setProductPrice(messageParser.getProductPrice());
        this.product.setAdjustmentOperator(messageParser.getOperatorType());

        // Set the total value of the product.
        setProductTotalPrice();

        // Set the sale log reports
        log.setNormalReports(saleNotice);

        // Update the product with the new details
        log.updateProduct(product);

        return true;
    }

    // Set or Append Total product price based on any adjustment if given.
    // Also appends the log for adjustments made.
    private void setProductTotalPrice() {
        double adjustedPrice;
        double productValue;

        if (!product.getAdjustmentOperator().isEmpty()) {
            adjustedPrice = adjustPrice.getAdjustedPrice();
            log.setAdjustmentReports(adjustPrice.adjustmentReport());
            product.setTotalPrice(adjustedPrice);
        } else {
            productValue = product.calculatePrice(product.getProductQuantity(), product.getProductPrice());
            product.appendTotalPrice(productValue);
        }
    }

}



