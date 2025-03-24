package me.escoffier.parasol;

public class ClaimSummary {

    public String title;
    public String summary;

    public String date;

    public String location;

    public String policyNumber;

    @Override
    public String toString() {
        return "ClaimSummary{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                '}';
    }
}
