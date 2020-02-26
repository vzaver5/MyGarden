import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanman = new Scanner(System.in);
        final String token = "T2F6bmtvTG8rSjBNcmhIVnBhUmVZQT09";


        String search = "";
        String get_url="";
        int option;

        System.out.println("================================================================================");
        System.out.println("\t\t\t\tWelcome to the PlantDex\n");
        System.out.println("================================================================================");
        System.out.println("What would you like to search?");
        System.out.println("1. Kingdom \n" +
                "2. Subkingdom\n"+
                "3. Division\n"+
                "4. Species\n" +
                "5. Family\n"+
                "6. Genus\n"+
                "7. Name\n"+
                "8. ID");
        try{
            option = scanman.nextInt();

            switch(option){
                case 1:
                    search = "kingdoms/";
                    get_url = "https://trefle.io/api/"+ search +"?token="+token;
                    break;
                case 2:
                    search = "subkingdoms/";
                    get_url = "https://trefle.io/api/"+ search +"?token="+token;
                    break;
                case 3:
                    search = "divisions/";
                    get_url = "https://trefle.io/api/"+ search +"?token="+token;
                    break;
                case 4:
                    search = "species/";
                    get_url = "https://trefle.io/api/"+ search +"?token="+token;
                    break;
                case 5:
                    search = "families/";
                    get_url = "https://trefle.io/api/"+ search +"?token="+token;
                    break;
                case 6:
                    search = "genuses/";
                    get_url = "https://trefle.io/api/"+ search +"?token="+token;
                    break;
                case 7:
                    search = "plants/";
                    scanman.nextLine();	//eat up the new line
                    System.out.println("Enter the plant's name(sci or com)");
                    String name = scanman.nextLine();
                    name = name.replaceAll("\\s", "_");
                    get_url = "https://trefle.io/api/"+ search +"?token="+token+"&q="+name;
                    break;
                case 8:
                    System.out.println("Enter the plant's id");
                    int id = scanman.nextInt();
                    get_url = "https://trefle.io/api/plants/"+ id +"/"+"?token="+token;
                    break;
                default:
                    System.out.println("Not a valid option");
            }
            System.out.println(get_url);
            sendGET(get_url,option);

        } catch (InputMismatchException e) {
            System.err.println("Wrong input! Input a number between 1 - 7...");
        }
    }

    //Purpose: GET request to reach the url provided
    private static void sendGET(String url,int option) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        //System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success 200
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            //System.out.println(response.toString());
            if(option == 8){
                JSONPlant(response.toString());
            }else{
                parseJSON(response.toString(), option);
            }

        } else {
            System.out.println("GET request failed");
        }
    }

    //Purpose: deserialize the Json object into a java object then display in a readable fashion
    static void parseJSON(String jsonString, int option){
        Gson gson= new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(jsonString).getAsJsonArray();



        if(option == 1){            //Kingdom chosen. parse kingdom aspects
            Kingdoms kingdoms = gson.fromJson(array.get(0), Kingdoms.class);
            System.out.println("The name of the kingdom is " + kingdoms.name);

        }else if(option == 2){      //Subkingdom
            //Determined we have a subkingdom so
            //we make a subkingdom class
            Subkingdoms subkingdoms = gson.fromJson(array.get(0), Subkingdoms.class);	//How we want to parse. Want to parse as a subkingdom
            System.out.println("The name of the subkingdom is " + subkingdoms.name);
        }else if(option == 3){      //Division
            System.out.println("The name of the divisions are:\n");
            for(int i = 0; i < array.size(); i++){
                Divisions divisions = gson.fromJson(array.get(i), Divisions.class);
                System.out.println("\n"+ (i+1) + ". " + divisions.name);
            }
        }else if(option == 4){      //Species
            System.out.println("Species");
        }else if(option == 5){      //Family
            System.out.println("Family");
        }else if(option == 6){      //Genus
            System.out.println("Genus");
        }else if(option == 7){      //Name
            System.out.println("The following plant(s) match the name you've provided:\n");
            for(int i = 0; i < array.size(); i++){
                Plants plants = gson.fromJson(array.get(i), Plants.class);
                System.out.println("\n" + (i+1) + ". " + plants.scientific_name + " --> id: " + plants.id);
            }
            //Want to store this id and the plant name in an array
            //When they decide what plant they want (have to access info using id)
            //Display information
        }
    }

/*	private static void sendPOST() throws IOException {
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}
*/

    //Purpose: When the user wants to look at a certain plant and get its info and display in readable fashion
    static void JSONPlant(String jsonString){
        Gson gson = new Gson();
        Plants plants = gson.fromJson(jsonString, Plants.class);
        System.out.println("Plant info:\n\tScientific Name: " + plants.scientific_name);
        System.out.println("\tCommon Name: " + plants.common_name);
        for(int i = 0; i < plants.images.length; i++){
            System.out.println("\tImages link: " + plants.images[i].url);
        }
    }

    //Function to add plant to the user's "Garden"
    static void addToMyPlant(){
        //TO DO. Do once firebase set up.
    }
}