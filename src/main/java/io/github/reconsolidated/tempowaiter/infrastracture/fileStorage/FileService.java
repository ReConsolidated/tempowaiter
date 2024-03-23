package io.github.reconsolidated.tempowaiter.infrastracture.fileStorage;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    private final OkHttpClient client = new OkHttpClient();
    private final String storageZoneName = "tempowaiter";
    private final String cdnUrl = "https://tempowaiter.b-cdn.net/";
    private final String apiKey = "5424a220-8da0-44be-8b72f2bcd470-f5b5-467e";
    private final String baseUrl = "https://storage.bunnycdn.com/" + storageZoneName + "/";

    public List<String> saveFiles(String path, Map<String,String> base64FileContents) {
        List<String> fileUrls = new ArrayList<>();

        for (Map.Entry<String, String> entry : base64FileContents.entrySet()) {
            String base64Content = entry.getValue();
            String fileName = entry.getKey();
            String base64Data = base64Content.split(",")[1];
            byte[] data = Base64.getDecoder().decode(base64Data);
            String filePath = path + "/" + System.currentTimeMillis() + "_" + fileName.replace(' ', '_');
            String url = baseUrl + filePath;

            RequestBody body = RequestBody.create(data);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("AccessKey", apiKey)
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    fileUrls.add(cdnUrl + filePath);
                } else {
                    // Handle failure
                    System.out.println("Failed to upload file: " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileUrls;
    }
}

