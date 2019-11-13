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
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class HttpMultiThread implements Runnable{
    GameField field;
    String method;
    String token;
    public HttpMultiThread(@Nonnull GameField field, String method){
        this.field = field;
        this.method = method;
        this.token = this.getToken();
    }
    @Override
    public void run() {
        if(method.equals("POST"))  saveToCloud();
        else loadFromCloud();
    }

    public final String getToken(){
        String token = null;
        try (final InputStream stream = new FileInputStream(new File("./token.txt"))){
            if(stream == null) throw new IOException("Token file does not exists");
            final Scanner sc = new Scanner(stream);
            token = sc.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    public final String getURL(String path, String token) throws URISyntaxException {
        String protocol = Config.PROTOCOL;
        String host = Config.HOST;
        int port = Config.PORT;
        URIBuilder builder = new URIBuilder();
        builder.setPath(path);
        builder.setHost(host);
        builder.setPort(port);
        builder.setScheme(protocol);
        builder.setParameter("token", token);
        String url = builder.build().toString();
        return url;
    }

    public final void saveToCloud(){
        field.pauseGame();
        List<String> file = field.createSaveFile();
        field.unpauseGame();
        StringBuilder strSave = new StringBuilder();
        strSave.append(String.format("%d\n", file.size()));
        for(String s: file) strSave.append(s);
        String resSave = String.valueOf(strSave);
        String encodedSave = Base64.getEncoder().encodeToString(resSave.getBytes());
        try{
            String url = getURL("/sendsavefile", this.token);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("data",encodedSave));
            post.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(post);

        } catch (URISyntaxException | IOException e){
            e.printStackTrace();
        }
        System.out.println("\nSave!");
    }
    public final void loadFromCloud(){
        try{
            String url = this.getURL("/getsavefile", this.token);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);

            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
            Object jsonString = new JSONParser().parse(apiOutput);
            JSONObject jo = (JSONObject) jsonString;
            String data = (String)jo.get("data");
            byte[] dataDecoded = Base64.getDecoder().decode(data);
            InputStream stream = new ByteArrayInputStream(dataDecoded);
            field.pauseGame();
            field.load(stream);
            field.unpauseGame();

        } catch (URISyntaxException | IOException | ParseException e){
            e.printStackTrace();
        }
        System.out.println("\nLoad!");
    }
}