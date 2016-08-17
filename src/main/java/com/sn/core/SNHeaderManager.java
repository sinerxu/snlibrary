package com.sn.core;

import com.sn.models.SNHeader;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by xuhui on 16/6/30.
 */
public class SNHeaderManager {
    public static SNHeader parse(Header header) {
        SNHeader snHeader = new SNHeader();
        if (header != null) {
            snHeader.setValue(header.getValue());
            snHeader.setName(header.getName());

        }
        return snHeader;
    }

    public static ArrayList<SNHeader> parse(Header[] headers) {
        ArrayList<SNHeader> snHeaders = new ArrayList<SNHeader>();
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                snHeaders.add(parse(header));
            }
        }
        return snHeaders;
    }


    public static String findByName(ArrayList<SNHeader> headers, String name) {
        String result = "";
        if (headers != null && headers.size() > 0) {
            for (SNHeader header : headers) {
                if (header.getName().equals(name)) result = header.getValue();
            }
        }
        return result;
    }

}
