/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */
package com.mikemcgrain.justjava;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int quantity = 1;

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot order more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity += 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You cannot order less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        EditText orderNameTextView = (EditText) findViewById(R.id.order_name_text_view);
        String orderName = orderNameTextView.getText().toString();

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, orderName);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, orderName));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @return calulated price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int basePrice = 5;

        if (hasWhippedCream) {
            basePrice += 1;
        }

        if (hasChocolate) {
            basePrice += 2;
        }

        return quantity * basePrice;
    }

    /**
     * @param price
     * @return an order summary
     */
    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String orderName) {
        return getString(R.string.order_summary_name, orderName) +
                "\n" + getString(R.string.order_summary_quantity, quantity) +
                "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream) +
                "\n" + getString(R.string.order_summary_chocolate, hasChocolate) +
                "\n" + getString(R.string.order_summary_total, NumberFormat.getCurrencyInstance().format(price)) +
                "\n" + getString(R.string.thank_you);
    }

}