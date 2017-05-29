package pl.krakow.uek.project_android;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Kantors extends AppCompatActivity{
    Intent intent, intent_Dotus, intent_Grosz, intent_Kantory2, intent_Lodzinscy, intent_Meritum, intent_Vabanque, intent_Baksy;
    String input_currency, input_amount, input_direction;
    TextView name0, rate0, aval0, name1, rate1, aval1, name2, rate2, aval2, name3, rate3, aval3, name4, rate4, aval4, name5, rate5, aval5, name6, rate6, aval6;
    Button info0, info1, info2, info3, info4, info5, info6, registerBest;
    String rateJsonResult;
    Intent intentGraph;
    public void repaint(TextView textView, Boolean check){
        if(check){
            textView.setBackgroundColor(Color.parseColor("#49ff00"));;
        }else{
            textView.setBackgroundColor(Color.parseColor("#ff4000"));;
        }

    }
    class Kantor{
        String name;
        String adress;
        String timeOpen;
        String timeClose;
        String coordinates;
        String rate = "0";
        String url;
        double temp_rate;
        View.OnClickListener onClickListener;

        public Kantor(String name, View.OnClickListener onClickListener, String adress, String timeOpen, String timeClose, String coordinates, String url){
            this.name = name;
            this.url = url;
            this.adress = adress;
            this.coordinates = coordinates;
            this.timeClose = timeClose;
            this.timeOpen = timeOpen;
            this.onClickListener = onClickListener;
        }
        public void setRate(String rate){
            this.rate = ""+Double.parseDouble(rate);
            this.temp_rate = Double.parseDouble(rate);
        }
        public String getRate(){
            return rate;
        }
        public String checkAval(TextView textView) {
            try {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                String currentTime = ""+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                Date close = new SimpleDateFormat("HH:mm").parse(timeClose);
                Date open = new SimpleDateFormat("HH:mm").parse(timeOpen);
                Date current = new SimpleDateFormat("HH:mm").parse(currentTime);
                if (close.after(current) && open.before(current)) {
                    repaint(textView, true);
                    return "Yes";
                }else {
                    repaint(textView, false);
                    return "No";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "No";
        }
        public String calculate(){
            Double result_temp;
            result_temp = Double.parseDouble(input_amount)*Double.parseDouble(rate);
            DecimalFormat df = new DecimalFormat("#.##");
            String result = df.format(result_temp);
            return result;
        }

    }


    public String getInputCurrency(){
        return input_currency;
    }
    public String getInputDirection(){
        return input_direction;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kantors_activity);
        intent = new Intent(this, Kantors.class);
        intent_Dotus = new Intent(this, info_dotus.class);
        intent_Grosz = new Intent(this, info_dotus.class);
                intent_Kantory2 = new Intent(this, info_dotus.class);
                intent_Lodzinscy = new Intent(this, info_dotus.class);
                intent_Meritum = new Intent(this, info_dotus.class);
                intent_Vabanque = new Intent(this, info_dotus.class);
                intent_Baksy = new Intent(this, info_dotus.class);




        intent = getIntent();
        input_currency = intent.getStringExtra("currency");
        input_amount = intent.getStringExtra("amount");
        input_direction = intent.getStringExtra("direction");

        intentGraph = new Intent(this,Graph.class);
        intentGraph.putExtra("currency",input_currency);
        intentGraph.putExtra("direction",input_direction);


        registerBest = (Button)findViewById(R.id.register);

        name0 = (TextView) findViewById(R.id.name0);
        rate0 = (TextView) findViewById(R.id.rate0);
        aval0 = (TextView) findViewById(R.id.aval0);
        name1 = (TextView) findViewById(R.id.name1);
        rate1 = (TextView) findViewById(R.id.rate1);
        aval1 = (TextView) findViewById(R.id.aval1);
        name2 = (TextView) findViewById(R.id.name2);
        rate2 = (TextView) findViewById(R.id.rate2);
        aval2 = (TextView) findViewById(R.id.aval2);
        name3 = (TextView) findViewById(R.id.name3);
        rate3 = (TextView) findViewById(R.id.rate3);
        aval3 = (TextView) findViewById(R.id.aval3);
        name4 = (TextView) findViewById(R.id.name4);
        rate4 = (TextView) findViewById(R.id.rate4);
        aval4 = (TextView) findViewById(R.id.aval4);
        name5 = (TextView) findViewById(R.id.name5);
        rate5 = (TextView) findViewById(R.id.rate5);
        aval5 = (TextView) findViewById(R.id.aval5);
        name6 = (TextView) findViewById(R.id.name6);
        rate6 = (TextView) findViewById(R.id.rate6);
        aval6 = (TextView) findViewById(R.id.aval6);
        info0 = (Button)findViewById(R.id.info0);
        info1 = (Button)findViewById(R.id.info1);
        info2 = (Button)findViewById(R.id.info2);
        info3 = (Button)findViewById(R.id.info3);
        info4 = (Button)findViewById(R.id.info4);
        info5 = (Button)findViewById(R.id.info5);
        info6 = (Button)findViewById(R.id.info6);
        TextView[] name = {name0, name1, name2, name3, name4, name5, name6};
        TextView[] rate = {rate0, rate1, rate2, rate3, rate4, rate5, rate6};
        TextView[] aval = {aval0, aval1, aval2, aval3, aval4, aval5, aval6};
        Button[] info = {info0, info1, info2, info3, info4, info5, info6};
        Intent[] intents = {intent_Dotus, intent_Grosz, intent_Kantory2, intent_Lodzinscy, intent_Meritum, intent_Vabanque, intent_Baksy};

        View.OnClickListener onClickDotus = new View.OnClickListener(){@Override public void onClick(View v) {startActivity(intent_Dotus);}};
        View.OnClickListener onClickGrosz = new View.OnClickListener(){@Override public void onClick(View v) {startActivity(intent_Grosz);}};
        View.OnClickListener onClickKantory2 = new View.OnClickListener(){@Override public void onClick(View v) {startActivity(intent_Kantory2);}};
        View.OnClickListener onClicklodzinscy = new View.OnClickListener(){@Override public void onClick(View v) {startActivity(intent_Lodzinscy);}};
        View.OnClickListener onClickMeritum = new View.OnClickListener(){@Override public void onClick(View v) {startActivity(intent_Meritum);}};
        View.OnClickListener onClickVabanque = new View.OnClickListener(){@Override public void onClick(View v) {startActivity(intent_Vabanque);}};
        View.OnClickListener onClickBaksy = new View.OnClickListener(){@Override public void onClick(View v) {startActivity(intent_Baksy);}};


        Kantor dotus = new Kantor("Dotus", onClickDotus, "ul. Limanowskiego 20,30-534 Krakow","09:00","18:00","50.0449405:19.951252","www.dotus.pl");
        Kantor grosz = new Kantor("Grosz", onClickGrosz, "Slawkowska 4, 33-332 Krakow","09:00","18:00","50.0631159:19.9348219","www.kantorgrosz.pl");
        Kantor kantory2 = new Kantor("Kantory2", onClickKantory2, "pl. Szczepanski 8, 31-011 Krakow","10:00","17:30","50.0637199:19.9337495","kantory2.pl");
        Kantor lodzinscy = new Kantor("Lodzinscy", onClicklodzinscy, "Pilotow 6, Krakow","09:00","14:00","50.0804625:19.9688462","kantor.lodzinscy.pl");
        Kantor meritum = new Kantor("Meritum",onClickMeritum,"pl. Imbramowski 2 31-217 Krakow","08:30","17:30","50.088146:19.9464313", "www.kantormeritum.pl");
        Kantor vabanque = new Kantor("Vabanque", onClickVabanque, "ul. Wielopole 13,31-072 Krakow","09:00","18:00","50.0583989:19.9423643", "www.kantorvabanque.pl");
        Kantor baksy = new Kantor("BaksyPl", onClickBaksy, "ul. Krolewska 82, 30-079 Krakow","09:00","19:00","50.0722559:19.9149632", "www.baksy.pl");




        /** В стринги ниже должнен записываться курс каждого кантора в зависимость от выбранной валюты, через свич. По типу dotus_rate="375.00"
         *
         */


        String dotus_rate = null;
        try {
            dotus_rate = dotus();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String grosz_rate = null;
        try {
            grosz_rate = grosz();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String kantory2_rate = null;
        try {
            kantory2_rate = kantory2();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String lodzinscy_rate = null;
        try {
            lodzinscy_rate = lodzinscy();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String meritum_rate = null;
        try {
            meritum_rate = meritum();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String vabanque_rate = null;
        try {
            vabanque_rate = vabanque();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String baksy_rate = null;
        try {
            baksy_rate = baksy();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        /* Здесь парсинг кончается */

        dotus.setRate(dotus_rate);
        grosz.setRate(grosz_rate);
        kantory2.setRate(kantory2_rate);
        lodzinscy.setRate(lodzinscy_rate);
        meritum.setRate(meritum_rate);
        vabanque.setRate(vabanque_rate);
        baksy.setRate(baksy_rate);

        Kantor[] kantor = {dotus, grosz, kantory2, lodzinscy, meritum, vabanque, baksy};

        for(int i=0; i<7; i++){
            intents[i].putExtra("name", kantor[i].name);
            intents[i].putExtra("adress", kantor[i].adress);
            intents[i].putExtra("timeOpen", kantor[i].timeOpen);
            intents[i].putExtra("timeClose", kantor[i].timeClose);
            intents[i].putExtra("coordinates", kantor[i].coordinates);
            intents[i].putExtra("url", kantor[i].url);

        }


        List<Kantor> kantorList = Arrays.asList(kantor);
        if(getInputDirection().equals("BUY")) {
            Collections.sort(kantorList, new Comparator<Kantor>() {
                public int compare(Kantor o1, Kantor o2) {
                    if (o1.temp_rate == o2.temp_rate)
                        return 0;
                    return o2.temp_rate < o1.temp_rate ? -1 : 1;
                }
            });
        } else{
            Collections.sort(kantorList, new Comparator<Kantor>() {
                public int compare(Kantor o1, Kantor o2) {
                    if (o1.temp_rate == o2.temp_rate)
                        return 0;
                    return o1.temp_rate < o2.temp_rate ? -1 : 1;
                }
            });
        }
        final Kantor[] kantory = kantorList.toArray(new Kantor[kantorList.size()]);
        for(int i=0; i<7; i++){
            name[i].setText(kantory[i].name);
            rate[i].setText(kantory[i].calculate());
            aval[i].setText(kantory[i].checkAval(aval[i]));
            info[i].setOnClickListener(kantory[i].onClickListener);
        }

        intentGraph.putExtra("rate",kantory[0].getRate());

        registerBest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentGraph);
            }
        });

    }


    public class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line ="";
                while((line = reader.readLine())!=null){
                    buffer.append(line);
                }

                String finalJson =  buffer.toString();
                JSONArray parentArray = new JSONArray(finalJson);
                JSONObject parentObject = parentArray.getJSONObject(Integer.parseInt(params[3]));

                String buy = parentObject.getString(params[1]);
                String sell = parentObject.getString(params[2]);
                return buy + " " + sell;
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }



    public String dotus() throws ExecutionException, InterruptedException {
        String rate ="";
        String result= null;
        if(getInputCurrency().equals("USD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/dotus.json","Kupno","Sprzedaz","0","dotus").get();
        else if(getInputCurrency().equals("EUR")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/dotus.json","Kupno","Sprzedaz","1","dotus").get();
        else if(getInputCurrency().equals("GBP")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/dotus.json","Kupno","Sprzedaz","2","dotus").get();
        else if(getInputCurrency().equals("CZK")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/dotus.json","Kupno","Sprzedaz","10","dotus").get();
        else if(getInputCurrency().equals("AUD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/dotus.json","Kupno","Sprzedaz","7","dotus").get();
        else if(getInputCurrency().equals("CAD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/dotus.json","Kupno","Sprzedaz","4","dotus").get();

        String [] finalRate = result.split(" ");
        if (input_direction.equals("BUY")) {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[0];
                    break;
                case "EUR":rate = finalRate[0];
                    break;
                case "GBP":rate = finalRate[0];
                    break;
                case "CZK":rate = finalRate[0];
                    break;
                case "AUD":rate = finalRate[0];
                    break;
                case "CAD":rate = finalRate[0];
                    break;
                default:Log.d("Blad", "Something happen");
            }
        } else {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[1];
                    break;
                case "EUR":rate = finalRate[1];
                    break;
                case "GBP":rate = finalRate[1];
                    break;
                case "CZK":rate = finalRate[1];
                    break;
                case "AUD":rate = finalRate[1];
                    break;
                case "CAD":rate = finalRate[1];
                    break;
                default:
                    Log.d("Blad", "Something happen");
            }
        }
        return rate;
    }

    public String grosz() throws ExecutionException, InterruptedException {
        String rate="";
        String result = null;
        if(getInputCurrency().equals("USD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantorGrosz.json","kupno","sprzedaz","0","grosz").get();
        else if(getInputCurrency().equals("EUR")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantorGrosz.json","kupno","sprzedaz","1","grosz").get();
        else if(getInputCurrency().equals("GBP")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantorGrosz.json","kupno","sprzedaz","9","grosz").get();
        else if(getInputCurrency().equals("CZK")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantorGrosz.json","kupno","sprzedaz","11","grosz").get();
        else if(getInputCurrency().equals("AUD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantorGrosz.json","kupno","sprzedaz","2","grosz").get();
        else if(getInputCurrency().equals("CAD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantorGrosz.json","kupno","sprzedaz","5","grosz").get();
        String [] finalRate = result.split(" ");
        if (input_direction.equals("BUY")) {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[0];
                    break;
                case "EUR":rate = finalRate[0];
                    break;
                case "GBP":rate = finalRate[0];
                    break;
                case "CZK":rate = finalRate[0];
                    break;
                case "AUD":rate = finalRate[0];
                    break;
                case "CAD":rate = finalRate[0];
                    break;
                default:Log.d("Blad", "Something happen");
            }
        } else {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[1];
                    break;
                case "EUR":rate = finalRate[1];
                    break;
                case "GBP":rate = finalRate[1];
                    break;
                case "CZK":rate = finalRate[1];
                    break;
                case "AUD":rate = finalRate[1];
                    break;
                case "CAD":rate = finalRate[1];
                    break;
                default:
                    Log.d("Blad", "Something happen");
            }
        }
        return rate;
    }




    public String kantory2() throws ExecutionException, InterruptedException {
        String rate="";
        String result = null;
        if(getInputCurrency().equals("USD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantory2.json","Zakup","Sprzedaz","0","kantory2").get();
        else if(getInputCurrency().equals("EUR")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantory2.json","Zakup","Sprzedaz","1","kantory2").get();
        else if(getInputCurrency().equals("GBP")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantory2.json","Zakup","Sprzedaz","5","kantory2").get();
        else if(getInputCurrency().equals("CZK")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantory2.json","Zakup","Sprzedaz","10","kantory2").get();
        else if(getInputCurrency().equals("AUD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantory2.json","Zakup","Sprzedaz","3","kantory2").get();
        else if(getInputCurrency().equals("CAD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/kantory2.json","Zakup","Sprzedaz","2","kantory2").get();
        String [] finalRate = result.split(" ");
        if (input_direction.equals("BUY")) {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[0];
                    break;
                case "EUR":rate = finalRate[0];
                    break;
                case "GBP":rate = finalRate[0];
                    break;
                case "CZK":rate = finalRate[0];
                    break;
                case "AUD":rate = finalRate[0];
                    break;
                case "CAD":rate = finalRate[0];
                    break;
                default:Log.d("Blad", "Something happen");
            }
        } else {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[1];
                    break;
                case "EUR":rate = finalRate[1];
                    break;
                case "GBP":rate = finalRate[1];
                    break;
                case "CZK":rate = finalRate[1];
                    break;
                case "AUD":rate = finalRate[1];
                    break;
                case "CAD":rate = finalRate[1];
                    break;
                default:
                    Log.d("Blad", "Something happen");
            }
        }
        return rate;
    }

    public String lodzinscy() throws ExecutionException, InterruptedException {
        String rate ="";
        String result = null;
        if(getInputCurrency().equals("USD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/lodzinscy.json","SKUP","SPRZEDAZ","0","lodzinsy").get();
        else if(getInputCurrency().equals("EUR")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/lodzinscy.json","SKUP","SPRZEDAZ","1","lodzinsy").get();
        else if(getInputCurrency().equals("GBP")) result=new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/lodzinscy.json","SKUP","SPRZEDAZ","2","lodzinsy").get();
        else if(getInputCurrency().equals("CZK")) result=new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/lodzinscy.json","SKUP","SPRZEDAZ","9","lodzinsy").get();
        else if(getInputCurrency().equals("AUD")) result=new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/lodzinscy.json","SKUP","SPRZEDAZ","8","lodzinsy").get();
        else if(getInputCurrency().equals("CAD")) result=new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/lodzinscy.json","SKUP","SPRZEDAZ","7","lodzinsy").get();
        String [] finalRate = result.split(" ");
        if (input_direction.equals("BUY")) {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[0];
                    break;
                case "EUR":rate = finalRate[0];
                    break;
                case "GBP":rate = finalRate[0];
                    break;
                case "CZK":rate = finalRate[0];
                    break;
                case "AUD":rate = finalRate[0];
                    break;
                case "CAD":rate = finalRate[0];
                    break;
                default:Log.d("Blad", "Something happen");
            }
        } else {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[1];
                    break;
                case "EUR":rate = finalRate[1];
                    break;
                case "GBP":rate = finalRate[1];
                    break;
                case "CZK":rate = finalRate[1];
                    break;
                case "AUD":rate = finalRate[1];
                    break;
                case "CAD":rate = finalRate[1];
                    break;
                default:
                    Log.d("Blad", "Something happen");
            }
        }
        return rate;
    }

    public String meritum() throws ExecutionException, InterruptedException {
        String rate="";
        String result = null;
        if(getInputCurrency().equals("USD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/meritum.json","Kupno","Sprzedaz","1","meritum").get();
        else if(getInputCurrency().equals("EUR")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/meritum.json","Kupno","Sprzedaz","0","meritum").get();
        else if(getInputCurrency().equals("GBP")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/meritum.json","Kupno","Sprzedaz","2","meritum").get();
        else if(getInputCurrency().equals("CZK")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/meritum.json","Kupno","Sprzedaz","9","meritum").get();
        else if(getInputCurrency().equals("AUD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/meritum.json","Kupno","Sprzedaz","8","meritum").get();
        else if(getInputCurrency().equals("CAD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/meritum.json","Kupno","Sprzedaz","7","meritum").get();
        String [] finalRate = result.split(" ");
        if (input_direction.equals("BUY")) {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[0];
                    break;
                case "EUR":rate = finalRate[0];
                    break;
                case "GBP":rate = finalRate[0];
                    break;
                case "CZK":rate = finalRate[0];
                    break;
                case "AUD":rate = finalRate[0];
                    break;
                case "CAD":rate = finalRate[0];
                    break;
                default:Log.d("Blad", "Something happen");
            }
        } else {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[1];
                    break;
                case "EUR":rate = finalRate[1];
                    break;
                case "GBP":rate = finalRate[1];
                    break;
                case "CZK":rate = finalRate[1];
                    break;
                case "AUD":rate = finalRate[1];
                    break;
                case "CAD":rate = finalRate[1];
                    break;
                default:
                    Log.d("Blad", "Something happen");
            }
        }
        return rate;
    }

    public String vabanque() throws ExecutionException, InterruptedException {
        String rate="";
        String result = null;
        if(getInputCurrency().equals("USD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/vabanque.json","kupno","sprzedaz","0","vabanque").get();
        else if(getInputCurrency().equals("EUR")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/vabanque.json","kupno","sprzedaz","1","vabanque").get();
        else if(getInputCurrency().equals("GBP")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/vabanque.json","kupno","sprzedaz","2","vabanque").get();
        else if(getInputCurrency().equals("CZK")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/vabanque.json","kupno","sprzedaz","9","vabanque").get();
        else if(getInputCurrency().equals("AUD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/vabanque.json","kupno","sprzedaz","5","vabanque").get();
        else if(getInputCurrency().equals("CAD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/vabanque.json","kupno","sprzedaz","3","vabanque").get();
        String [] finalRate = result.split(" ");
        if (input_direction.equals("BUY")) {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[0];
                    break;
                case "EUR":rate = finalRate[0];
                    break;
                case "GBP":rate = finalRate[0];
                    break;
                case "CZK":rate = finalRate[0];
                    break;
                case "AUD":rate = finalRate[0];
                    break;
                case "CAD":rate = finalRate[0];
                    break;
                default:Log.d("Blad", "Something happen");
            }
        } else {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[1];
                    break;
                case "EUR":rate = finalRate[1];
                    break;
                case "GBP":rate = finalRate[1];
                    break;
                case "CZK":rate = finalRate[1];
                    break;
                case "AUD":rate = finalRate[1];
                    break;
                case "CAD":rate = finalRate[1];
                    break;
                default:
                    Log.d("Blad", "Something happen");
            }
        }
        return rate;
    }

    public String baksy() throws ExecutionException, InterruptedException {
        String rate="";
        String result = null;
        if(getInputCurrency().equals("USD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/baksyPl.json","Skup","Sprzedaz","0","baksy").get();
        else if(getInputCurrency().equals("EUR")) result =  new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/baksyPl.json","Skup","Sprzedaz","1","baksy").get();
        else if(getInputCurrency().equals("GBP")) result =  new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/baksyPl.json","Skup","Sprzedaz","23","baksy").get();
        else if(getInputCurrency().equals("CZK")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/baksyPl.json","Skup","Sprzedaz","5","baksy").get();
        else if(getInputCurrency().equals("AUD")) result = new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/baksyPl.json","Skup","Sprzedaz","2","baksy").get();
        else if(getInputCurrency().equals("CAD")) result =  new JSONTask().execute("http://vegas-pro.ucoz.ua/JSON/baksyPl.json","Skup","Sprzedaz","9","baksy").get();
        String [] finalRate = result.split(" ");
        if (input_direction.equals("BUY")) {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[0];
                    break;
                case "EUR":rate = finalRate[0];
                    break;
                case "GBP":rate = finalRate[0];
                    break;
                case "CZK":rate = finalRate[0];
                    break;
                case "AUD":rate = finalRate[0];
                    break;
                case "CAD":rate = finalRate[0];
                    break;
                default:Log.d("Blad", "Something happen");
            }
        } else {
            switch (getInputCurrency()) {
                case "USD":rate = finalRate[1];
                    break;
                case "EUR":rate = finalRate[1];
                    break;
                case "GBP":rate = finalRate[1];
                    break;
                case "CZK":rate = finalRate[1];
                    break;
                case "AUD":rate = finalRate[1];
                    break;
                case "CAD":rate = finalRate[1];
                    break;
                default:
                    Log.d("Blad", "Something happen");
            }
        }
        return rate;
    }





}
