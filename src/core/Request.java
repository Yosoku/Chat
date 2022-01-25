package core;

import java.util.ArrayList;
import java.util.Arrays;

public class Request {
    public final int FN_ID;
    public final ArrayList<String> params = new ArrayList<>();



    public Request(String requestStr) throws NumberFormatException,ArrayIndexOutOfBoundsException{
        String[] split = requestStr.split(" ");
        FN_ID = Integer.parseInt(split[4]);
        params.addAll(Arrays.asList(split).subList(5, split.length));
        if(params.isEmpty()) throw new ArrayIndexOutOfBoundsException("Args is empty");
    }
}
