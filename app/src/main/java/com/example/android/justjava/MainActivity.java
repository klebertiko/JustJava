package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {

    private static final int COFFEEPRICE = 5;
    private static final int WHIPPEDCREAMTOPPINGPRICE = 1;
    private static final int CHOCOLATETOPPINGPRICE = 2;
    private int quantity = 0;
    private boolean hasWhippedCream = false;
    private boolean hasChocolate = false;
    private String name = "";
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the minus (-) button is clicked.
     */
    public void incrementQuantity(View view) {
        if (this.quantity >= 10) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 10 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        this.quantity = this.quantity + 1;
        this.displayQuantity(this.quantity);
    }

    /**
     * This method is called when the plus (+) button is clicked.
     */
    public void decrementQuantity(View view) {
        if (this.quantity <= 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        this.quantity = this.quantity - 1;
        this.displayQuantity(this.quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        this.name = nameEditText.getText().toString();

        EditText emailEditText = (EditText) findViewById(R.id.email_edit_text);
        this.email = emailEditText.getText().toString();

        CheckBox hasWhippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        this.hasWhippedCream = hasWhippedCreamCheckbox.isChecked();

        CheckBox hasChocolateCheckbox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        this.hasChocolate = hasChocolateCheckbox.isChecked();

        int totalPrice = this.calculatePrice();
        String orderSummary = this.createOrderSummary(totalPrice);

        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "JustJava order for " + this.name);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private int calculatePrice() {
        int basePrice = COFFEEPRICE;

        // Add 1 if the user wants whipped cream
        if (this.hasWhippedCream) {
            basePrice = basePrice + WHIPPEDCREAMTOPPINGPRICE;
        }

        // Add 2 if the user wants chocolate
        if (this.hasChocolate) {
            basePrice = basePrice + CHOCOLATETOPPINGPRICE;
        }

        // Calculate the total order price
        return this.quantity * basePrice;
    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private String createOrderSummary(int totalPrice) {

        StringBuilder orderSummary = new StringBuilder();
        orderSummary.append(super.getString(R.string.order_summary_name, this.name));
        orderSummary.append("\n");
        orderSummary.append(super.getString(R.string.order_summary_email, this.email));
        orderSummary.append("\n");

        if (this.hasWhippedCream) {
            orderSummary.append(super.getString(R.string.order_summary_whipped_cream));
            orderSummary.append(" " + super.getString(R.string.yes));
        } else {
            orderSummary.append(super.getString(R.string.order_summary_whipped_cream));
            orderSummary.append(" " + super.getString(R.string.no));
        }

        orderSummary.append("\n");

        if (this.hasWhippedCream) {
            orderSummary.append(super.getString(R.string.order_summary_chocolate));
            orderSummary.append(" " + super.getString(R.string.yes));
        } else {
            orderSummary.append(super.getString(R.string.order_summary_chocolate));
            orderSummary.append(" " + super.getString(R.string.no));
        }

        orderSummary.append("\n");
        orderSummary.append(super.getString(R.string.order_summary_quantity, this.quantity));
        orderSummary.append("\n");
        orderSummary.append(super.getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(totalPrice)));
        orderSummary.append("\n");
        orderSummary.append(super.getString(R.string.thank_you));

        Log.v("MyActivity", "MainActivity.createOrderSummary() returns " + orderSummary.toString());

        return orderSummary.toString();
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityCounterTextView = (TextView) findViewById(R.id.quantity_counter);
        quantityCounterTextView.setText("" + number);
    }
}