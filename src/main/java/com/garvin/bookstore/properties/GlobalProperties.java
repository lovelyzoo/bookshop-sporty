package com.garvin.bookstore.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("book")
public class GlobalProperties {

    private List<BookType> booktypes = new ArrayList<>();

    public static class BookType {
        String type;
        BigDecimal pricemodifier;
        BigDecimal bundlemodifier;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BigDecimal getPricemodifier() {
            return pricemodifier;
        }

        public void setPricemodifier(BigDecimal pricemodifier) {
            this.pricemodifier = pricemodifier;
        }

        public BigDecimal getBundlemodifier() {
            return bundlemodifier;
        }

        public void setBundlemodifier(BigDecimal bundlemodifier) {
            this.bundlemodifier = bundlemodifier;
        }
    }

    public List<BookType> getBooktypes() {
        return booktypes;
    }

    public void setBooktypes(List<BookType> booktypes) {
        this.booktypes = booktypes;
    }
}
