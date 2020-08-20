import okhttp3.*;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String path = "/v1/3rdsys/terminals/1000161344";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String query = "sysKey=" + Parameters.systemAccessKey + "&timestamp=" + timestamp;
        String signature = "";

        try{
            signature = SysHelper.encryptHMAC(query, Parameters.systemAccessSecret);
        }catch (Exception e){
            e.printStackTrace();
        }

        HttpUrl.Builder builder = HttpUrl.parse(Parameters.url + path).newBuilder();
        builder.addQueryParameter("sysKey", Parameters.systemAccessKey);
        builder.addQueryParameter("timestamp", timestamp);
        String s = builder.build().toString();

        if(!signature.equals("")) {
            Request request = new Request.Builder()
                                        .header("signature", signature)
                                        .url(s)
                                        .build();

            try{
                System.out.println("Request: " + request.toString());
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(!response.isSuccessful())
                            throw new IOException("Unexcepted code: " + response);
                        else {
                            System.out.println("Response: " + response.toString());

                            String jsonData = "";
                            BufferedReader br = null;
                            try {
                                String line;
                                br = new BufferedReader(response.body().charStream());
                                while ((line = br.readLine()) != null) {
                                    jsonData += line + "\n";
                                }
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("body: " + jsonData);
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
