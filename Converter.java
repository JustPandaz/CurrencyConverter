package currencyconverter.panda.org;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class Converter {

    JFrame frame;
    static JComboBox from, to;
    static JTextField amount, result;
    static JButton convert;
    static JLabel resultText, amountText, text, fromText, toText;

    Converter() {

        frame = new JFrame("CurrencyConverter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        Conversion();

        frame.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    void Conversion() {

        String[] list = {"Select","AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL","BSD","BTC","BTN","BWP","BYN","BYR","BZD","CAD","CDF","CHF","CLF","CLP","CNY","COP","CRC","CUC","CUP","CVE","CZK","DJF","DKK","DOP","DZD","EGP","ERN","ETB","EUR","FJD","FKP","GBP","GEL","GGP","GHS","GIP","GMD","GNF","GTX","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS","IMP","INR","IQD","IRR","ISK","JEP","JMD","JOD","JPY","KES","KGS","KHR","KMF","KPW","KRW","KWD","KYD","KZT","LAK","LBP","LKR","LRD","LSL","LTL","LVL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRO","MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK","SGD","SHP","SLL","SOS","SRD","STD","SVC","SYP","SZL","THB","TJS","TMT","TND","TOP","TRY","TTD","TWD","TZS","UAH","UGX","USD","UYU","UZS","VEF","VND","VUV","WST","XAF","XAG","XAU","XCD","XDR","XOF","XPF","YER","ZAR","ZMK","ZMW","ZWL"};

        text = new JLabel("Chose the currencies you want to convert!");
        text.setBounds(150, 10, 315, 75);

        fromText = new JLabel("From");
        fromText.setBounds(90, 52, 100, 75);

        from = new JComboBox(list);
        from.setBounds(125, 80, 100, 20);

        to = new JComboBox(list);
        to.setBounds(350, 80, 100, 20);

        toText = new JLabel("To");
        toText.setBounds(330, 52, 100, 75);

        amount = new JTextField();
        amount.setBounds(125, 115, 100, 21);

        amountText = new JLabel("Amount:");
        amountText.setBounds(75, 115, 100, 21);

        result = new JTextField();
        result.setBounds(350, 115, 100, 21);
        result.setEditable(false);

        resultText = new JLabel("Result:");
        resultText.setBounds(305, 115, 100, 21);

        convert = new JButton("Convert");
        convert.setBounds(238, 80, 85, 20);
        convert.addActionListener(e1 -> {

            Object baseCurrency = from.getItemAt(from.getSelectedIndex());
            Object outputCurrency = to.getItemAt(to.getSelectedIndex());

            String baseC = String.valueOf(baseCurrency);
            String outputC = String.valueOf(outputCurrency);

            String input = amount.getText();
            int inputF = Integer. parseInt(input);

            try {
                sendHttpGETRequest(baseC, outputC, inputF);
            } catch (IOException e) {

                e.printStackTrace();
            }
        });

        frame.add(resultText);
        frame.add(result);
        frame.add(amountText);
        frame.add(amount);
        frame.add(convert);
        frame.add(toText);
        frame.add(fromText);
        frame.add(to);
        frame.add(from);
        frame.add(text);
    }

    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException {

        DecimalFormat f = new DecimalFormat("00.00");

        String GET_URL = "http://data.fixer.io/api/latest?access_key=a6e7c902a3ea7d3d84ac219c16fdfecf&format=1";
        URL url = new URL(GET_URL);

        HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
        httpURLconnection.setRequestMethod("GET");

        int responseCode = httpURLconnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLconnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while((inputLine = in.readLine()) != null) {

                response.append(inputLine);
            }

            in.close();

            JSONObject obj = new JSONObject(response.toString());
            double exchangeRate = obj.getJSONObject("rates").getDouble(fromCode);
            double exchangeRate2 = obj.getJSONObject("rates").getDouble(toCode);

            double exchangeRates, conversionRate;

            if(toCode.equals("EUR")) {

                conversionRate = 1 / exchangeRate;
            } else {

                exchangeRates = (exchangeRate / exchangeRate2);
                conversionRate = 1 / exchangeRates;
            }

            String calc = f.format(conversionRate * amount);

            System.out.println("The current exchange rate is: " + conversionRate);
            System.out.println(amount + fromCode + " = " + calc + toCode);

            result.setText(calc);
        } else {

            System.out.println("GET request failed!");
        }
    }
}
