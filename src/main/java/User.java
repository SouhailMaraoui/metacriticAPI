public class User {

    private String username;
    private String totalGameReviewed;
    private String GOT_Score;
    private String TLOU_Score;

    public User()
    {

    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String totalGameReviewed, String GOT_Score, String TLOU_Score) {
        this.username = username;
        this.totalGameReviewed = totalGameReviewed;
        this.GOT_Score = GOT_Score;
        this.TLOU_Score = TLOU_Score;
    }

    public String getGOT_Score() {
        return GOT_Score;
    }

    public void setGOT_Score(String GOT_Score) {
        this.GOT_Score = GOT_Score;
    }

    public String getTLOU_Score() {
        return TLOU_Score;
    }

    public void setTLOU_Score(String TLOU_Score) {
        this.TLOU_Score = TLOU_Score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalGameReviewed() {
        return totalGameReviewed;
    }

    public void setTotalGameReviewed(String totalGameReviewed) {
        this.totalGameReviewed = totalGameReviewed;
    }
}
