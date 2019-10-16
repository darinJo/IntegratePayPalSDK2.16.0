package com.example.testpaypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView txtId,txtStatus,txtAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId=(TextView)findViewById(R.id.txtId);
        txtAmount=(TextView)findViewById(R.id.txtAmount);
        txtStatus=(TextView)findViewById(R.id.txtStatus);

        //get Intent
        Intent intent= getIntent();
        try {
            JSONObject jsonObject= new JSONObject(intent.getStringExtra("PaymentDetalis"));
            showDetails(jsonObject.getJSONObject("RESPONSE"),intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtAmount.setText("$"+paymentAmount);
            txtStatus.setText(response.getString("status"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
