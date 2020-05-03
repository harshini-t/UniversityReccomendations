
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UniversityDataParser {
    private String baseURL;
    private Document currentDoc;
    Map<String, String> universityLinks;
    Map<String, String> universityRank;
    Map<String, String> universityTuition;
    Map<String, String> universitySubSchools;
    Map<String, String> universityFinancialAid;
    Map<String, String> universityAddmissionRate;
    Map<String, ArrayList<String>> calendarUniversity;
    Map<String, ArrayList<String>> universityLocation;
    Map<String, ArrayList<String>> publicPrivateUniversity;
    Map<String, ArrayList<String>> settingToUniversity;
    Map<String, ArrayList<String>> sizeToUniversity;

    public UniversityDataParser() {
        this.baseURL = "https://www.4icu.org/top-universities-north-america/"
                + "?fbclid=IwAR3mNd9z5hXoGPjyG3D5Ej9fvHpzGuAVjnr-5pngZXONWrINPtydgAAvC5I";
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            System.out.println("Could not get universities :(");
        }
    }

    // university - link
    public void getUniversityLinks() {
        this.universityLinks = new HashMap<String, String>();
        Elements universities = this.currentDoc.select("a");
        for (Element uni : universities) {
            if (uni.attr("href").contains("reviews") && !uni.attr("href").contains("index2")) {
                String href = "https://www.4icu.org" + uni.attr("href");
                String UniversityName = uni.text();
                universityLinks.put(UniversityName, href);
            }
        }
    }

    // university - rank
    public Map<String, String> getCountryRank() {
        this.universityRank = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("td").text();
            String splitInfo[] = info.split(" ");
            universityRank.put(name, splitInfo[2]);
        }
        return universityRank;
    }

    // university - schools in university
    public Map<String, String> getSchoolsInUniversity() {
        this.universitySubSchools = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[style=margin:auto]").text();
            int index = info.indexOf("|");
            info = info.substring(index + 1);
            universitySubSchools.put(name, info);
        }
        return universityRank;
    }

    // university - financial aid
    public Map<String, String> getFinancialAid() {
        this.universityFinancialAid = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[class=table borderless]").text();
            String[] infoSplit = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : infoSplit) {
                arrayList.add(x);
            }
            int index = arrayList.indexOf("Aids");
            String finAid = arrayList.get(index + 1);
            if (finAid.contains("Yes")) {
                universityFinancialAid.put(name, finAid);
            } else {
                universityFinancialAid.put(name, "Not Reported");
            }
        }
        return universityFinancialAid;
    }

    // university - admission rate
    public Map<String, String> getAdmissionRate() {
        this.universityAddmissionRate = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            System.out.println(name);
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[class=table borderless]").text();
            String[] infoSplit = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : infoSplit) {
                arrayList.add(x);
            }
            int index = arrayList.indexOf("Rate");

            String addRate = arrayList.get(index + 1);
            if (addRate.contains("Not")) {
                universityAddmissionRate.put(name, "Not Available");
            } else {
                universityAddmissionRate.put(name, addRate);
            }
        }
        return universityAddmissionRate;
    }

    // university - tuition
    public Map<String, String> getTuition() {
        this.universityTuition = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("td").text();
            String[] splitInfo = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : splitInfo) {
                arrayList.add(x);
            }
            int index = arrayList.indexOf("US$");
            universityTuition.put(name, arrayList.get(index - 1));
        }
        return universityRank;
    }

    // state - university
    public Map<String, ArrayList<String>> getAddress() {
        this.universityLocation = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[class=table borderless]").text();
            String[] infoSplit = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : infoSplit) {
                arrayList.add(x);
            }

            int index = arrayList.indexOf("United");
            int index2 = arrayList.indexOf("Canada");
            String fullCampusState = "";
            if (index >= 0) {
                String campusState = arrayList.get(index - 1);
                if (campusState.equals("Hampshire")|| campusState.equals("Jersey")|| 
                        campusState.equals("York")|| campusState.equals("Mexico")||
                        campusState.equals("Carolina")||campusState.equals("Dakota")||
                        campusState.equals("Island")|| campusState.equals("Virginia")) {
                    if (!arrayList.get(index - 2).equals("West") && campusState.equals("Virginia")) {
                        fullCampusState = campusState;
                    } else {
                        fullCampusState = arrayList.get(index - 2) + " " + campusState;
                    }
                } else {
                    fullCampusState = campusState;
                }
            } else {
                fullCampusState = arrayList.get(index2 - 1);
            }
            if (universityLocation.containsKey(fullCampusState)) {
                universityLocation.get(fullCampusState).add(name);
            } else {
                ArrayList<String> newStateList = new ArrayList<String>();
                newStateList.add(name);
                universityLocation.put(fullCampusState, newStateList);
            }
        }
        return universityLocation;
    }

    // Academic Calendar(Semesters, Quarters, Trimesters, 4-1-4, Other) - university
    public Map<String, ArrayList<String>> getAcademicSystem() {
        this.calendarUniversity = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[class=table borderless]").text();
            String[] infoSplit = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : infoSplit) {
                arrayList.add(x);
            }
            int index = arrayList.indexOf("Calendar");
            String campusCalendar = arrayList.get(index + 1);
            if (campusCalendar.equals("Not")) {
                campusCalendar = campusCalendar + " reported";
            }
            if (calendarUniversity.containsKey(campusCalendar)) {
                calendarUniversity.get(campusCalendar).add(name);
            } else {
                ArrayList<String> newStateList = new ArrayList<String>();
                newStateList.add(name);
                calendarUniversity.put(campusCalendar, newStateList);
            }
        }
        return calendarUniversity;
    }

    // campus type (public/private) - university
    public Map<String, ArrayList<String>> getPublicPrivate() {
        this.publicPrivateUniversity = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[class=table borderless]").text();
            String[] infoSplit = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : infoSplit) {
                arrayList.add(x);
            }
            int index = arrayList.indexOf("Type");
            arrayList.remove(index);
            int index2 = arrayList.indexOf("Type");
            String campusType = arrayList.get(index2 + 1);
            if (campusType.equals("Public")) {
                if (publicPrivateUniversity.containsKey("Public")) {
                    publicPrivateUniversity.get("Public").add(name);
                } else {
                    ArrayList<String> publicList = new ArrayList<String>();
                    publicList.add(name);
                    publicPrivateUniversity.put("Public", publicList);
                }
            } else {
                if (publicPrivateUniversity.containsKey("Private")) {
                    publicPrivateUniversity.get("Private").add(name);
                } else {
                    ArrayList<String> privateList = new ArrayList<String>();
                    privateList.add(name);
                    publicPrivateUniversity.put("Private", privateList);
                }
            }
        }
        return publicPrivateUniversity;
    }

    // campus setting (rural/urban/suburban) - university
    public Map<String, ArrayList<String>> getCampusSetting() {
        this.settingToUniversity = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[class=table borderless]").text();
            String[] infoSplit = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : infoSplit) {
                arrayList.add(x);
            }
            int index = arrayList.indexOf("Setting");

            String campusSetting = arrayList.get(index + 1);
            if (campusSetting.equals("Urban")) {
                if (settingToUniversity.containsKey("Urban")) {
                    settingToUniversity.get("Urban").add(name);
                } else {
                    ArrayList<String> urbanList = new ArrayList<String>();
                    urbanList.add(name);
                    settingToUniversity.put("Urban", urbanList);
                }
            } else if (campusSetting.equals("Suburban")) {
                if (settingToUniversity.containsKey("Suburban")) {
                    settingToUniversity.get("Suburban").add(name);
                } else {
                    ArrayList<String> subList = new ArrayList<String>();
                    subList.add(name);
                    settingToUniversity.put("Suburban", subList);
                }
            } else {
                if (settingToUniversity.containsKey("Rural")) {
                    settingToUniversity.get("Rural").add(name);
                } else {
                    ArrayList<String> ruralList = new ArrayList<String>();
                    ruralList.add(name);
                    settingToUniversity.put("Rural", ruralList);
                }
            }

        }
        return settingToUniversity;
    }

    // university - size 
    // small - 20,000 lower
    // medium - 40,000 lower
    // large 40,000 higher
    public Map<String, ArrayList<String>> getEnrollmentSizes() {
        this.sizeToUniversity = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, String> entry : this.universityLinks.entrySet()) {
            String name = entry.getKey();
            String link = entry.getValue();
            int index = 0;
            try {
                this.currentDoc = Jsoup.connect(link).get();
            } catch (IOException e) {
                System.out.println("Could not get the university :");
            }
            String info = this.currentDoc.select("table[class=table borderless]").text();
            String[] splitInfo = info.split(" ");
            ArrayList<String> arrayList = new ArrayList<String>();
            for (String x : splitInfo) {
                arrayList.add(x);
            }
            if (!name.contains("Iowa State University of")) {
                index = arrayList.indexOf("Enrollment");
                index = index + 1;
            } else {
                index = arrayList.indexOf("Control");
                index = index - 4;
            }
            String numberAsString = arrayList.get(index);
            String str = numberAsString.replaceAll("[a-zA-Z]", "");

            int indexDash = str.indexOf("-");
            if (indexDash >= 0) {
                str = str.substring(indexDash + 1);
            }
            str = str.replaceAll(",", "");
            int enrollment = Integer.parseInt(str);
            if (enrollment <= 20000) {
                if (sizeToUniversity.containsKey("small")) {
                    sizeToUniversity.get("small").add(name);
                } else {
                    ArrayList<String> smallList = new ArrayList<String>();
                    smallList.add(name);
                    sizeToUniversity.put("small", smallList);
                }
            } else if (enrollment <= 40000) {
                if (sizeToUniversity.containsKey("medium")) {
                    sizeToUniversity.get("medium").add(name);
                } else {
                    ArrayList<String> medList = new ArrayList<String>();
                    medList.add(name);
                    sizeToUniversity.put("medium", medList);
                }
            } else {
                if (sizeToUniversity.containsKey("large")) {
                    sizeToUniversity.get("large").add(name);
                } else {
                    ArrayList<String> largeList = new ArrayList<String>();
                    largeList.add(name);
                    sizeToUniversity.put("large", largeList);
                }
            }
        }
        return sizeToUniversity;
    }
}
