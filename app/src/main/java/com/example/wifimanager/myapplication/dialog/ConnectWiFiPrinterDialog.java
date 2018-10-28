package com.example.wifimanager.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifimanager.myapplication.R;
import com.example.wifimanager.myapplication.manager.ConnectWiFiPrinterManager;

public class ConnectWiFiPrinterDialog extends Dialog {

    private EditText ip_text, port_text;
    private TextView connect_cancel_text, connect_determine_text;
    private Context mContext;

    public ConnectWiFiPrinterDialog(Context context) {
        super(context);
        mContext = context;
        initDialog(context);
    }

    public ConnectWiFiPrinterDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initDialog(context);
    }


    private void initDialog(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_connect_printer, null);
        addContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        setContentView(layout);
        ip_text = findViewById(R.id.connect_ip_text);
        port_text = findViewById(R.id.connect_port_text);
        connect_cancel_text = findViewById(R.id.connect_cancel_text);
        connect_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        connect_determine_text = findViewById(R.id.connect_determine_text);
        connect_determine_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(getTextByConnectIp())) {
                    Toast.makeText(mContext, "Please enter the ip address first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(getTextByConnectPort())) {
                    Toast.makeText(mContext, "Please enter the port number first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext, "connecting", Toast.LENGTH_SHORT).show();
                dismiss();
                connectWiFiPrinter(getTextByConnectIp(), Integer.parseInt(getTextByConnectPort()), context);
            }
        });
    }

    public void determine() {

    }

    public String getTextByConnectIp() {
        return ip_text.getText().toString();
    }

    public String getTextByConnectPort() {
        return port_text.getText().toString();
    }

    public void connectWiFiPrinter(final String ip, final int port, final Context context) {

        ConnectWiFiPrinterManager.getInstance(mContext).connectWiFiPrinter(ip, port);
    }
}
