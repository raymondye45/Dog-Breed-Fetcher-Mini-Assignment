package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.

        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        final Request request = new Request.Builder()
                .url("https://dog.ceo/api/breeds/list/all")
                .build();

        try{
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if(responseBody.getString("status").equals("success")){
                ArrayList<String> sub_breeds = new ArrayList<>();
                JSONObject allBreeds = responseBody.getJSONObject("message");

                if (!allBreeds.has(breed)){
                    throw new BreedNotFoundException(breed + " not found!");
                }
                JSONArray sub_breeds_JSON = allBreeds.getJSONArray(breed);

                for(int i = 0; i < sub_breeds_JSON.length(); i++){
                    sub_breeds.add(sub_breeds_JSON.getString(i));
                }

                return sub_breeds;
            }
            else{
                throw new BreedNotFoundException("Error: " + responseBody.getString("message"));
            }

        }catch(BreedNotFoundException e){
            throw e;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}