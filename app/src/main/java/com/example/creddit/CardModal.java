package com.example.creddit;

public class CardModal {

    int card_profile_image, card_image;
    String card_title, posted_by, card_description;

    public CardModal(int card_profile_image, int card_image, String card_title, String posted_by, String card_description) {
        this.card_profile_image = card_profile_image;
        this.card_image = card_image;
        this.card_title = card_title;
        this.posted_by = posted_by;
        this.card_description = card_description;
    }

    public int getCard_profile_image() {
        return card_profile_image;
    }

    public void setCard_profile_image(int card_profile_image) {
        this.card_profile_image = card_profile_image;
    }

    public int getCard_image() {
        return card_image;
    }

    public void setCard_image(int card_image) {
        this.card_image = card_image;
    }

    public String getCard_title() {
        return card_title;
    }

    public void setCard_title(String card_title) {
        this.card_title = card_title;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getCard_description() {
        return card_description;
    }

    public void setCard_description(String card_description) {
        this.card_description = card_description;
    }
}
