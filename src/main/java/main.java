import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.thoughtworks.xstream.XStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {

    private static final int GOT_number_pages=79;
    private static final String GOT_USER_REVIEWS_URL="https://www.metacritic.com/game/playstation-4/ghost-of-tsushima/user-reviews";
    private static final String USER_URL="https://www.metacritic.com/user/";


    public static void main(String[] args) throws IOException {

        //--------------------------------------------------------------------------------------------------------------
        //fetchUsers();

        //--------------------------------------------------------------------------------------------------------------
        fillUserReviews();

        //--------------------------------------------------------------------------------------------------------------
        //getStats();

    }

    private static void fetchUsers() throws IOException
    {
        List<User> users=new ArrayList<>();
        for(int i=0;i<GOT_number_pages;i++)
        {
            OkHttpClient okHttp = new OkHttpClient();
            Request request = new Request.Builder().url(GOT_USER_REVIEWS_URL+"?page="+i).get().build();
            System.out.println((i+1)+"/"+GOT_number_pages+" ::: "+GOT_USER_REVIEWS_URL+"?page="+i);
            Document doc = Jsoup.parse(okHttp.newCall(request).execute().body().string());

            for(Element row : doc.select("div.name a"))
            {
                users.add(new User(row.getElementsByTag("a").text()));
            }

        }
        FileWriter usersFile = new FileWriter("users.txt");
        usersFile.write(users.size());
        for(User user:users)
        {
            usersFile.write("\n"+user.getUsername());
        }
        usersFile.close();

    }

    private static void fillUserReviews() throws IOException {
        UserList users=new UserList();

        Scanner scanner = new Scanner(new File("users.txt"));
        int i=0;

        String totalUsers="";
        while (scanner.hasNextLine())
        {
            if(i==0)
            {
                totalUsers=scanner.nextLine();
                System.out.println(totalUsers);
            }
            else{
                //testing---REMOVE
                if (i==3)
                {
                    break;
                }
                String username =scanner.nextLine();
                OkHttpClient okHttp = new OkHttpClient();
                Request request = new Request.Builder().url(USER_URL+username).get().build();
                System.out.println(i+"/"+totalUsers+" ::: "+USER_URL+username);

                Document doc = Jsoup.parse(okHttp.newCall(request).execute().body().string());

                ReviewList reviews=new ReviewList();
                for(Element row : doc.select("div.review_section.review_data"))
                {
                    Elements review_stats=row.select("div.review_stats");
                    Elements review_body=row.select("div.review_body");

                    String game=review_stats.select("div.review_product").select("div.product_title").text();
                    String commentary=review_body.select("span.blurb.blurb_expanded").text();
                    String date=review_stats.select("div.review_product").select("div.date").text();;
                    String score=row.select("div.review_score").text();

                    Review review=new Review(game,commentary,date,score);
                    reviews.add(review);
                }
                User user=new User(username,reviews.getReviews());
                users.add(user);
            }
            i++;
        }
        scanner.close();

        XStream xstream = new XStream();
        xstream.alias("user", User.class);
        xstream.alias("users", UserList.class);
        xstream.alias("review", Review.class);
        xstream.alias("reviews", ReviewList.class);

        xstream.addImplicitCollection(UserList.class, "list");
        xstream.addImplicitCollection(ReviewList.class, "list");

        String xml = xstream.toXML(users);
        FileWriter usersXML = new FileWriter("users.xml");
        usersXML.write(xml);
        usersXML.close();
    }

    private static void getStats() throws IOException {

        /*String xml = Files.readString(Paths.get("users.xml"));

        XStream xstream = new XStream();
        xstream.alias("user", User.class);
        xstream.alias("users", UserList.class);
        xstream.addImplicitCollection(UserList.class, "list");
        UserList users = (UserList) xstream.fromXML(xml);

        int GOT_reviews=users.size();
        int TLOU_reviews=0;
        int User_oneReview=0;
        int User_twoReviews=0;
        int[][] score_matrix=new int[11][11];

        for(User user:users.getUsers())
        {
            if(user.getGOT_Score()!=null)
            {
                if(user.getTLOU_Score()!=null)
                {
                    TLOU_reviews++;
                    for(int i=0; i<11; i++)
                    {
                        if(user.getGOT_Score().equals(String.valueOf(i)))
                        {
                            int TLOU_score=Integer.parseInt(user.getTLOU_Score());
                            score_matrix[i][TLOU_score]++;
                        }
                    }
                    if(user.getGOT_Score().equals("10") && user.getTotalGameReviewed().equals("2"))
                    {
                        User_twoReviews++;
                    }
                }
                if(user.getGOT_Score().equals("10") && user.getTotalGameReviewed().equals("1"))
                {
                    User_oneReview++;
                }
            }
        }

        System.out.print("Total reviews for GOT :" + GOT_reviews);
        System.out.println(", of which " + TLOU_reviews+ " reviewed TLOU2 ("+(100.0*TLOU_reviews/GOT_reviews)+"%)");
        for(int[] GOT:score_matrix)
        {
            System.out.println(Arrays.toString(GOT));
        }

        System.out.println(User_oneReview);
        System.out.println(User_twoReviews);*/

    }


}
