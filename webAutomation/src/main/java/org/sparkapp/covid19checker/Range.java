/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sparkapp.covid19checker;

/**
 *
 * @author rowta
 */
public enum Range {
    r5MILES("5 miles"),
    r10MILES("10 miles"),
    r25MILES("25 miles"),
    r50MILES("50 miles"),
    r100MILES("100 miles");

    public final String distance;

    private Range(String distance) {
        this.distance = distance;
    }    

    /**
     * @return the distance
     */
    public String getDistance() {
        return distance;
    }
}
