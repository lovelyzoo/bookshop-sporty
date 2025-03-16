package com.garvin.bookstore.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("lpscheme")
public class LpSchemeProperties {
    private long bookCost;
    private long bundleThreshold;

    public long getBookCost() {
        return bookCost;
    }

    public void setBookCost(long bookCost) {
        this.bookCost = bookCost;
    }

    public long getBundleThreshold() {
        return bundleThreshold;
    }

    public void setBundleThreshold(long bundleThreshold) {
        this.bundleThreshold = bundleThreshold;
    }
}
