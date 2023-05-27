package Entities;

import Enums.CountryEnum;
import Enums.GenderEnum;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class HeadersMessage {
    public HashMap<String, Object> headers = new HashMap<String, Object>();
    public String title;
    public String content = "";
    public CountryEnum country;

    public HashMap<String, Object> getHeaders() {
        return headers;
    }
    public void setHeaders(List<GenderEnum> messageGenders) {
        for (GenderEnum gender: messageGenders) {
            this.headers.put(gender.toString(), country.toString());
        }
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public CountryEnum getCountry() {
        return this.country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public String getFormattedMessage() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        return "[" + timeStamp + "] " + this.getTitle() + "\n" + this.getContent();
    }
}
