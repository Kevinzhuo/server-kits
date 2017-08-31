package com.huya.server.kits.serverkits.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/28.
 */
public class CommonUtils {

    private static final String SEP1 = "&";
    private static final String SEP2 = "&";
    private static final String SEP3 = "=";

    public CommonUtils() {
    }

    public static String ListToString(List<?> list) {
        if (list != null && !list.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            if (list != null && list.size() > 0) {
                for(int i = 0; i < list.size(); ++i) {
                    if (list.get(i) != null && list.get(i) != "") {
                        if (list.get(i) instanceof List) {
                            sb.append(ListToString((List)list.get(i)));
                            sb.append("&");
                        } else if (list.get(i) instanceof Map) {
                            sb.append(MapToString((Map)list.get(i)));
                            sb.append("&");
                        } else {
                            sb.append(list.get(i));
                            sb.append("&");
                        }
                    }
                }
            }

            return sb.toString();
        } else {
            return null;
        }
    }

    public static String MapToString(Map<?, ?> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            Iterator var3 = map.keySet().iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                if (obj != null) {
                    Object value = map.get(obj);
                    if (value instanceof List) {
                        sb.append(obj.toString() + "&" + ListToString((List)value));
                        sb.append("&");
                    } else if (value instanceof Map) {
                        sb.append(obj.toString() + "&" + MapToString((Map)value));
                        sb.append("&");
                    } else {
                        sb.append(obj.toString() + "=" + (value == null ? "" : value.toString()));
                        sb.append("&");
                    }
                }
            }

            return sb.toString();
        } else {
            return null;
        }
    }

}
