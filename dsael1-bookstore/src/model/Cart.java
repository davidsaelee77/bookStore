/*
 * TCSS 305 � Autumn 2018
 */

package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Shopping cart class which is used to store collection information and to
 * calculate the cost of bulk items and non bulk items.
 * 
 * @author David Saelee
 * @version 10/13/2018
 *
 */
public class Cart {
    /**
     * Field declaration.
     */
    /**
     * boolean statement to determine bulk membership.
     * 
     */
    private boolean myMember;
    /**
     * HashMap to store Item and ItemOrder information.
     * 
     */
    private Map<Item, ItemOrder> myShoppingCart;

    /**
     * Initialize empty HashMap to store Item and ItemOrder information.
     * 
     */
    public Cart() {

        myShoppingCart = new HashMap<Item, ItemOrder>();

    }

    /**
     * Adds ItemOrder into the shopping cart.
     * 
     * @param theOrder is a parameter that holds the ItemOrder information.
     * @throws NullPointerException if the parameter is a null value.
     */
    public void add(final ItemOrder theOrder) {

        if (theOrder == null) {
            throw new NullPointerException("The parameter is an invalid 'null' value");
        }
        myShoppingCart.put(theOrder.getItem(), theOrder);

    }

    /**
     * Sets the membership for bulk prices.
     * 
     * @param theMembership sets the membership for bulk prices.
     */
    public void setMembership(final boolean theMembership) {

        myMember = theMembership;
    }

    /**
     * Calculates the total for non discounted purchases and the total for bulk
     * option purchases. Uses integer division to determine bulk ratio, which is
     * then computed at bulk prices. Uses mod operator to determine remainder,
     * which is computed at regular price then returns total bulk cost.
     * <p>
     * If no bulk options are available, then price is multiplied by the
     * quantity and added to total cost.
     * 
     * @return totalCost of bulk purchases or non discounted purchases in
     *         BigDecimal format.
     */
    public BigDecimal calculateTotal() {

        BigDecimal totalCost = BigDecimal.ZERO;
        for (ItemOrder order : myShoppingCart.values()) {
            final BigDecimal itemQuantity = new BigDecimal(order.getQuantity());
            final BigDecimal thisPrice = order.getItem().getPrice();
            final BigDecimal thisBulkPrice = order.getItem().getBulkPrice();
            
            if (myMember && order.getItem().isBulk()) {
                final BigDecimal bulk = 
                                thisBulkPrice.multiply(new BigDecimal(order.getQuantity()
                                                         / order.getItem().getBulkQuantity()));
                final BigDecimal remainder = 
                                thisPrice.multiply(new BigDecimal(order.getQuantity()
                                                         % order.getItem().getBulkQuantity()));
                totalCost = totalCost.add(bulk).add(remainder);
            } else {
                totalCost = totalCost.add(thisPrice.multiply(itemQuantity));
            }
        }
        return totalCost.setScale(2, RoundingMode.HALF_EVEN);
    }
    /**
     * Clears the shopping cart.
     */
    public void clear() {
        
        myShoppingCart.clear();
    }

    /**
     * Text representation of the total cost and items added to the
     * shopping cart.
     * 
     * @return total cost and string representation of contents in shopping cart.
     */
    @Override
    public String toString() {

        return  "Total Cost: " + calculateTotal() + " My Shopping Cart: "
               + myShoppingCart.toString();
    }

}
