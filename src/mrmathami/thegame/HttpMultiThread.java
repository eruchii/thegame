package mrmathami.thegame;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.utils.URIBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

public class HttpMultiThread implements Runnable{
    GameField field;
    String method;

    public HttpMultiThread(@Nonnull GameField field, String method){
        this.field = field;
        this.method = method;
    }
    @Override
    public void run() {
        if(method.equals("POST"))  saveToCloud();
        else loadFromCloud();
    }

    public final void saveToCloud(){
        String token = new String();
        try (final InputStream stream = this.getClass().getResourceAsStream("/token.txt")){
            if(stream == null) throw new IOException("Token file does not exists");
            final Scanner sc = new Scanner(stream);
            token = sc.next();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> file = field.createSaveFile();
        StringBuilder strSave = new StringBuilder();
        strSave.append(String.format("%d\n", file.size()));
        for(String s: file) strSave.append(s);
        String resSave = String.valueOf(strSave);
        String encodedSave = Base64.getEncoder().encodeToString(resSave.getBytes());

        try{
            String protocol = Config.PROTOCOL;
            String host = Config.HOST;
            int port = Config.PORT;
            String path = "/sendsavefile";

            URIBuilder builder = new URIBuilder();
            builder.setPath(path);
            builder.setHost(host);
            builder.setPort(port);
            builder.setScheme(protocol);
            builder.setParameter("token", token);
            String url = builder.build().toString();

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("data",encodedSave));
            post.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(post);
            System.out.println(response.getStatusLine());

        } catch (URISyntaxException | IOException e){
            e.printStackTrace();
        }
    }
    public final void loadFromCloud(){
        String token = new String();
        try (final InputStream stream = this.getClass().getResourceAsStream("/token.txt")){
            if(stream == null) throw new IOException("Token file does not exists");
            final Scanner sc = new Scanner(stream);
            token = sc.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            String protocol = Config.PROTOCOL;
            String host = Config.HOST;
            int port = Config.PORT;
            String path = "/getsavefile";

            URIBuilder builder = new URIBuilder();
            builder.setPath(path);
            builder.setHost(host);
            builder.setPort(port);
            builder.setScheme(protocol);
            builder.setParameter("token", token);
            String url = builder.build().toString();

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);

            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
            System.out.println(apiOutput);
            Object jsonString = new JSONParser().parse(apiOutput);
            JSONObject jo = (JSONObject) jsonString;
            String data = (String)jo.get("data");
            byte[] dataDecoded = Base64.getDecoder().decode(data);
            InputStream stream = new ByteArrayInputStream(dataDecoded);
            field.load(stream);

        } catch (URISyntaxException | IOException | ParseException e){
            e.printStackTrace();
        }
    }
}