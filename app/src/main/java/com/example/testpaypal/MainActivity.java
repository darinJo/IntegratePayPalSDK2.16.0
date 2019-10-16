package com.example.testpaypal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.example.testpaypal.Config.Config;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONException;

import java.math.BigDecimal;

import static com.example.testpaypal.Config.Config.PAYMENT_CLIENT_ID;

public class MainActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE=7171;

private static PayPalConfiguration config= new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
.clientId(PAYMENT_CLIENT_ID);// envaironment is sanbox because we're on test and we don't want to transfer real money
    Button btPayNow;
    EditText edtAmount;
    String amount="";

   @Override
  protected void onDestroy() {
      stopService(new Intent(this,PayPalService.class));
       super.onDestroy();
   }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //START PayPal Service
        Intent intent= new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        btPayNow=(Button)findViewById(R.id.btPayNow);
        edtAmount=(EditText)findViewById(R.id.edtAccont);

        btPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {// to call payment activity to take the entered money 
        amount=edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD","denote for ..",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent= new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);//used #4 to check the request code digits 
                        startActivity(new Intent(this, PaymentDetails.class)
						.putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
		
            else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();


        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) 
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
	
    }

     
}